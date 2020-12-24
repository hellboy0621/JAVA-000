package com.transformers.api.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AccountDTO implements Serializable {
    private Long accountId;
    private Double usdAmount;
    private Double rmbAmount;
}
