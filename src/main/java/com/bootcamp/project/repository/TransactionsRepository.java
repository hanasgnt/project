package com.bootcamp.project.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bootcamp.project.entity.Transactions;

public interface TransactionsRepository extends JpaRepository<Transactions, Long> {

    @Query("""
                SELECT t
                FROM Transactions t
                WHERE t.type = com.bootcamp.project.entity.TransactionType.OUT
                AND t.transactionDate >= :startDate
                AND t.transactionDate < :endDate
                ORDER BY t.transactionDate DESC
            """)
    List<Transactions> findSalesReport(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("""
            SELECT COUNT(t) > 0
            FROM Transactions t
            WHERE t.supplier.id = :supplierId
            """)
    boolean existsBySupplierId(@Param("supplierId") Long supplierId);
}