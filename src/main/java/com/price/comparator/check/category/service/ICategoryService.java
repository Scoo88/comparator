package com.price.comparator.check.category.service;

import com.price.comparator.check.category.dto.CategoryDto;
import com.price.comparator.check.enums.CategoryLevel;

import java.util.List;

public interface ICategoryService {
    List<CategoryDto> createCategory();
    List<CategoryDto> getAll();
    CategoryDto getById();
    CategoryDto getByName();
    CategoryDto updateCategory();
    String deleteCategory();

    CategoryLevel enumTest();

}
