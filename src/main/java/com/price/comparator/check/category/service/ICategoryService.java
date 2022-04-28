package com.price.comparator.check.category.service;

import com.price.comparator.check.category.dto.CategoryDto;
import com.price.comparator.check.store.exception.PriceException;

import java.util.List;

public interface ICategoryService {
    List<CategoryDto> createCategory();
    List<CategoryDto> getAll();

    CategoryDto getById(Long id) throws PriceException;

    CategoryDto getByName();
    CategoryDto updateCategory();
    String deleteCategory();

}
