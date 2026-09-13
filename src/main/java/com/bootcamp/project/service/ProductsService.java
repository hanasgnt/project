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

import com.bootcamp.project.dto.ProductsRequest;
import com.bootcamp.project.dto.ProductsResponse;
import com.bootcamp.project.entity.Categories;
import com.bootcamp.project.entity.Products;
import com.bootcamp.project.entity.Suppliers;
import com.bootcamp.project.repository.CategoriesRepository;
import com.bootcamp.project.repository.ProductsRepository;
import com.bootcamp.project.repository.SuppliersRepository;

import jakarta.transaction.Transactional;

@Service
public class ProductsService {

        private static final Logger logger = LoggerFactory.getLogger(ProductsService.class);

        @Autowired
        private ProductsRepository productRepository;

        @Autowired
        private CategoriesRepository categoriesRepository;

        @Autowired
        private SuppliersRepository suppliersRepository;

        // =========================
        // Mapper
        // =========================
        private ProductsResponse mapToResponse(Products product) {
                return ProductsResponse.builder()
                                .id(product.getId())
                                .productName(product.getProductName())

                                .supplierId(
                                                product.getSupplier() != null
                                                                ? product.getSupplier().getId()
                                                                : null)
                                .supplierName(
                                                product.getSupplier() != null
                                                                ? product.getSupplier().getCompanyName()
                                                                : null)

                                .categoryId(
                                                product.getCategory() != null
                                                                ? product.getCategory().getId()
                                                                : null)
                                .categoryName(
                                                product.getCategory() != null
                                                                ? product.getCategory().getName()
                                                                : null)

                                .quantityPerUnit(product.getQuantityPerUnit())
                                .unitPrice(product.getUnitPrice())
                                .unitsInStock(product.getUnitsInStock())
                                .unitsOnOrder(product.getUnitsOnOrder())
                                .reorderLevel(product.getReorderLevel())
                                .discontinued(product.getDiscontinued())

                                .createdAt(product.getCreatedAt())
                                .updatedAt(product.getUpdatedAt())
                                .build();
        }

        // =========================
        // CREATE
        // =========================
        @CacheEvict(value = "products", key = "'all'")
        @Transactional
        public ProductsResponse createProduct(ProductsRequest request) {
                logger.info(
                                "Starting to create product: {}",
                                request.getProductName());

                Categories category = null;
                Suppliers supplier = null;

                // =========================
                // Find Category
                // =========================
                if (request.getCategoryId() != null) {

                        category = categoriesRepository
                                        .findById(request.getCategoryId())
                                        .orElseThrow(() -> {

                                                logger.warn(
                                                                "Category with ID {} not found",
                                                                request.getCategoryId());

                                                return new ResponseStatusException(
                                                                HttpStatus.NOT_FOUND,
                                                                "Category not found");
                                        });
                }

                // =========================
                // Find Supplier
                // =========================
                if (request.getSupplierId() != null) {

                        supplier = suppliersRepository
                                        .findById(request.getSupplierId())
                                        .orElseThrow(() -> {

                                                logger.warn(
                                                                "Supplier with ID {} not found",
                                                                request.getSupplierId());

                                                return new ResponseStatusException(
                                                                HttpStatus.NOT_FOUND,
                                                                "Supplier not found");
                                        });
                }

                Products product = new Products();

                product.setProductName(request.getProductName());
                product.setCategory(category);
                product.setSupplier(supplier);
                product.setQuantityPerUnit(
                                request.getQuantityPerUnit());
                product.setUnitPrice(request.getUnitPrice());
                product.setUnitsInStock(request.getUnitsInStock());
                product.setUnitsOnOrder(request.getUnitsOnOrder());
                product.setReorderLevel(request.getReorderLevel());
                product.setDiscontinued(request.getDiscontinued());

                Products savedProduct = productRepository.save(product);

                logger.info(
                                "Product created successfully with ID: {}",
                                savedProduct.getId());

                return mapToResponse(savedProduct);
        }

        // =========================
        // GET ALL
        // =========================
        @Cacheable(value = "products", key = "'all'")
        public List<ProductsResponse> getAllProducts() {

                logger.info("Fetching all products");

                List<ProductsResponse> products = productRepository.findAll()
                                .stream()
                                .map(this::mapToResponse)
                                .toList();

                logger.info(
                                "Successfully fetched {} products",
                                products.size());

                return products;
        }

        // =========================
        // GET BY ID
        // =========================
        public ProductsResponse getProductById(Long id) {

                logger.info(
                                "Fetching product with ID: {}",
                                id);

                Products product = productRepository.findById(id)
                                .orElseThrow(() -> {

                                        logger.warn(
                                                        "Product with ID {} not found",
                                                        id);

                                        return new ResponseStatusException(
                                                        HttpStatus.NOT_FOUND,
                                                        "Product not found");
                                });

                return mapToResponse(product);
        }

        // =========================
        // UPDATE
        // =========================
        @CacheEvict(value = "products", key = "'all'")
        @Transactional
        public ProductsResponse updateProduct(Long id, ProductsRequest request) {
                logger.info(
                                "Starting update process for product ID: {}",
                                id);

                Products product = productRepository.findById(id)
                                .orElseThrow(() -> {

                                        logger.warn(
                                                        "Product with ID {} not found",
                                                        id);

                                        return new ResponseStatusException(
                                                        HttpStatus.NOT_FOUND,
                                                        "Product not found");
                                });

                Categories category = null;
                Suppliers supplier = null;

                // =========================
                // Update Category
                // =========================
                if (request.getCategoryId() != null) {

                        category = categoriesRepository
                                        .findById(request.getCategoryId())
                                        .orElseThrow(() -> {

                                                return new ResponseStatusException(
                                                                HttpStatus.NOT_FOUND,
                                                                "Category not found");
                                        });
                }

                // =========================
                // Update Supplier
                // =========================
                if (request.getSupplierId() != null) {

                        supplier = suppliersRepository
                                        .findById(request.getSupplierId())
                                        .orElseThrow(() -> {

                                                return new ResponseStatusException(
                                                                HttpStatus.NOT_FOUND,
                                                                "Supplier not found");
                                        });
                }

                product.setProductName(request.getProductName());
                product.setCategory(category);
                product.setSupplier(supplier);
                product.setQuantityPerUnit(
                                request.getQuantityPerUnit());
                product.setUnitPrice(request.getUnitPrice());
                product.setUnitsInStock(request.getUnitsInStock());
                product.setUnitsOnOrder(request.getUnitsOnOrder());
                product.setReorderLevel(request.getReorderLevel());
                product.setDiscontinued(request.getDiscontinued());

                Products updatedProduct = productRepository.save(product);

                logger.info(
                                "Product with ID {} updated successfully",
                                id);

                return mapToResponse(updatedProduct);
        }

        // =========================
        // DELETE
        // =========================
        @CacheEvict(value = "products", key = "'all'")
        @Transactional
        public void deleteProduct(Long id) {

                logger.info(
                                "Attempting to delete product with ID: {}",
                                id);

                if (!productRepository.existsById(id)) {

                        logger.warn(
                                        "Product with ID {} does not exist",
                                        id);

                        throw new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Product not found");
                }

                productRepository.deleteById(id);

                logger.info(
                                "Product with ID {} deleted successfully",
                                id);
        }
}