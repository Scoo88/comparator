package com.price.comparator.entity.links;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "links_products")
public class LinksProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private Date dateCreated;
    private String categoryId;
    private String category;
    private String title;
    private BigDecimal webshopPrice;
    private BigDecimal shopPrice;
}
