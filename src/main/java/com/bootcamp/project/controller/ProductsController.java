package com.bootcamp.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.project.dto.ApiResponse;
import com.bootcamp.project.dto.ProductsRequest;
import com.bootcamp.project.dto.ProductsResponse;
import com.bootcamp.project.service.ProductsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Products Management")
@RequestMapping("/products")
public class ProductsController {

        @Autowired
        private ProductsService productsService;

        // =========================
        // CREATE
        // =========================
        @Operation(summary = "Create a new product")
        @PostMapping("/create")
        public ApiResponse<ProductsResponse> createProduct(
                        @Valid @RequestBody ProductsRequest productsRequest) {

                ProductsResponse createdProduct = productsService.createProduct(productsRequest);

                return ApiResponse.<ProductsResponse>builder()
                                .message("Product successfully created")
                                .data(createdProduct)
                                .build();
        }

        // =========================
        // GET ALL
        // =========================
        @Operation(summary = "Get all products")
        @GetMapping
        public ApiResponse<List<ProductsResponse>> getAllProducts() {

                List<ProductsResponse> products = productsService.getAllProducts();

                return ApiResponse.<List<ProductsResponse>>builder()
                                .message("Successfully fetched all products")
                                .data(products)
                                .build();
        }

        // =========================
        // GET BY ID
        // =========================
        @Operation(summary = "Get product by ID")
        @GetMapping("/{id}")
        public ApiResponse<ProductsResponse> getProductById(
                        @PathVariable Long id) {

                ProductsResponse product = productsService.getProductById(id);

                return ApiResponse.<ProductsResponse>builder()
                                .message(
                                                "Successfully fetched product with ID: " + id)
                                .data(product)
                                .build();
        }

        // =========================
        // UPDATE
        // =========================
        @Operation(summary = "Update product by ID")
        @PutMapping("/{id}")
        public ApiResponse<ProductsResponse> updateProduct(
                        @PathVariable Long id,
                        @Valid @RequestBody ProductsRequest productsRequest) {

                ProductsResponse updatedProduct = productsService.updateProduct(
                                id,
                                productsRequest);

                return ApiResponse.<ProductsResponse>builder()
                                .message("Product successfully updated")
                                .data(updatedProduct)
                                .build();
        }

        // =========================
        // DELETE
        // =========================
        @Operation(summary = "Delete product by ID")
        @DeleteMapping("/delete/{id}")
        public ApiResponse<String> deleteProduct(
                        @PathVariable Long id) {

                String message = productsService.deleteProduct(id);

                return ApiResponse.<String>builder()
                                .message(message)
                                .data(null)
                                .build();
        }
}