package com.bootcamp.project.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.project.dto.SalesReportResponse;
import com.bootcamp.project.service.SalesReportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/reports")
@Tag(name = "Reports")
public class SalesReportController {

    private final SalesReportService salesReportService;

    public SalesReportController(
            SalesReportService salesReportService) {
        this.salesReportService = salesReportService;
    }

    @GetMapping("/sales")
    @Operation(summary = "Get sales report")
    public List<SalesReportResponse> getSalesReport(
            @RequestParam(value = "start_date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

            @RequestParam(value = "end_date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return salesReportService.getSalesReport(
                startDate,
                endDate);
    }
}