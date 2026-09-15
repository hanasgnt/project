package com.bootcamp.project.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.bootcamp.project.dto.BestSellerResponse;
import com.bootcamp.project.dto.RevenueTrendResponse;
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

            transactions = fetchOutTransactions(startDate, endDate);
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

    public List<BestSellerResponse> getBestSellerReport(
            LocalDate startDate,
            LocalDate endDate) {

        List<Transactions> transactions = fetchOutTransactions(startDate, endDate);

        // key = productId, biar aman walaupun ada 2 produk nama sama
        Map<Long, BestSellerAccumulator> accumulatorByProductId = new LinkedHashMap<>();

        for (Transactions transaction : transactions) {

            for (TransactionDetails detail : transaction.getDetails()) {

                Long productId = detail.getProduct().getId();
                String productName = detail.getProduct().getProductName();

                BigDecimal total = detail.getUnitPrice()
                        .multiply(BigDecimal.valueOf(detail.getQuantity()));

                BestSellerAccumulator accumulator = accumulatorByProductId.computeIfAbsent(
                        productId,
                        id -> new BestSellerAccumulator(productId, productName));

                accumulator.quantitySold += detail.getQuantity();
                accumulator.revenue = accumulator.revenue.add(total);
            }
        }

        return accumulatorByProductId.values().stream()
                .map(accumulator -> new BestSellerResponse(
                        accumulator.productId,
                        accumulator.productName,
                        accumulator.quantitySold,
                        accumulator.revenue))
                .sorted(Comparator.comparing(
                        BestSellerResponse::getTotalQuantitySold).reversed())
                .collect(Collectors.toList());
    }

    public List<RevenueTrendResponse> getRevenueTrend(
            LocalDate startDate,
            LocalDate endDate,
            String groupBy) {

        List<Transactions> transactions = fetchOutTransactions(startDate, endDate);

        Map<String, RevenueTrendAccumulator> accumulatorByPeriod = new LinkedHashMap<>();

        for (Transactions transaction : transactions) {

            String period = resolvePeriodKey(transaction.getTransactionDate(), groupBy);

            BigDecimal transactionTotal = transaction.getDetails().stream()
                    .map(detail -> detail.getUnitPrice()
                            .multiply(BigDecimal.valueOf(detail.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            RevenueTrendAccumulator accumulator = accumulatorByPeriod.computeIfAbsent(
                    period,
                    key -> new RevenueTrendAccumulator(key));

            accumulator.totalRevenue = accumulator.totalRevenue.add(transactionTotal);
            accumulator.transactionCount += 1;
        }

        return accumulatorByPeriod.values().stream()
                .map(accumulator -> new RevenueTrendResponse(
                        accumulator.period,
                        accumulator.totalRevenue,
                        accumulator.transactionCount))
                // format period (yyyy-MM-dd / yyyy-MM / yyyy-Www) sortable langsung
                // secara alfabet, jadi cukup diurutin string biasa
                .sorted(Comparator.comparing(RevenueTrendResponse::getPeriod))
                .collect(Collectors.toList());
    }

    private List<Transactions> fetchOutTransactions(
            LocalDate startDate,
            LocalDate endDate) {

        LocalDateTime startDateTime = startDate != null
                ? startDate.atStartOfDay()
                : LocalDateTime.MIN;

        LocalDateTime endDateTime = endDate != null
                ? endDate.plusDays(1).atStartOfDay()
                : LocalDateTime.MAX;

        return transactionsRepository.findSalesReport(
                startDateTime,
                endDateTime);
    }

    private String resolvePeriodKey(LocalDateTime transactionDate, String groupBy) {

        LocalDate date = transactionDate.toLocalDate();

        String normalizedGroupBy = groupBy == null ? "day" : groupBy.toLowerCase();

        switch (normalizedGroupBy) {

            case "month":
                return date.format(DateTimeFormatter.ofPattern("yyyy-MM"));

            case "week":
                WeekFields weekFields = WeekFields.ISO;
                int week = date.get(weekFields.weekOfWeekBasedYear());
                int weekYear = date.get(weekFields.weekBasedYear());
                return String.format("%d-W%02d", weekYear, week);

            case "day":
            default:
                return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        }
    }

    private static class BestSellerAccumulator {

        private final Long productId;
        private final String productName;
        private Integer quantitySold = 0;
        private BigDecimal revenue = BigDecimal.ZERO;

        private BestSellerAccumulator(Long productId, String productName) {
            this.productId = productId;
            this.productName = productName;
        }
    }

    private static class RevenueTrendAccumulator {

        private final String period;
        private BigDecimal totalRevenue = BigDecimal.ZERO;
        private Integer transactionCount = 0;

        private RevenueTrendAccumulator(String period) {
            this.period = period;
        }
    }
}