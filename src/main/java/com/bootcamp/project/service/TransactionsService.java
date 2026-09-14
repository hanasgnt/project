package com.bootcamp.project.service;

import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.bootcamp.project.dto.ProductsResponse;
import com.bootcamp.project.dto.TransactionDetailRequest;
import com.bootcamp.project.dto.TransactionEvent;
import com.bootcamp.project.dto.TransactionRequest;
import com.bootcamp.project.entity.TransactionType;

@Service
public class TransactionsService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionsService.class);

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private ProductsService productsService;

    public TransactionEvent createTransaction(TransactionRequest request) {

        logger.info("Preparing transaction event with type: {}", request.getType());

        // Pre-check stok dari Redis cache (bukan DB) sebelum kirim ke Kafka.
        // Ini cuma validasi awal - validasi final tetap di consumer (sumber
        // kebenaran datanya DB, bukan cache).
        if (request.getType() == TransactionType.OUT) {
            validateStockAvailability(request);
        }

        TransactionEvent event = new TransactionEvent(
                request.getType(),
                request.getSupplierId(),
                request.getCustomerName(),
                request.getDetails());

        kafkaProducerService.sendTransaction(event);

        logger.info("Transaction event successfully sent to Kafka");

        return event;
    }

    private void validateStockAvailability(TransactionRequest request) {

        // getAllProducts() sudah @Cacheable -> ini baca dari Redis, bukan DB
        Map<Long, ProductsResponse> productsById = productsService.getAllProducts()
                .stream()
                .collect(java.util.stream.Collectors.toMap(
                        ProductsResponse::getId,
                        Function.identity()));

        for (TransactionDetailRequest detail : request.getDetails()) {

            ProductsResponse product = productsById.get(detail.getProductId());

            if (product == null) {
                logger.warn(
                        "Validation failed. Product ID {} not found in cache",
                        detail.getProductId());

                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Product not found: " + detail.getProductId());
            }

            int availableStock = product.getUnitsInStock() == null
                    ? 0
                    : product.getUnitsInStock();

            if (detail.getQuantity() > availableStock) {

                logger.warn(
                        "Insufficient stock for product '{}' (ID: {}). Available: {}, Requested: {}",
                        product.getProductName(),
                        product.getId(),
                        availableStock,
                        detail.getQuantity());

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        String.format(
                                "Stok tidak cukup untuk produk '%s'. Tersedia: %d, diminta: %d. Silakan masukkan jumlah yang sesuai.",
                                product.getProductName(),
                                availableStock,
                                detail.getQuantity()));
            }
        }
    }
}