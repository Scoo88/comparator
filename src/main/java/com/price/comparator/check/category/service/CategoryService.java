package com.price.comparator.check.category.service;

import com.price.comparator.check.category.dto.CategoryDto;
import com.price.comparator.check.category.dto.CategoryResponse;
import com.price.comparator.check.category.dto.CategoryUpdateDto;
import com.price.comparator.check.category.entity.Category;
import com.price.comparator.check.category.repository.CategoryRepository;
import com.price.comparator.check.store.entity.Store;
import com.price.comparator.check.store.exception.Messages;
import com.price.comparator.check.store.exception.PriceException;
import com.price.comparator.check.store.repository.StoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public List<CategoryResponse> createCategory() {
        List<CategoryResponse> response;
        List<CategoryDto> categoriesFromStores = new ArrayList<>();
        List<Store> getActiveStores = storeRepository.findByActive(true);

        getActiveStores.forEach(activeStore -> {
            try {
                switch (activeStore.getStoreName().toLowerCase()){
                case "links":
                    categoriesFromStores.addAll(linksCategoryService.create(activeStore));
                    break;
                case "hgspot":
                    break;
                }
            } catch (PriceException e) {
                throw new RuntimeException(e);
            }
        });

        categoriesFromStores.forEach(categoryDto -> {
            Optional<Category> checkIfCategoryExists =
                    categoryRepository.findByCategoryNameAndCategoryUrl(categoryDto.getCategoryName(),
                    categoryDto.getCategoryUrl());
            if (checkIfCategoryExists.isEmpty()){
                Category category = new Category();

                if (categoryDto.getParentCategory() != null){
                    Optional<Category> parentCategory =
                            categoryRepository.findByCategoryUrl(categoryDto.getParentCategory().getCategoryUrl());
                    parentCategory.ifPresent(category::setParentCategory);
                } else {
                    category.setParentCategory(null);
                }

                category.setCreatedAt(categoryDto.getCreatedAt());
                category.setCategoryName(categoryDto.getCategoryName());
                category.setStore(categoryDto.getStore());
                category.setCategoryUrl(categoryDto.getCategoryUrl());
                category.setActive(categoryDto.getActive());
                category.setActiveStatusChange(categoryDto.getActiveStatusChange());

                categoryRepository.saveAndFlush(category);
            }
        });

        response = List.of(modelMapper.map(categoriesFromStores, CategoryResponse[].class));

        return response;
    }

    @Override
    public List<CategoryResponse> getAll() {
        List<CategoryResponse> response;
        List<Category> listOfCategoriesFromDb = categoryRepository.findAll();
        response = List.of(modelMapper.map(listOfCategoriesFromDb, CategoryResponse[].class));
        return response;
    }

    @Override
    public CategoryResponse getById(Long id) throws PriceException {
        CategoryResponse response;
        Optional<Category> categoryFromDb = categoryRepository.findById(id);
        if (categoryFromDb.isEmpty()){
            log.error(Messages.CATEGORY_NOT_FOUND.getMessage());
            throw new PriceException(Messages.CATEGORY_NOT_FOUND);
        }
        response = modelMapper.map(categoryFromDb.get(), CategoryResponse.class);

        return response;
    }

    @Override
    public List<CategoryResponse> getCategories(Long storeId, String categoryName, Long parentCategoryId,
            Boolean activeStatus) throws PriceException {
        List<CategoryResponse> response;
        List<Category> list = categoryRepository.findCategoriesByActiveAndCategoryNameAndParentCategoryAndStore(activeStatus,
                categoryName, parentCategoryId, storeId);

        if (list.isEmpty()){
            throw new PriceException(Messages.CATEGORY_NOT_FOUND);
        }

        response = List.of(modelMapper.map(list, CategoryResponse[].class));

        return response;
    }

    @Override
    public CategoryResponse updateCategory(Long id, CategoryUpdateDto categoryUpdateDto) throws PriceException {
        CategoryResponse response;

        Optional<Category> checkDb = categoryRepository.findById(id);

        if (checkDb.isEmpty()){
            throw new PriceException(Messages.CATEGORY_NOT_FOUND);
        }

        Category categoryToUpdate = checkDb.get();

        if (categoryUpdateDto.getActive() != null){
            categoryToUpdate.setActive(categoryUpdateDto.getActive());
            categoryToUpdate.setActiveStatusChange(LocalDateTime.now());
        }
        if (categoryUpdateDto.getCategoryName() != null){
            categoryToUpdate.setCategoryName(categoryUpdateDto.getCategoryName());
        }
        if (categoryUpdateDto.getCategoryUrl() != null){
            categoryToUpdate.setCategoryUrl(categoryUpdateDto.getCategoryUrl());
        }

        categoryRepository.save(categoryToUpdate);

        response = modelMapper.map(categoryToUpdate, CategoryResponse.class);

        return response;
    }

    public List<CategoryResponse> changeActiveStatus(List<Long> listOfIds, boolean activeStatus) throws PriceException {
        List<CategoryResponse> response = new ArrayList<>();
        List<Category> fetchFromDb = categoryRepository.findByIdIn(listOfIds);

        if (fetchFromDb.isEmpty()){
            throw new PriceException(Messages.CATEGORY_NOT_FOUND);
        }

        fetchFromDb.forEach(category -> {
            category.setActive(activeStatus);
            category.setActiveStatusChange(LocalDateTime.now());
            categoryRepository.saveAndFlush(category);

            response.add(modelMapper.map(category, CategoryResponse.class));
        });

        return response;
    }

    @Override
    public List<CategoryResponse> changeChildrenActiveStatus(List<Long> listOfIds) {
        return null;
    }

    @Override
    public String deleteCategory() {
        return null;
    }

}
