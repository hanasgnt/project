package com.bootcamp.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.project.dto.ApiResponse;
import com.bootcamp.project.dto.TransactionEvent;
import com.bootcamp.project.dto.TransactionRequest;
import com.bootcamp.project.service.TransactionsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Transactions Management")
@RequestMapping("/transactions")
public class TransactionsController {

    @Autowired
    private TransactionsService transactionsService;

    @Operation(summary = "Create a new transaction")
    @PostMapping
    public ApiResponse<TransactionEvent> createTransaction(
            @Valid @RequestBody TransactionRequest request) {

        TransactionEvent event = transactionsService.createTransaction(request);

        return ApiResponse.<TransactionEvent>builder()
                .message("Transaction successfully sent to Kafka")
                .data(event)
                .build();
    }
}