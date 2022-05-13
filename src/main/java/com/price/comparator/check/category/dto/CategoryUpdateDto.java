package com.price.comparator.check.category.dto;

import lombok.Data;

@Data
public class CategoryUpdateDto {
    private String categoryName;
    private String categoryUrl;
    private Boolean active;
}
