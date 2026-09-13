package com.bootcamp.project.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDetailRequest {

    @NotNull
    private Long productId;

    @NotNull
    @Positive
    private Integer quantity;

    private BigDecimal unitPrice;
}