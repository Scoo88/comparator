package com.price.comparator.check.category.repository.query;

import com.price.comparator.check.category.entity.Category;

import java.util.List;

public interface CategoryRepositoryCustom {
    List<Category> findCategoriesByActiveAndCategoryNameAndParentCategoryAndStore(Boolean activeStatus,
            String categoryName, Long parentCategoryId, Long storeId);
}
