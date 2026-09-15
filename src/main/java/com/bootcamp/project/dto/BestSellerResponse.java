package com.bootcamp.project.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BestSellerResponse {

    private Long productId;
    private String productName;
    private Integer totalQuantitySold;
    private BigDecimal totalRevenue;
}