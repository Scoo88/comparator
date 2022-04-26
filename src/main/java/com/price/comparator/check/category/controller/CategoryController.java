package com.price.comparator.check.category.controller;

import com.price.comparator.check.category.dto.CategoryDto;
import com.price.comparator.check.category.service.ICategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
@Tag(name = "Category")
public class CategoryController {
    @Autowired
    ICategoryService categoryService;

    @PostMapping("/create-category")
    public ResponseEntity<List<CategoryDto>> createStore() {
        return new ResponseEntity<>(categoryService.createCategory(), HttpStatus.OK);
    }
}
