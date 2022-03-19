package com.price.comparator.repository;

import com.price.comparator.entity.links.LinksCategory;
import com.price.comparator.enums.CategoryEnums;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LinksCategoriesRepository extends JpaRepository<LinksCategory, Long> {
    Optional<LinksCategory> findByTitle(String title);

    Optional<LinksCategory> findByCategoryId(String categoryId);

    List<LinksCategory> findByCategoryIdIn(List<String> categoryIds);

    List<LinksCategory> findByLevel(CategoryEnums firstLevel);

    List<LinksCategory> findByTitleIgnoreCaseContaining(String title);

    List<LinksCategory> findByActive(Boolean status);

}
