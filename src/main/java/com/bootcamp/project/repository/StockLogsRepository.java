package com.bootcamp.project.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bootcamp.project.entity.StockLogs;

public interface StockLogsRepository extends JpaRepository<StockLogs, Long> {

    @Query("""
            SELECT s
            FROM StockLogs s
            WHERE s.createdAt >= :startDate
            AND s.createdAt < :endDate
            ORDER BY s.createdAt ASC
            """)
    List<StockLogs> findStockLogReport(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(s) > 0 FROM StockLogs s WHERE s.product.id = :productId")
    boolean existsByProductId(@Param("productId") Long productId);
}