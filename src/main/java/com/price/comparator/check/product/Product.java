package com.price.comparator.check.product;

import com.price.comparator.check.category.Category;
import com.price.comparator.check.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;
    private String productName;
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    private BigDecimal price;
    private BigDecimal priceDiscounted;
    private Currency priceCurrency;
    private Currency priceDiscountedCurrency;

}
