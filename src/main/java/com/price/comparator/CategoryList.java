package com.price.comparator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryList {
    private String className;
    private String link;
    private int productCount;
    private String title;
}
