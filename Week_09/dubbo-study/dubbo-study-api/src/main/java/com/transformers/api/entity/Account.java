package com.transformers.api.entity;

import lombok.Data;

@Data
public class Account {
    private Long accountId;
    private Long userId;
    private Double usdBalance;
    private Double rmbBalance;
    private Double usdFreezeAmount;
    private Double rmbFreezeAmount;
    private Long createTime;
    private Long updateTime;
}
