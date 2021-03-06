package com.price.comparator.check.category.dto;

import com.price.comparator.check.category.entity.Category;
import com.price.comparator.check.store.entity.Store;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CategoryDto {
    private LocalDateTime createdAt;
    private String categoryName;
    private Store store;
    private Category parentCategory;
    private Integer categoryLevel;
    private String categoryUrl;
    private Boolean active;
    private LocalDateTime activeStatusChange;
}
