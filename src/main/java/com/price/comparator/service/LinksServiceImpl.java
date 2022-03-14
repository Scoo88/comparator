package com.price.comparator.service;

import com.price.comparator.entity.links.LinksCategory;
import com.price.comparator.entity.links.LinksProduct;
import com.price.comparator.enums.CategoryEnums;
import com.price.comparator.repository.LinksRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.price.comparator.enums.CategoryEnums.*;

@Service
public class LinksServiceImpl implements LinksService{

    @Autowired
    LinksRepository linksRepository;

    @Value("${url.links}")
    String urlLinks;

    @Value("${url.links.page-properties}")
    String pageProperties;

    public List<LinksCategory> getAllCategoriesFromSite(){
        List<LinksCategory> response = new ArrayList<>();

        response.addAll(getCategoriesFirstLevel());
        response.addAll(getCategoriesSecondLevel());
        response.addAll(getCategoriesThirdLevel());

        return response;
    }

    public void getProductsFromSite() throws IOException {
        int currentPageNumber = 1;
        String pageProperty = pageProperties + currentPageNumber;
        Optional<LinksCategory> category = linksRepository.findByTitle("Grafičke kartice");

        Document document = Jsoup.connect(category.get().getLink()+pageProperty).get();

        Elements pages = document.getElementsByClass("individual-page");
        int totalPages = currentPageNumber + pages.size();

//        rješenje za loop stranica!!!!
//        do {
//
//            currentPageNumber++;
//        } while(currentPageNumber <= totalPages);

        Elements elements = document.getElementsByClass("product-item");

        elements.forEach(oneElement -> {
            LinksProduct linksProduct = new LinksProduct();

            linksProduct.setCategoryId(category.get().getCategoryId());
            linksProduct.setDateCreated(new Date());
            linksProduct.setTitle(oneElement.getElementsByClass("product-title").text());
            linksProduct.setCategory(category.get().getTitle());

            String priceInteger =
                    oneElement.getElementsByClass("price actual-price").first().childNodes().get(0).toString().replace(".", "").trim();
            String priceDecimals = oneElement.getElementsByClass("decimalPlaces").first().text();

            String priceToParse = priceInteger + "." + priceDecimals;

            BigDecimal finalPrice = BigDecimal.valueOf(Double.parseDouble(priceToParse)).setScale(2, RoundingMode.HALF_EVEN);

            linksProduct.setPrice(finalPrice);

        });
    }


    /*private methods----------------------------------------------------------------------------------------------------*/
    private List<LinksCategory> getCategoriesFirstLevel() {
        String category = "cat-IT";
        CategoryEnums level = FIRST_LEVEL;
        List<LinksCategory> response;

        try {
            mapFromJsoupToCategories(urlLinks, category, level);
        } catch (IOException e) {
            e.printStackTrace();
        }

        response = linksRepository.findByLevel(level);


        return response;
    }

    private List<LinksCategory> getCategoriesSecondLevel() {
        List<LinksCategory> response;

        List<LinksCategory> getCategoriesFromDb = linksRepository.findByLevel(FIRST_LEVEL);
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

        response = linksRepository.findByLevel(level);

        return response;
    }

    private List<LinksCategory> getCategoriesThirdLevel() {
        List<LinksCategory> response;

        List<LinksCategory> getCategoriesFromDb = linksRepository.findByLevel(SECOND_LEVEL);
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

        response = linksRepository.findByLevel(level);

        return response;
    }

    private List<LinksCategory> mapFromJsoupToCategories(String url, String category, CategoryEnums categoryEnum) throws IOException {
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

            Optional<LinksCategory> checkDb = linksRepository.findByCategoryId(linksCategory.getCategoryId());
            if (checkDb.isEmpty()) {
                linksRepository.saveAndFlush(linksCategory);
            }
        });
        return response;
    }
}
