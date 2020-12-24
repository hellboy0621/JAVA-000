package com.transformers.consumer.controller;

import com.transformers.api.entity.Transaction;
import com.transformers.api.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/trans")
    public String trans() {
        Transaction transaction = new Transaction();
        transaction.setFromAccountId(1L);
        transaction.setToAccountId(2L);
        transaction.setAmount(1.0);
        transaction.setType("usd");
        transactionService.trans(transaction);
        return "success";
    }

}
