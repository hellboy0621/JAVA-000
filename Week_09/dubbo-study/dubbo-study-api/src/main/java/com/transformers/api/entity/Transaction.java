package com.transformers.api.entity;

import lombok.Data;

@Data
public class Transaction {
    private Long transactionId;
    private Long fromAccountId;
    private Long toAccountId;
    private String type;
    private Double amount;
    private Long createTime;
    private Long updateTime;
}
