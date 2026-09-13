package com.bootcamp.project.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductsRequest {

    @NotBlank(message = "Product name is required")
    @Size(max = 255, message = "Product name must not exceed 255 characters")
    private String productName;

    private Long supplierId;

    private Long categoryId;

    @Size(max = 255, message = "Quantity per unit must not exceed 255 characters")
    private String quantityPerUnit;

    @DecimalMin(value = "0.0", message = "Unit price must not be negative")
    private BigDecimal unitPrice;

    private Short unitsInStock;

    private Short unitsOnOrder;

    private Short reorderLevel;

    @NotNull(message = "Discontinued is required")
    private Boolean discontinued;
}