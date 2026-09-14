package com.bootcamp.project.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.bootcamp.project.dto.SuppliersRequest;
import com.bootcamp.project.dto.SuppliersResponse;
import com.bootcamp.project.entity.Suppliers;
import com.bootcamp.project.repository.ProductsRepository;
import com.bootcamp.project.repository.SuppliersRepository;
import com.bootcamp.project.repository.TransactionsRepository;

import jakarta.transaction.Transactional;

@Service
public class SuppliersService {

        private static final Logger logger = LoggerFactory.getLogger(SuppliersService.class);

        @Autowired
        private SuppliersRepository suppliersRepository;

        @Autowired
        private ProductsRepository productRepository;

        @Autowired
        private TransactionsRepository transactionsRepository;

        // =========================
        // Mapper
        // =========================
        private SuppliersResponse mapToResponse(Suppliers supplier) {

                return SuppliersResponse.builder()
                                .id(supplier.getId())
                                .companyName(supplier.getCompanyName())
                                .contactName(supplier.getContactName())
                                .contactTitle(supplier.getContactTitle())
                                .address(supplier.getAddress())
                                .city(supplier.getCity())
                                .region(supplier.getRegion())
                                .postalCode(supplier.getPostalCode())
                                .country(supplier.getCountry())
                                .phone(supplier.getPhone())
                                .fax(supplier.getFax())
                                .homePage(supplier.getHomePage())
                                .createdAt(supplier.getCreatedAt())
                                .updatedAt(supplier.getUpdatedAt())
                                .build();
        }

        // =========================
        // CREATE
        // =========================
        @Transactional
        public SuppliersResponse createSupplier(
                        SuppliersRequest request) {

                logger.info(
                                "Starting to create supplier: {}",
                                request.getCompanyName());

                if (suppliersRepository.existsByCompanyNameIgnoreCase(
                                request.getCompanyName())) {

                        throw new ResponseStatusException(
                                        HttpStatus.CONFLICT,
                                        "Supplier company name already exists");
                }

                Suppliers supplier = new Suppliers();

                supplier.setCompanyName(request.getCompanyName());
                supplier.setContactName(request.getContactName());
                supplier.setContactTitle(request.getContactTitle());
                supplier.setAddress(request.getAddress());
                supplier.setCity(request.getCity());
                supplier.setRegion(request.getRegion());
                supplier.setPostalCode(request.getPostalCode());
                supplier.setCountry(request.getCountry());
                supplier.setPhone(request.getPhone());
                supplier.setFax(request.getFax());
                supplier.setHomePage(request.getHomePage());

                Suppliers savedSupplier = suppliersRepository.save(supplier);

                logger.info(
                                "Supplier created successfully with ID: {}",
                                savedSupplier.getId());

                return mapToResponse(savedSupplier);
        }

        // =========================
        // GET ALL
        // =========================
        public List<SuppliersResponse> getAllSuppliers() {

                logger.info("Fetching all suppliers");

                List<SuppliersResponse> suppliers = suppliersRepository.findAll()
                                .stream()
                                .map(this::mapToResponse)
                                .toList();

                logger.info(
                                "Successfully fetched {} suppliers",
                                suppliers.size());

                return suppliers;
        }

        // =========================
        // GET BY ID
        // =========================
        public SuppliersResponse getSupplierById(Long id) {

                logger.info(
                                "Fetching supplier with ID: {}",
                                id);

                Suppliers supplier = suppliersRepository.findById(id)
                                .orElseThrow(() -> {

                                        logger.warn(
                                                        "Supplier with ID {} not found",
                                                        id);

                                        return new ResponseStatusException(
                                                        HttpStatus.NOT_FOUND,
                                                        "Supplier not found");
                                });

                return mapToResponse(supplier);
        }

        // =========================
        // UPDATE
        // =========================
        @Transactional
        public SuppliersResponse updateSupplier(
                        Long id,
                        SuppliersRequest request) {

                logger.info(
                                "Starting update process for supplier ID: {}",
                                id);

                Suppliers supplier = suppliersRepository.findById(id)
                                .orElseThrow(() -> {

                                        logger.warn(
                                                        "Supplier with ID {} not found",
                                                        id);

                                        return new ResponseStatusException(
                                                        HttpStatus.NOT_FOUND,
                                                        "Supplier not found");
                                });

                if (suppliersRepository
                                .existsByCompanyNameIgnoreCaseAndIdNot(
                                                request.getCompanyName(), id)) {

                        throw new ResponseStatusException(
                                        HttpStatus.CONFLICT,
                                        "Supplier company name already exists");
                }

                supplier.setCompanyName(request.getCompanyName());
                supplier.setContactName(request.getContactName());
                supplier.setContactTitle(request.getContactTitle());
                supplier.setAddress(request.getAddress());
                supplier.setCity(request.getCity());
                supplier.setRegion(request.getRegion());
                supplier.setPostalCode(request.getPostalCode());
                supplier.setCountry(request.getCountry());
                supplier.setPhone(request.getPhone());
                supplier.setFax(request.getFax());
                supplier.setHomePage(request.getHomePage());

                Suppliers updatedSupplier = suppliersRepository.save(supplier);

                logger.info(
                                "Supplier with ID {} updated successfully",
                                id);

                return mapToResponse(updatedSupplier);
        }

        // =========================
        // DELETE
        // =========================
        @Transactional
        public String deleteSupplier(Long id) {

                logger.info(
                                "Attempting to delete supplier with ID: {}",
                                id);

                if (!suppliersRepository.existsById(id)) {

                        throw new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Supplier not found");
                }

                boolean isUsedByProduct = productRepository.existsBySupplierId(id);

                boolean isUsedByTransaction = transactionsRepository.existsBySupplierId(id);

                if (isUsedByProduct || isUsedByTransaction) {

                        Suppliers supplier = suppliersRepository.findById(id)
                                        .orElseThrow();

                        supplier.setDiscontinued(true);
                        suppliersRepository.save(supplier);

                        logger.info(
                                        "Supplier with ID {} marked as discontinued",
                                        id);

                        return "Supplier discontinued because it is still referenced by products or transactions";
                }

                suppliersRepository.deleteById(id);

                logger.info(
                                "Supplier with ID {} deleted successfully",
                                id);

                return "Supplier successfully deleted";
        }
}