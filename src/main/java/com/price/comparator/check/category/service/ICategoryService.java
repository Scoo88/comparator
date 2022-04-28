package com.price.comparator.check.category.service;

import com.price.comparator.check.category.dto.CategoryDto;
import com.price.comparator.check.enums.CategoryLevel;
import com.price.comparator.check.store.exception.PriceException;

import java.util.List;

public interface ICategoryService {
    List<CategoryDto> createCategory();
    List<CategoryDto> getAll();

    CategoryDto getById(Long id) throws PriceException;

    List<CategoryDto> getCategories(String storeName, String categoryName, CategoryLevel categoryLevel,
            boolean activeStatus);

    CategoryDto updateCategory();
    String deleteCategory();

}
