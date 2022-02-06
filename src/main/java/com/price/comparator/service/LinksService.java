package com.price.comparator.service;

import com.price.comparator.entity.links.LinksCategory;
import com.price.comparator.enums.CategoryEnums;
import com.price.comparator.repository.LinksRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.price.comparator.enums.CategoryEnums.*;

@org.springframework.stereotype.Service
public class LinksService {

    @Autowired
    LinksRepository linksRepository;

    @Value("${url.links}")
    String urlLinks;

    @Value("${url.links.page-properties}")
    String pageProperties;

    public List<LinksCategory> getCategoriesFirstLevel() throws IOException {
        String category = "cat-IT";
        CategoryEnums level = FIRST_LEVEL;
        List<LinksCategory> response = mapFromJsoupToCategories(urlLinks, category, level);
        return response;
    }

    public List<LinksCategory> getCategoriesSecondLevel() throws IOException {
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

        response = linksRepository.findByLevel(SECOND_LEVEL);

        return response;
    }

    public List<LinksCategory> getCategoriesThirdLevel() throws IOException {
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

        response = linksRepository.findByLevel(THIRD_LEVEL);

        return response;
    }

    public void getProducts() throws IOException {
        Optional<LinksCategory> category = linksRepository.findByTitle("Grafičke kartice");
        Document document = Jsoup.connect(category.get().getLink()+pageProperties).get();
        Elements elements = document.getElementsByClass("item-grid");
        System.out.println(elements.first().toString());
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
            response.add(linksCategory);

            Optional<LinksCategory> checkDb = linksRepository.findByCategoryId(linksCategory.getCategoryId());
            if (checkDb.isEmpty()) {
                linksRepository.saveAndFlush(linksCategory);
            }
        });
        return response;
    }
}
