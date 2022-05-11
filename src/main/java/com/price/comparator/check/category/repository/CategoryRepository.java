package com.price.comparator.check.category.repository;

import com.price.comparator.check.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByCategoryNameAndCategoryUrl(String categoryName, String categoryUrl);

    Optional<Category> findByCategoryUrl(String url);
}
