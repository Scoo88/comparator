package com.price.comparator.check.category.controller;

import com.price.comparator.check.category.dto.CategoryResponse;
import com.price.comparator.check.category.dto.CategoryUpdateDto;
import com.price.comparator.check.category.service.ICategoryService;
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
    public ResponseEntity<List<CategoryResponse>> createStore() {
        return new ResponseEntity<>(categoryService.createCategory(), HttpStatus.OK);
    }

    @GetMapping("/get-all-categories")
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        return new ResponseEntity<>(categoryService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/get-category-by-ids/{categoryIds}")
    public ResponseEntity<List<CategoryResponse>> getCategoryByIds(@PathVariable List<Long> categoryIds) throws PriceException {
        return new ResponseEntity<>(categoryService.getByIds(categoryIds), HttpStatus.OK);
    }

    @GetMapping(value = "/get-categories")
    public ResponseEntity<List<CategoryResponse>> getCategories(@RequestParam(required = false) Long storeId,
            @RequestParam(required = false) String categoryName, @RequestParam(required = false) Long parentCategoryId,
            @RequestParam(required = false) Boolean activeStatus) throws PriceException {
        return new ResponseEntity<>(categoryService.getCategories(storeId, categoryName, parentCategoryId, activeStatus),
                HttpStatus.OK);
    }

    @PutMapping(value = "/update-category/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Long id,
            @RequestBody CategoryUpdateDto categoryUpdateDto)
            throws PriceException {
        return new ResponseEntity<>(categoryService.updateCategory(id, categoryUpdateDto), HttpStatus.OK);
    }

    @PutMapping(value = "/change-active-status/{categoryIds}")
    public ResponseEntity<List<CategoryResponse>> changeActiveStatus(@PathVariable List<Long> categoryIds, boolean activeStatus)
            throws PriceException {
        return new ResponseEntity<>(categoryService.changeActiveStatus(categoryIds, activeStatus), HttpStatus.OK);
    }
}
