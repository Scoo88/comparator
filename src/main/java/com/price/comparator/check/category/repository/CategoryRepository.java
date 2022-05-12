package com.price.comparator.check.category.repository;

import com.price.comparator.check.category.entity.Category;
import com.price.comparator.check.category.repository.query.CategoryRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryRepositoryCustom {
    Optional<Category> findByCategoryNameAndCategoryUrl(String categoryName, String categoryUrl);

    Optional<Category> findByCategoryUrl(String url);
}
