package com.price.comparator.check.category.service;

import com.price.comparator.check.category.dto.CategoryDto;
import com.price.comparator.check.category.entity.Category;
import com.price.comparator.check.store.entity.Store;
import com.price.comparator.check.store.exception.PriceException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class LinksCategoryService {
    public List<CategoryDto> create(Store store) throws PriceException {
        List<CategoryDto> response = new ArrayList<>();

        int numberOfLevels = 2;

        for (int level = 1; level <= numberOfLevels; level++) {
            List<CategoryDto> getNextCategoryLevel = getNextCategoryLevel(store, response);
            getNextCategoryLevel.forEach(categoryDto -> {
                String checkDuplicate = categoryDto.getCategoryUrl();
                if (response.stream().noneMatch(categoryDto1 -> categoryDto1.getCategoryUrl().equals(checkDuplicate))) {
                    response.addAll(getNextCategoryLevel);
                }
            });
        }

        return response;
    }

    // private methods

    private List<CategoryDto> getNextCategoryLevel(Store store, List<CategoryDto> higherLevelCategories) {
        List<CategoryDto> response;
        Integer nextCategoryLevel = calculateNextCategoryLevel(higherLevelCategories);

        if (higherLevelCategories.isEmpty()) {
            response = getCategories(store, nextCategoryLevel, new CategoryDto());
        } else {
            Integer previousCategoryLevel = calculatePreviousCategoryLevel(higherLevelCategories);
            List<CategoryDto> nextLevelCategories = new ArrayList<>();
            List<CategoryDto> previousLevelCategories = higherLevelCategories.stream().filter(
                    categoryDto -> categoryDto.getCategoryLevel().equals(previousCategoryLevel)).collect(
                    Collectors.toList());

            previousLevelCategories.forEach(categoryDto -> {
                nextLevelCategories.addAll(getCategories(store, nextCategoryLevel, categoryDto));
            });
            response = nextLevelCategories;
        }

        return response;
    }

    private Integer calculateNextCategoryLevel(List<CategoryDto> categoryDtoList) {
        Optional<CategoryDto> checkCategoryWithMaxLevel = categoryDtoList.stream().max(
                Comparator.comparing(CategoryDto::getCategoryLevel));

        if (checkCategoryWithMaxLevel.isEmpty()) {
            return 0;
        }

        Integer currentCategoryLevel = checkCategoryWithMaxLevel.get().getCategoryLevel();

        int calculateNextCategoryLevel = currentCategoryLevel + 1;

        return calculateNextCategoryLevel;
    }

    private Integer calculatePreviousCategoryLevel(List<CategoryDto> categoryDtoList) {
        Optional<CategoryDto> checkCategoryWithMaxLevel = categoryDtoList.stream().max(
                Comparator.comparing(CategoryDto::getCategoryLevel));

        if (checkCategoryWithMaxLevel.isEmpty()) {
            return 0;
        }

        Integer currentCategoryLevel = checkCategoryWithMaxLevel.get().getCategoryLevel();

        return currentCategoryLevel;
    }

    private List<CategoryDto> getCategories(Store store, Integer nextCategoryLevel, CategoryDto parentCategory) {
        List<CategoryDto> response = new ArrayList<>();
        try {
            if (parentCategory.getStore() == null) {
                parentCategory.setCategoryUrl(store.getLink());
                parentCategory.setCategoryName("");
            }
            response = mapFromJsoupToCategories(store, nextCategoryLevel, parentCategory);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    @Autowired
    ModelMapper modelMapper;

    private List<CategoryDto> mapFromJsoupToCategories(Store store, Integer categoryLevel, CategoryDto parentCategory)
            throws IOException {
        List<CategoryDto> response = new ArrayList<>();

        Document document = Jsoup.connect(parentCategory.getCategoryUrl()).get();
        Elements elementsByClass = document.getElementsByClass("active").select("ul").first().children();

        elementsByClass.forEach(element -> {
            CategoryDto category = new CategoryDto();

            category.setCategoryLevel(categoryLevel);
            category.setCreatedAt(LocalDateTime.now());
            category.setActive(false);
            if (parentCategory.getCategoryName().isEmpty()) {
                category.setCategory(null);
            } else {
                category.setCategory(modelMapper.map(parentCategory, Category.class));
            }

            if (categoryLevel.equals(0)) {
                category.setCategoryName(element.children().first().children().get(1).text());
            } else {
                category.setCategoryName(
                        parentCategory.getCategoryName() + " | " + element.children().first().children().get(1).text());
            }

            category.setCategoryUrl(store.getLink() + element.children().first().attr("href"));
            category.setActiveStatusChange(category.getCreatedAt());
            category.setStore(store);

            response.add(category);
        });

        return response;
    }

}
