package com.bootcamp.project.dto;

import java.util.List;

import com.bootcamp.project.entity.TransactionType;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {

    @NotNull
    private TransactionType type;

    private Long supplierId;

    private String customerName;

    @NotEmpty
    @Valid
    private List<TransactionDetailRequest> details;
}