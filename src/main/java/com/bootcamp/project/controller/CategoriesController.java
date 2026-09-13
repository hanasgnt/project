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
import com.bootcamp.project.dto.CategoriesRequest;
import com.bootcamp.project.dto.CategoriesResponse;
import com.bootcamp.project.service.CategoriesService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Categories Management")
@RequestMapping("/categories")
public class CategoriesController {

    @Autowired
    private CategoriesService categoriesService;

    // =========================
    // CREATE
    // =========================
    @Operation(summary = "Create a new category")
    @PostMapping("/create")
    public ApiResponse<CategoriesResponse> createCategory(
            @Valid @RequestBody CategoriesRequest categoriesRequest) {

        CategoriesResponse createdCategory = categoriesService.createCategory(categoriesRequest);

        return ApiResponse.<CategoriesResponse>builder()
                .message("Category successfully created")
                .data(createdCategory)
                .build();
    }

    // =========================
    // GET ALL
    // =========================
    @Operation(summary = "Get all categories")
    @GetMapping
    public ApiResponse<List<CategoriesResponse>> getAllCategories() {

        List<CategoriesResponse> categories = categoriesService.getAllCategories();

        return ApiResponse.<List<CategoriesResponse>>builder()
                .message("Successfully fetched all categories")
                .data(categories)
                .build();
    }

    // =========================
    // GET BY ID
    // =========================
    @Operation(summary = "Get category by ID")
    @GetMapping("/{id}")
    public ApiResponse<CategoriesResponse> getCategoryById(
            @PathVariable Long id) {

        CategoriesResponse category = categoriesService.getCategoryById(id);

        return ApiResponse.<CategoriesResponse>builder()
                .message(
                        "Successfully fetched category with ID: " + id)
                .data(category)
                .build();
    }

    // =========================
    // UPDATE
    // =========================
    @Operation(summary = "Update category by ID")
    @PutMapping("/{id}")
    public ApiResponse<CategoriesResponse> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoriesRequest categoriesRequest) {

        CategoriesResponse updatedCategory = categoriesService.updateCategory(
                id,
                categoriesRequest);

        return ApiResponse.<CategoriesResponse>builder()
                .message("Category successfully updated")
                .data(updatedCategory)
                .build();
    }

    // =========================
    // DELETE
    // =========================
    @Operation(summary = "Delete category by ID")
    @DeleteMapping("/delete/{id}")
    public ApiResponse<String> deleteCategory(
            @PathVariable Long id) {

        categoriesService.deleteCategory(id);

        return ApiResponse.<String>builder()
                .message("Category successfully deleted")
                .data(null)
                .build();
    }
}
