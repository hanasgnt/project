package com.bootcamp.project.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.bootcamp.project.dto.CategoriesRequest;
import com.bootcamp.project.dto.CategoriesResponse;
import com.bootcamp.project.entity.Categories;
import com.bootcamp.project.repository.CategoriesRepository;
import com.bootcamp.project.repository.ProductsRepository;

import jakarta.transaction.Transactional;

@Service
public class CategoriesService {

        private static final Logger logger = LoggerFactory.getLogger(CategoriesService.class);

        @Autowired
        private CategoriesRepository categoriesRepository;

        @Autowired
        private ProductsRepository productRepository;

        // =========================
        // Mapper
        // =========================
        private CategoriesResponse mapToResponse(Categories category) {
                return CategoriesResponse.builder()
                                .id(category.getId())
                                .name(category.getName())
                                .description(category.getDescription())
                                .createdAt(category.getCreatedAt())
                                .updatedAt(category.getUpdatedAt())
                                .build();
        }

        // =========================
        // CREATE
        // =========================
        @CacheEvict(value = "categories", key = "'all'")
        @Transactional
        public CategoriesResponse createCategory(CategoriesRequest request) {

                logger.info("Starting to create category with name: {}", request.getName());

                // Cek duplicate name
                if (categoriesRepository.existsByNameIgnoreCase(request.getName())) {
                        logger.warn("Category creation failed: name '{}' already exists",
                                        request.getName());

                        throw new ResponseStatusException(
                                        HttpStatus.CONFLICT,
                                        "Category name already exists");
                }

                Categories category = new Categories();
                category.setName(request.getName());
                category.setDescription(request.getDescription());

                Categories savedCategory = categoriesRepository.save(category);

                logger.info(
                                "Category created successfully with ID: {}",
                                savedCategory.getId());

                return mapToResponse(savedCategory);
        }

        // =========================
        // GET ALL
        // =========================
        @Cacheable(value = "categories", key = "'all'")
        public List<CategoriesResponse> getAllCategories() {

                logger.info("Fetching all categories");

                List<CategoriesResponse> categories = categoriesRepository.findAll()
                                .stream()
                                .map(this::mapToResponse)
                                .toList();

                logger.info(
                                "Successfully fetched {} categories",
                                categories.size());

                return categories;
        }

        // =========================
        // GET BY ID
        // =========================
        public CategoriesResponse getCategoryById(Long id) {

                logger.info("Fetching category with ID: {}", id);

                Categories category = categoriesRepository.findById(id)
                                .orElseThrow(() -> {

                                        logger.warn(
                                                        "Fetch failed: Category with ID {} not found",
                                                        id);

                                        return new ResponseStatusException(
                                                        HttpStatus.NOT_FOUND,
                                                        "Category not found");
                                });

                return mapToResponse(category);
        }

        // =========================
        // UPDATE
        // =========================
        @CacheEvict(value = "categories", key = "'all'")
        @Transactional
        public CategoriesResponse updateCategory(
                        Long id,
                        CategoriesRequest request) {

                logger.info(
                                "Starting update process for category ID: {}",
                                id);

                Categories category = categoriesRepository.findById(id)
                                .orElseThrow(() -> {

                                        logger.warn(
                                                        "Update failed: Category with ID {} not found",
                                                        id);

                                        return new ResponseStatusException(
                                                        HttpStatus.NOT_FOUND,
                                                        "Category not found");
                                });

                // Cek apakah nama dipakai category lain
                if (categoriesRepository.existsByNameIgnoreCaseAndIdNot(
                                request.getName(), id)) {

                        throw new ResponseStatusException(
                                        HttpStatus.CONFLICT,
                                        "Category name already exists");
                }

                category.setName(request.getName());
                category.setDescription(request.getDescription());

                Categories updatedCategory = categoriesRepository.save(category);

                logger.info(
                                "Category with ID {} updated successfully",
                                id);

                return mapToResponse(updatedCategory);
        }

        // =========================
        // DELETE
        // =========================
        @CacheEvict(value = "categories", key = "'all'")
        @Transactional
        public void deleteCategory(Long id) {

                logger.info(
                                "Attempting to delete category with ID: {}",
                                id);

                if (!categoriesRepository.existsById(id)) {

                        logger.warn(
                                        "Deletion failed: Category with ID {} does not exist",
                                        id);

                        throw new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Category not found");
                }

                // Jangan hapus jika masih dipakai product
                if (productRepository.existsByCategoryId(id)) {

                        logger.warn(
                                        "Deletion failed: Category with ID {} is still used by products",
                                        id);

                        throw new ResponseStatusException(
                                        HttpStatus.CONFLICT,
                                        "Category cannot be deleted because it is still used by products");
                }

                categoriesRepository.deleteById(id);

                logger.info(
                                "Category with ID {} deleted successfully",
                                id);
        }

        // ========================
        // GET ALL
        // ========================

}