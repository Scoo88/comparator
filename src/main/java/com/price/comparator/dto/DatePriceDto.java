package com.price.comparator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DatePriceDto {
    private String category;
    private String product;
    private Map<LocalDate, BigDecimal> webshopPricePerDate;
    private Map<LocalDate, BigDecimal> shopPricePerDate;
}


