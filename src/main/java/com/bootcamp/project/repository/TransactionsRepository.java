package com.bootcamp.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bootcamp.project.entity.Transactions;

public interface TransactionsRepository extends JpaRepository<Transactions, Long> {
}