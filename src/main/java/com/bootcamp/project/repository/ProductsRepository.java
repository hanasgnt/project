package com.bootcamp.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bootcamp.project.entity.Products;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Long> {
    boolean existsByCategoryId(Long categoryId);

    boolean existsBySupplierId(Long supplierId);
}
