package com.price.comparator.check.category.service;

import com.price.comparator.check.category.dto.CategoryDto;
import com.price.comparator.check.category.repository.CategoryRepository;
import com.price.comparator.check.enums.CategoryLevel;
import com.price.comparator.check.store.entity.Store;
import com.price.comparator.check.store.exception.Messages;
import com.price.comparator.check.store.exception.PriceException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class LinksCategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    public List<CategoryDto> create(Store store) throws PriceException {
        List<CategoryDto> response = new ArrayList<>();

        int numberOfLevels = 3;

        for (int level = 0; level <= numberOfLevels; level++) {
            response.addAll(getNextCategoryLevel(store, response));
        }

        return response;
    }

    private List<CategoryDto> getCategories(Store store, CategoryLevel nextCategoryLevel, String url){
        List<CategoryDto> response = new ArrayList<>();
        try {
            response = mapFromJsoupToCategories(store, nextCategoryLevel, url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private List<CategoryDto> getNextCategoryLevel(Store store, List<CategoryDto> higherLevelCategories)
            throws PriceException {
        List<CategoryDto> response;
        CategoryLevel nextCategoryLevel = calculateNextCategoryLevel(higherLevelCategories);

        if (higherLevelCategories.isEmpty()){
            response = getCategories(store, nextCategoryLevel, store.getLink());
        } else {
            List<CategoryDto> nextLevelCategories = new ArrayList<>();
            List<CategoryDto> previousLevelCategories = higherLevelCategories.stream().filter(
                    categoryDto -> categoryDto.getCategoryLevel().equals(CategoryLevel.FIRST_LEVEL)).collect(
                    Collectors.toList());

            previousLevelCategories.forEach(level -> {
                String url = level.getCategoryUrl();
                nextLevelCategories.addAll(getCategories(store, nextCategoryLevel, url));
            });
            response = nextLevelCategories;
        }


        return response;
    }

    private CategoryLevel calculateNextCategoryLevel(List<CategoryDto> categoryDtoList) throws PriceException {
        Optional<CategoryDto> checkCategoryWithMaxLevel = categoryDtoList.stream().max(
                Comparator.comparing(CategoryDto::getCategoryLevel));

        if (checkCategoryWithMaxLevel.isEmpty()) {
            return CategoryLevel.FIRST_LEVEL;
        }

        CategoryLevel currentCategoryLevel = checkCategoryWithMaxLevel.get().getCategoryLevel();

        int calculateNextCategoryLevel = currentCategoryLevel.ordinal() + 1;
        Optional<CategoryLevel> retrieveCategoryLevel = Arrays.stream(CategoryLevel.values()).filter(
                categoryLevel -> categoryLevel.getLevel() == calculateNextCategoryLevel).findFirst();

        if (retrieveCategoryLevel.isEmpty()){
            throw new PriceException(Messages.UNSUPPORTED_CATEGORY_LEVEL);
        }

        CategoryLevel response = retrieveCategoryLevel.get();

        return response;
    }

    // private methods

    private List<CategoryDto> getCategoriesFirstLevel(Store store) {
        List<CategoryDto> response = new ArrayList<>();
        CategoryLevel level = CategoryLevel.FIRST_LEVEL;

        try {
            response = mapFromJsoupToCategories(store, level, store.getLink());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    private List<CategoryDto> getCategoriesThirdLevel(Store store, List<CategoryDto> higherLevelCategories) {
        List<CategoryDto> response = new ArrayList<>();
        CategoryLevel level = CategoryLevel.THIRD_LEVEL;

        List<CategoryDto> secondLevelCategories = higherLevelCategories.stream().filter(
                categoryDto -> categoryDto.getCategoryLevel().equals(CategoryLevel.SECOND_LEVEL)).collect(
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
        Elements elementsByClass = document.getElementsByClass("active").select("ul").first().children();

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
