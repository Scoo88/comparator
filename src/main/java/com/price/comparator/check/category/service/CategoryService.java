package com.price.comparator.check.category.service;

import com.price.comparator.check.category.dto.CategoryDto;
import com.price.comparator.check.enums.CategoryLevel;
import com.price.comparator.check.store.entity.Store;
import com.price.comparator.check.store.exception.PriceException;
import com.price.comparator.check.store.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryService implements ICategoryService{

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    LinksCategoryService linksCategoryService;

    @Autowired
    HgspotCategoryService hgspotCategoryService;

    @Override
    public List<CategoryDto> createCategory() {
        List<CategoryDto> response = new ArrayList<>();
        List<Store> getActiveStores = storeRepository.findByActive(true);

        getActiveStores.forEach(activeStore -> {
            switch (activeStore.getStoreName().toLowerCase()){
            case "links":
                try {
                    response.addAll(linksCategoryService.create(activeStore));
                } catch (PriceException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "hgspot":
                break;
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
