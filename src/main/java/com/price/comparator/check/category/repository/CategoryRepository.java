package com.price.comparator.check.category.repository;

import com.price.comparator.check.category.entity.Category;
import com.price.comparator.check.enums.CategoryLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByCategoryName(String name);

//    Optional<Category> findByStoreByCategoryName(Store store,
//            String categoryName);

    List<Category> findByStoreIdAndCategoryLevel(Long id, CategoryLevel categoryLevel);

    List<Category> findByCategoryLevel(CategoryLevel categoryLevel);
}
