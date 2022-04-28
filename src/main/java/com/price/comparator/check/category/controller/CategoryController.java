package com.price.comparator.check.category.controller;

import com.price.comparator.check.category.dto.CategoryDto;
import com.price.comparator.check.category.service.ICategoryService;
import com.price.comparator.check.enums.CategoryLevel;
import com.price.comparator.check.store.exception.PriceException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/get-all-categories")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        return new ResponseEntity<>(categoryService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/get-category-by-id/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) throws PriceException {
        return new ResponseEntity<>(categoryService.getById(id), HttpStatus.OK);
    }

    @GetMapping("/get-categories")
    public ResponseEntity<List<CategoryDto>> getCategories(@RequestParam String storeName,
            @RequestParam String categoryName, @RequestParam CategoryLevel categoryLevel,
            @RequestParam boolean activeStatus) {
        return new ResponseEntity<>(categoryService.getCategories(storeName, categoryName, categoryLevel, activeStatus),
                HttpStatus.OK);
    }
}
