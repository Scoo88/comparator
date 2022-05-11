package com.price.comparator.check.category.service;

import com.price.comparator.check.category.dto.CategoryDto;
import com.price.comparator.check.category.entity.Category;
import com.price.comparator.check.category.repository.CategoryRepository;
import com.price.comparator.check.enums.CategoryLevel;
import com.price.comparator.check.store.entity.Store;
import com.price.comparator.check.store.exception.Messages;
import com.price.comparator.check.store.exception.PriceException;
import com.price.comparator.check.store.repository.StoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
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
    ModelMapper modelMapper;

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

                if (categoryDto.getCategory() != null){
                    Optional<Category> parentCategory =
                            categoryRepository.findByCategoryUrl(categoryDto.getCategory().getCategoryUrl());
                    category.setCategory(parentCategory.get());
                } else {
                    category.setCategory(null);
                }

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
        List<CategoryDto> response;
        List<Category> listOfCategoriesFromDb = categoryRepository.findAll();
        response = List.of(modelMapper.map(listOfCategoriesFromDb, CategoryDto[].class));
        return response;
    }

    @Override
    public CategoryDto getById(Long id) throws PriceException {
        CategoryDto response;
        Optional<Category> categoryFromDb = categoryRepository.findById(id);
        if (categoryFromDb.isEmpty()){
            log.error(Messages.CATEGORY_NOT_FOUND.getMessage());
            throw new PriceException(Messages.CATEGORY_NOT_FOUND);
        }
        response = modelMapper.map(categoryFromDb.get(), CategoryDto.class);

        return response;
    }

    @Override
    public List<CategoryDto> getCategories(String storeName, String categoryName, CategoryLevel categoryLevel,
            boolean activeStatus) {
        List<CategoryDto> response = new ArrayList<>();



        return response;
    }

    @Override
    public CategoryDto updateCategory() {
        return null;
    }

    @Override
    public String deleteCategory() {
        return null;
    }

}
