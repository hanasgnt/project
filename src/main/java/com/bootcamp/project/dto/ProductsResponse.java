package com.bootcamp.project.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
// serializable is used to convert the object into a byte stream, which can be useful for caching, sending over a network, or saving to a file.
public class ProductsResponse implements Serializable {

    private Long id;
    private String productName;

    private Long supplierId;
    private String supplierName;

    private Long categoryId;
    private String categoryName;

    private String quantityPerUnit;
    private BigDecimal unitPrice;
    private Short unitsInStock;
    private Short unitsOnOrder;
    private Short reorderLevel;
    private Boolean discontinued;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}