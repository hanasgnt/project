package com.bootcamp.project.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bootcamp.project.dto.SalesReportResponse;
import com.bootcamp.project.entity.TransactionDetails;
import com.bootcamp.project.entity.Transactions;
import com.bootcamp.project.repository.TransactionsRepository;

@Service
public class SalesReportService {

    private final TransactionsRepository transactionsRepository;

    public SalesReportService(
            TransactionsRepository transactionsRepository) {
        this.transactionsRepository = transactionsRepository;
    }

    public List<SalesReportResponse> getSalesReport(
            LocalDate startDate,
            LocalDate endDate) {

        List<Transactions> transactions;

        if (startDate == null && endDate == null) {

            transactions = transactionsRepository.findAll();

        } else {

            LocalDateTime startDateTime = startDate != null
                    ? startDate.atStartOfDay()
                    : LocalDateTime.MIN;

            LocalDateTime endDateTime = endDate != null
                    ? endDate.plusDays(1).atStartOfDay()
                    : LocalDateTime.MAX;

            transactions = transactionsRepository.findSalesReport(
                    startDateTime,
                    endDateTime);
        }

        List<SalesReportResponse> result = new ArrayList<>();

        for (Transactions transaction : transactions) {

            for (TransactionDetails detail : transaction.getDetails()) {

                BigDecimal unitPrice = detail.getUnitPrice();

                BigDecimal total = unitPrice
                        .multiply(BigDecimal.valueOf(detail.getQuantity()));

                SalesReportResponse response = new SalesReportResponse(
                        transaction.getId(),
                        transaction.getTransactionDate(),
                        transaction.getCustomerName(),
                        detail.getProduct().getProductName(),
                        detail.getQuantity(),
                        unitPrice,
                        total);

                result.add(response);
            }
        }

        return result;
    }
}