package com.price.comparator.check.category.service;

import com.price.comparator.check.category.dto.CategoryResponse;
import com.price.comparator.check.category.dto.CategoryUpdateDto;
import com.price.comparator.check.store.exception.PriceException;

import java.util.List;

public interface ICategoryService {
    List<CategoryResponse> createCategory();
    List<CategoryResponse> getAll();

    CategoryResponse getById(Long id) throws PriceException;

    List<CategoryResponse> getCategories(Long storeId, String categoryName, Long parentCategoryId,
            Boolean activeStatus);

    CategoryResponse updateCategory(Long id, CategoryUpdateDto categoryUpdateDto) throws PriceException;
    String deleteCategory();

}
