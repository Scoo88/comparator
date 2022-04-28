package com.price.comparator.check.category.service;

import com.price.comparator.check.category.dto.CategoryDto;
import com.price.comparator.check.category.entity.Category;
import com.price.comparator.check.category.repository.CategoryRepository;
import com.price.comparator.check.enums.CategoryLevel;
import com.price.comparator.check.store.entity.Store;
import com.price.comparator.check.store.exception.PriceException;
import com.price.comparator.check.store.repository.StoreRepository;
import com.price.comparator.config.ModelMapperConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryService implements ICategoryService{

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    LinksCategoryService linksCategoryService;

    @Autowired
    HgspotCategoryService hgspotCategoryService;

    @Autowired
    ModelMapperConfig modelMapperConfig;

    @Override
    public List<CategoryDto> createCategory() {
        List<CategoryDto> response = new ArrayList<>();
        List<Store> getActiveStores = storeRepository.findByActive(true);

        getActiveStores.forEach(activeStore -> {
            try {
                switch (activeStore.getStoreName().toLowerCase()){
                case "links":
                    response.addAll(linksCategoryService.create(activeStore));
                    break;
                case "hgspot":
                    break;
                }
            } catch (PriceException e) {
                throw new RuntimeException(e);
            }
        });

        response.forEach(categoryDto -> {
            Optional<Category> checkIfCategoryExists =
                    categoryRepository.findByCategoryNameAndCategoryUrl(categoryDto.getCategoryName(),
                    categoryDto.getCategoryUrl());
            if (checkIfCategoryExists.isEmpty()){
                Category category = new Category();
                category.setCreatedAt(categoryDto.getCreatedAt());
                category.setCategoryName(categoryDto.getCategoryName());
                category.setStore(categoryDto.getStore());
                category.setCategoryLevel(categoryDto.getCategoryLevel());
                category.setCategoryUrl(categoryDto.getCategoryUrl());
                category.setActive(categoryDto.getActive());
                category.setActiveStatusChange(categoryDto.getActiveStatusChange());

                categoryRepository.saveAndFlush(category);
            }
        });

        return response;
    }

    @Override
    public List<CategoryDto> getAll() {
        return null;
    }

    @Override
    public CategoryDto getById() {
        return null;
    }

    @Override
    public CategoryDto getByName() {
        return null;
    }

    @Override
    public CategoryDto updateCategory() {
        return null;
    }

    @Override
    public String deleteCategory() {
        return null;
    }

    @Override
    public CategoryLevel enumTest() {

        CategoryLevel response;

        CategoryDto categoryDto1 = new CategoryDto();
        CategoryDto categoryDto2 = new CategoryDto();
        CategoryDto categoryDto3 = new CategoryDto();
        categoryDto1.setCategoryLevel(CategoryLevel.FIRST_LEVEL);
        categoryDto2.setCategoryLevel(CategoryLevel.SECOND_LEVEL);
        categoryDto3.setCategoryLevel(CategoryLevel.THIRD_LEVEL);
        List<CategoryDto> list = new ArrayList<>(Arrays.asList(categoryDto1, categoryDto2));

        int calculateNextCategoryLevel =
                list.stream().max(Comparator.comparing(CategoryDto::getCategoryLevel)).get().getCategoryLevel().ordinal() + 1;
        Optional<CategoryLevel> retrieveCategoryLevel =
                Arrays.stream(CategoryLevel.values()).filter(categoryLevel -> categoryLevel.getLevel() == calculateNextCategoryLevel).findFirst();
        response = retrieveCategoryLevel.get();

        return response;
    }
}
