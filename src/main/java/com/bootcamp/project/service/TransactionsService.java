package com.bootcamp.project.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bootcamp.project.dto.TransactionEvent;
import com.bootcamp.project.dto.TransactionRequest;

@Service
public class TransactionsService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionsService.class);

    @Autowired
    private KafkaProducerService kafkaProducerService;

    public TransactionEvent createTransaction(TransactionRequest request) {

        logger.info("Preparing transaction event with type: {}", request.getType());

        TransactionEvent event = new TransactionEvent(
                request.getType(),
                request.getSupplierId(),
                request.getCustomerName(),
                request.getDetails());

        kafkaProducerService.sendTransaction(event);

        logger.info("Transaction event successfully sent to Kafka");

        return event;
    }
}