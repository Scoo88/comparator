package com.price.comparator.service;

import com.price.comparator.entity.links.LinksCategory;
import com.price.comparator.enums.CategoryEnums;
import com.price.comparator.repository.LinksRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
        Optional<LinksCategory> category = linksRepository.findByTitle("Grafičke kartice");
        Document document = Jsoup.connect(category.get().getLink()+pageProperties).get();
        Elements elements = document.getElementsByClass("item-grid");
        Elements elements1 = document.getElementsByClass("product-title");


        System.out.println(elements.first().toString());
        System.out.println(elements1.first().toString());
    }

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
