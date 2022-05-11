package com.price.comparator.check.category.entity;

import com.price.comparator.check.store.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;
    private LocalDateTime createdAt;
    private String categoryName;
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;
    @ManyToOne(fetch = FetchType.LAZY)
    private Category parentCategory;
    private String categoryUrl;
    private Boolean active;
    private LocalDateTime activeStatusChange;
}
