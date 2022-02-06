package com.price.comparator.entity.links;

import com.price.comparator.enums.CategoryEnums;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "links_category_list")
public class LinksCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String categoryId;
    private String link;
    private String title;
    private CategoryEnums level;
}
