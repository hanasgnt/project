package com.bootcamp.project.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SalesReportResponse {

    private Long transactionId;
    private LocalDateTime transactionDate;
    private String customerName;
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal total;
}