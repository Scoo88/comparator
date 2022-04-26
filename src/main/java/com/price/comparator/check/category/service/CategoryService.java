package com.price.comparator.check.category.service;

import com.price.comparator.check.category.dto.CategoryDto;
import com.price.comparator.check.store.entity.Store;
import com.price.comparator.check.store.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
                response.addAll(linksCategoryService.create(activeStore));
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
}
