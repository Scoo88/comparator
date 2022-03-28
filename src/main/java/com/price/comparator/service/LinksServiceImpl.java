package com.price.comparator.service;

import com.price.comparator.entity.links.LinksCategory;
import com.price.comparator.entity.links.LinksProduct;
import com.price.comparator.enums.CategoryEnums;
import com.price.comparator.repository.LinksCategoriesRepository;
import com.price.comparator.repository.LinksProductRepository;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static com.price.comparator.enums.CategoryEnums.*;

@Service
public class LinksServiceImpl implements LinksService {

    @Autowired
    LinksCategoriesRepository linksCategoriesRepository;

    @Autowired
    LinksProductRepository linksProductRepository;

    @Value("${url.links}")
    String urlLinks;

    @Value("${url.links.page-properties}")
    String pageProperties;

    public List<LinksCategory> createAllCategoriesFromSite() {
        List<LinksCategory> response = new ArrayList<>();

        response.addAll(getCategoriesFirstLevel());
        response.addAll(getCategoriesSecondLevel());
        response.addAll(getCategoriesThirdLevel());

        return response;
    }

    public List<LinksCategory> getCategoriesFromDb() {
        return linksCategoriesRepository.findAll();
    }

    public List<LinksCategory> getCategoriesByTitle(String title) {
        return linksCategoriesRepository.findByTitleIgnoreCaseContaining(title);
    }

    public void createProductsFromSite() throws IOException {
        List<LinksCategory> categories = linksCategoriesRepository.findByActive(true);

        categories.forEach(category -> {
            int currentPageNumber = 1;
            int totalPages = 0;

            // page loop
            do {
                String pageProperty = pageProperties + currentPageNumber;
                Document document = null;
                try {
                    document = Jsoup.connect(category.getLink() + pageProperty).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (currentPageNumber == 1) {
                    Elements pages = document.getElementsByClass("individual-page");
                    totalPages = currentPageNumber + pages.size();
                }

                // fetching and mapping product
                Elements elements = document.getElementsByClass("product-item");

                elements.forEach(oneElement -> {
                    LinksProduct linksProduct = new LinksProduct();

                    linksProduct.setCategoryId(category.getCategoryId());
                    linksProduct.setDateCreated(new Date());
                    linksProduct.setTitle(oneElement.getElementsByClass("product-title").text());
                    linksProduct.setCategory(category.getTitle());
                    linksProduct.setLink(urlLinks + oneElement.getElementsByAttribute("title").attr("href"));

                    // webshop price
                    String webshopCurrency = oneElement.getElementsByClass("price actual-price").first().children().get(
                            1).text();
                    String webshopPrice = oneElement.getElementsByClass("price actual-price").first().text().replace(
                            webshopCurrency, "").replace(".", "").trim();
                    BigDecimal webshopPriceFinal = BigDecimal.valueOf(Double.parseDouble(webshopPrice) / 100).setScale(
                            2, RoundingMode.HALF_EVEN);
                    linksProduct.setWebshopPrice(webshopPriceFinal);
                    linksProduct.setWebshopCurrency(webshopCurrency);

                    if (oneElement.getElementsByClass("price old-price").first() != null) {
                        // shop price
                        String shopCurrency = oneElement.getElementsByClass("price old-price").first().children().get(
                                1).text();

                        String shopPrice = oneElement.getElementsByClass("price old-price").first().text().replace(
                                shopCurrency, "").replace(".", "").trim();

                        BigDecimal shopPriceFinal = BigDecimal.valueOf(Double.parseDouble(shopPrice) / 100).setScale(2,
                                RoundingMode.HALF_EVEN);
                        linksProduct.setShopPrice(shopPriceFinal);
                        linksProduct.setShopCurrency(shopCurrency);
                    } else {
                        linksProduct.setShopPrice(webshopPriceFinal);
                        linksProduct.setShopCurrency(webshopCurrency);
                    }

                    List<LinksProduct> checkDb =
                            linksProductRepository.findByTitleAndDateCreatedGreaterThanAndWebshopPriceAndShopPrice(
                                    linksProduct.getTitle(),
                                    DateUtils.truncate(linksProduct.getDateCreated(), Calendar.DATE),
                                    linksProduct.getWebshopPrice(),
                                    linksProduct.getShopPrice());
                    if (checkDb.isEmpty()){
                        linksProductRepository.saveAndFlush(linksProduct);
                    }
                });
                currentPageNumber++;
            } while (currentPageNumber <= totalPages);
        });

    }

    public void changeCategoryActiveStatus(List<String> categoryIds, Boolean statusUpdate) {
        List<LinksCategory> categoryCheckDb = linksCategoriesRepository.findByCategoryIdIn(categoryIds);

        categoryCheckDb.forEach(linksCategory -> {
            if (statusUpdate == null) {
                linksCategory.setActive(false);
            } else {
                linksCategory.setActive(statusUpdate);
            }
            linksCategoriesRepository.saveAndFlush(linksCategory);
        });
    }

    /*------ private methods ----------------------------------------------------------------------------------------*/
    private List<LinksCategory> getCategoriesFirstLevel() {
        String category = "cat-IT";
        CategoryEnums level = FIRST_LEVEL;
        List<LinksCategory> response;

        try {
            mapFromJsoupToCategories(urlLinks, category, level);
        } catch (IOException e) {
            e.printStackTrace();
        }

        response = linksCategoriesRepository.findByLevel(level);

        return response;
    }

    private List<LinksCategory> getCategoriesSecondLevel() {
        List<LinksCategory> response;

        List<LinksCategory> getCategoriesFromDb = linksCategoriesRepository.findByLevel(FIRST_LEVEL);
        CategoryEnums level = SECOND_LEVEL;

        getCategoriesFromDb.forEach(firstLevel -> {
            String url = firstLevel.getLink();
            String category = firstLevel.getCategoryId();
            try {
                mapFromJsoupToCategories(url, category, level);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        response = linksCategoriesRepository.findByLevel(level);

        return response;
    }

    private List<LinksCategory> getCategoriesThirdLevel() {
        List<LinksCategory> response;

        List<LinksCategory> getCategoriesFromDb = linksCategoriesRepository.findByLevel(SECOND_LEVEL);
        CategoryEnums level = THIRD_LEVEL;

        getCategoriesFromDb.forEach(firstLevel -> {
            String url = firstLevel.getLink();
            String category = firstLevel.getCategoryId();
            try {
                mapFromJsoupToCategories(url, category, level);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        response = linksCategoriesRepository.findByLevel(level);

        return response;
    }

    private List<LinksCategory> mapFromJsoupToCategories(String url, String category, CategoryEnums categoryEnum)
            throws IOException {
        List<LinksCategory> response = new ArrayList<>();

        Document document = Jsoup.connect(url).get();
        Elements elementsByClass = document.getElementsByClass(category + " active").select("ul").first().children();

        elementsByClass.forEach(oneElement -> {
            LinksCategory linksCategory = new LinksCategory();

            linksCategory.setCategoryId(oneElement.attr("class"));
            linksCategory.setLink(this.urlLinks + oneElement.children().first().attr("href"));
            linksCategory.setTitle(oneElement.children().first().children().get(1).text());
            linksCategory.setLevel(categoryEnum);
            linksCategory.setDateCreated(new Date());
            response.add(linksCategory);

            Optional<LinksCategory> checkDb = linksCategoriesRepository.findByCategoryId(linksCategory.getCategoryId());
            if (checkDb.isEmpty()) {
                linksCategoriesRepository.saveAndFlush(linksCategory);
            }
        });
        return response;
    }
}
