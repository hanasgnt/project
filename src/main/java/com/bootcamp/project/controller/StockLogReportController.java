package com.bootcamp.project.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.project.dto.StockLogReportResponse;
import com.bootcamp.project.service.StockLogReportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/reports")
@Tag(name = "Reports")
public class StockLogReportController {

    private final StockLogReportService stockLogReportService;

    public StockLogReportController(
            StockLogReportService stockLogReportService) {
        this.stockLogReportService = stockLogReportService;
    }

    @GetMapping("/stock-logs")
    @Operation(summary = "Get stock log report")
    public List<StockLogReportResponse> getStockLogReport(
            @RequestParam(value = "start_date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

            @RequestParam(value = "end_date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return stockLogReportService.getStockLogReport(
                startDate,
                endDate);
    }
}