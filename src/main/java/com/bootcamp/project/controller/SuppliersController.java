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
import com.bootcamp.project.dto.SuppliersRequest;
import com.bootcamp.project.dto.SuppliersResponse;
import com.bootcamp.project.service.SuppliersService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Suppliers Management")
@RequestMapping("/suppliers")
public class SuppliersController {

    @Autowired
    private SuppliersService suppliersService;

    // =========================
    // CREATE
    // =========================
    @Operation(summary = "Create a new supplier")
    @PostMapping("/create")
    public ApiResponse<SuppliersResponse> createSupplier(
            @Valid @RequestBody SuppliersRequest suppliersRequest) {

        SuppliersResponse createdSupplier = suppliersService.createSupplier(suppliersRequest);

        return ApiResponse.<SuppliersResponse>builder()
                .message("Supplier successfully created")
                .data(createdSupplier)
                .build();
    }

    // =========================
    // GET ALL
    // =========================
    @Operation(summary = "Get all suppliers")
    @GetMapping
    public ApiResponse<List<SuppliersResponse>> getAllSuppliers() {

        List<SuppliersResponse> suppliers = suppliersService.getAllSuppliers();

        return ApiResponse.<List<SuppliersResponse>>builder()
                .message("Successfully fetched all suppliers")
                .data(suppliers)
                .build();
    }

    // =========================
    // GET BY ID
    // =========================
    @Operation(summary = "Get supplier by ID")
    @GetMapping("/{id}")
    public ApiResponse<SuppliersResponse> getSupplierById(
            @PathVariable Long id) {

        SuppliersResponse supplier = suppliersService.getSupplierById(id);

        return ApiResponse.<SuppliersResponse>builder()
                .message(
                        "Successfully fetched supplier with ID: " + id)
                .data(supplier)
                .build();
    }

    // =========================
    // UPDATE
    // =========================
    @Operation(summary = "Update supplier by ID")
    @PutMapping("/{id}")
    public ApiResponse<SuppliersResponse> updateSupplier(
            @PathVariable Long id,
            @Valid @RequestBody SuppliersRequest suppliersRequest) {

        SuppliersResponse updatedSupplier = suppliersService.updateSupplier(
                id,
                suppliersRequest);

        return ApiResponse.<SuppliersResponse>builder()
                .message("Supplier successfully updated")
                .data(updatedSupplier)
                .build();
    }

    // =========================
    // DELETE
    // =========================
    @Operation(summary = "Delete supplier by ID")
    @DeleteMapping("/delete/{id}")
    public ApiResponse<String> deleteSupplier(
            @PathVariable Long id) {

        suppliersService.deleteSupplier(id);

        return ApiResponse.<String>builder()
                .message("Supplier successfully deleted")
                .data(null)
                .build();
    }
}