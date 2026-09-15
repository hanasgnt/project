package com.bootcamp.project.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StockLogReportResponse {
    private String productName;
    private Integer currentStock;
    private List<StockLogResponse> logs;
}