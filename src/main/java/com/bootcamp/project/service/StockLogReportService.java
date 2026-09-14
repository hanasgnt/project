package com.bootcamp.project.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bootcamp.project.dto.StockLogReportResponse;
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

        List<StockLogReportResponse> result = new ArrayList<>();

        for (StockLogs stockLog : stockLogs) {

            result.add(new StockLogReportResponse(
                    stockLog.getId(),
                    stockLog.getProduct().getProductName(),
                    stockLog.getQuantity(),
                    stockLog.getType(),
                    stockLog.getCreatedAt()));
        }

        return result;
    }
}