package com.price.comparator.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "products")
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private LocalDateTime dateCreated;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Categories category;
    private String product;
    private String link;
    private BigDecimal webshopPrice;
    private String webshopCurrency;
    private BigDecimal shopPrice;
    private String shopCurrency;
}
