package com.transformers.api.service;

import com.transformers.api.entity.Transaction;

public interface TransactionService {

    void trans(Transaction transaction);

    int insertTransaction(Transaction transaction);

}
