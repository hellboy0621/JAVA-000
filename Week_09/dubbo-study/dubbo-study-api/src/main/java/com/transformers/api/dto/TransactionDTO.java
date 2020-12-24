package com.transformers.api.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class TransactionDTO implements Serializable {
    private Long transactionId;
    private Long fromAccountId;
    private Long toAccountId;
    private String type;
    private Double amount;
}
