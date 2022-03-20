package com.price.comparator.repository;

import com.price.comparator.entity.links.LinksProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LinksProductRepository extends JpaRepository<LinksProduct, Long> {
    Optional<LinksProduct> findByTitle(String title);
}
