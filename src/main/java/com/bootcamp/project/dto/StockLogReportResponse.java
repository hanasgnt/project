package com.bootcamp.project.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StockLogReportResponse {

    private Long id;
    private String productName;
    private Integer quantity;
    private String type;
    private LocalDateTime createdAt;
}