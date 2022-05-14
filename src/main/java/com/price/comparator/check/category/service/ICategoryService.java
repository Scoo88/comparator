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
            Boolean activeStatus) throws PriceException;

    CategoryResponse updateCategory(Long id, CategoryUpdateDto categoryUpdateDto) throws PriceException;

    List<CategoryResponse> changeActiveStatus(List<Long> listOfIds, boolean activeStatus) throws PriceException;

    List<CategoryResponse> changeChildrenActiveStatus(List<Long> listOfIds);

    String deleteCategory();

}
