package com.price.comparator.check.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreDto {
    private LocalDateTime createdAt;
    private String storeName;
    private String link;
    private Boolean active;
}
