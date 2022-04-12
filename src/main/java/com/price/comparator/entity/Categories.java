package com.price.comparator.entity;

import com.price.comparator.enums.CategoryEnums;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "links_categories")
public class Categories {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private LocalDateTime dateCreated;
    private String categoryId;
    private String link;
    private String category;
    private CategoryEnums level;
    private boolean active = false;
}
