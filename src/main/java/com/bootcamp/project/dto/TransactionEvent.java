package com.bootcamp.project.dto;

import java.util.List;

import com.bootcamp.project.entity.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionEvent {

    private TransactionType type = TransactionType.OUT;
    private Long supplierId = null;
    private String customerName;
    private List<TransactionDetailRequest> details;
}