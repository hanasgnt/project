package com.bootcamp.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bootcamp.project.entity.Products;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Long> {
    boolean existsByProductName(String productName);

    boolean existsByCategoryId(Long categoryId);

    boolean existsBySupplierId(Long supplierId);

    List<Products> findByDiscontinuedFalse();
}
