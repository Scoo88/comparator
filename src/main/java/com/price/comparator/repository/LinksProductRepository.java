package com.price.comparator.repository;

import com.price.comparator.entity.links.LinksProduct;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface LinksProductRepository extends JpaRepository<LinksProduct, Long> {
    @Query(value = "select product from LinksProduct product where product.title = :title order by product"
            + ".dateCreated desc limit 1")
    Optional<LinksProduct> findByTitle(String title);

    Optional<LinksProduct> findFirstByTitleOrderByDateCreatedDesc(String title);

    List<LinksProduct> findByTitleAndDateCreatedGreaterThanAndWebshopPriceAndShopPrice(String title, Date date,
            BigDecimal webshopPrice,
            BigDecimal shopPrice);
}
