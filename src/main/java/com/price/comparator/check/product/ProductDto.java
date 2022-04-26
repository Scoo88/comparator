package com.price.comparator.check.product;

import com.price.comparator.check.category.entity.Category;
import com.price.comparator.check.enums.Currency;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductDto {
    private String productName;
    private LocalDateTime createdAt;
    private Category category;
    private BigDecimal price;
    private Currency priceCurrency;
    private BigDecimal priceDiscounted;
    private Currency priceDiscountedCurrency;
}
