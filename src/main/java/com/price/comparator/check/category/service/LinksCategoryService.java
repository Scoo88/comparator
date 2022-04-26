package com.price.comparator.check.category.service;

import com.price.comparator.check.category.dto.CategoryDto;
import com.price.comparator.check.category.repository.CategoryRepository;
import com.price.comparator.check.enums.CategoryLevel;
import com.price.comparator.check.store.entity.Store;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LinksCategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    public List<CategoryDto> create(Store store) {
        List<CategoryDto> response = new ArrayList<>();

        response.addAll(getCategoriesFirstLevel(store));
        response.addAll(getCategoriesSecondLevel(store, response));
        response.addAll(getCategoriesThirdLevel(store, response));

        return response;
    }

    // private methods
    private List<CategoryDto> getCategoriesFirstLevel(Store store) {
        CategoryLevel level = CategoryLevel.FIRST_LEVEL;
        List<CategoryDto> response = new ArrayList<>();

        try {
            response = mapFromJsoupToCategories(store, level, store.getLink());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    private List<CategoryDto> getCategoriesSecondLevel(Store store, List<CategoryDto> higherLevelCategories) {
        List<CategoryDto> response = new ArrayList<>();
        CategoryLevel level = CategoryLevel.SECOND_LEVEL;
        List<CategoryDto> firstLevelCategories =
                higherLevelCategories.stream().filter(categoryDto -> categoryDto.getCategoryLevel().equals(CategoryLevel.FIRST_LEVEL)).collect(
                        Collectors.toList());

        firstLevelCategories.forEach(firstLevel -> {
            String url = firstLevel.getCategoryUrl();
            String category = firstLevel.getCategoryName();
            try {
                response.addAll(mapFromJsoupToCategories(store, level, url));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return response;
    }

    private List<CategoryDto> getCategoriesThirdLevel(Store store, List<CategoryDto> higherLevelCategories) {
        List<CategoryDto> response = new ArrayList<>();

        CategoryLevel level = CategoryLevel.THIRD_LEVEL;

        List<CategoryDto> secondLevelCategories =
                higherLevelCategories.stream().filter(categoryDto -> categoryDto.getCategoryLevel().equals(CategoryLevel.SECOND_LEVEL)).collect(
                        Collectors.toList());

        secondLevelCategories.forEach(firstLevel -> {
            String url = firstLevel.getCategoryUrl();
            try {
                response.addAll(mapFromJsoupToCategories(store, level, url));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return response;
    }

    private List<CategoryDto> mapFromJsoupToCategories(Store store, CategoryLevel categoryLevel, String url)
            throws IOException {
        List<CategoryDto> response = new ArrayList<>();

        Document document = Jsoup.connect(url).get();
        Elements elementsByClass = document.getElementsByClass( "active").select("ul").first().children();

        elementsByClass.forEach(element -> {
            CategoryDto category = new CategoryDto();

            category.setCategoryLevel(categoryLevel);
            category.setCreatedAt(LocalDateTime.now());
            category.setActive(false);
            category.setCategoryName(element.children().first().children().get(1).text());
            category.setCategoryUrl(store.getLink() + element.children().first().attr("href"));
            category.setActiveStatusChange(category.getCreatedAt());
            category.setStore(store);
            response.add(category);
        });

        return response;
    }

}
