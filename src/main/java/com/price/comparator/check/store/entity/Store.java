package com.price.comparator.check.store.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "store")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private LocalDateTime createdAt;
    private String storeName;
    private String link;
    private Boolean active;

    public Store(String storeName, String link) {
        this.storeName = storeName;
        this.link = link;
    }
}
