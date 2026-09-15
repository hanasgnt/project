package com.bootcamp.project.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.bootcamp.project.dto.StockLogReportResponse;
import com.bootcamp.project.dto.StockLogResponse;
import com.bootcamp.project.entity.StockLogs;
import com.bootcamp.project.repository.StockLogsRepository;

@Service
public class StockLogReportService {

    private final StockLogsRepository stockLogsRepository;

    public StockLogReportService(
            StockLogsRepository stockLogsRepository) {
        this.stockLogsRepository = stockLogsRepository;
    }

    public List<StockLogReportResponse> getStockLogReport(
            LocalDate startDate,
            LocalDate endDate) {

        List<StockLogs> stockLogs;

        if (startDate == null && endDate == null) {

            stockLogs = stockLogsRepository.findAll();

        } else {

            LocalDateTime startDateTime = startDate != null
                    ? startDate.atStartOfDay()
                    : LocalDateTime.MIN;

            LocalDateTime endDateTime = endDate != null
                    ? endDate.plusDays(1).atStartOfDay()
                    : LocalDateTime.MAX;

            stockLogs = stockLogsRepository.findStockLogReport(
                    startDateTime,
                    endDateTime);
        }

        Map<String, List<StockLogResponse>> logsByProduct = new LinkedHashMap<>();

        Map<String, Integer> stockByProduct = new LinkedHashMap<>();

        for (StockLogs stockLog : stockLogs) {

            String productName = stockLog.getProduct().getProductName();

            Integer currentStock = stockLog.getProduct().getUnitsInStock() == null
                    ? 0
                    : stockLog.getProduct().getUnitsInStock().intValue();

            logsByProduct
                    .computeIfAbsent(productName, key -> new ArrayList<>())
                    .add(new StockLogResponse(
                            stockLog.getId(),
                            stockLog.getQuantity(),
                            stockLog.getType(),
                            stockLog.getCreatedAt()));

            stockByProduct.put(productName, currentStock);
        }

        List<StockLogReportResponse> result = new ArrayList<>();

        for (Map.Entry<String, List<StockLogResponse>> entry : logsByProduct.entrySet()) {

            String productName = entry.getKey();

            result.add(new StockLogReportResponse(
                    productName,
                    stockByProduct.get(productName),
                    entry.getValue()));
        }

        return result;
    }
}