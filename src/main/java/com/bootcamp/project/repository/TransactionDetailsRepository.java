package com.bootcamp.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bootcamp.project.entity.TransactionDetails;

public interface TransactionDetailsRepository extends JpaRepository<TransactionDetails, Long> {
}