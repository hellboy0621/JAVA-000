package com.transformers.springcloud.hmily.account.controller;

import com.transformers.springcloud.hmily.account.dto.AccountDTO;
import com.transformers.springcloud.hmily.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @RequestMapping("/payment")
    public Boolean payment(@RequestBody AccountDTO accountDO) {
        return accountService.payment(accountDO);
    }

}
