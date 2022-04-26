package com.price.comparator.check.category.dto;

import com.price.comparator.check.enums.CategoryLevel;
import com.price.comparator.check.store.entity.Store;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CategoryDto {
    private LocalDateTime createdAt;
    private String categoryName;
    private Store store;
    private CategoryLevel categoryLevel;
    private String categoryUrl;
    private Boolean active;
    private LocalDateTime activeStatusChange;
}
