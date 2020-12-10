package com.transformers.springcloud.hmily.account.service.impl;

import com.transformers.springcloud.hmily.account.dao.AccountMapper;
import com.transformers.springcloud.hmily.account.dto.AccountDTO;
import com.transformers.springcloud.hmily.account.service.AccountService;
import org.dromara.hmily.annotation.HmilyTCC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountMapper accountMapper;

    @Override
    @HmilyTCC(confirmMethod = "confirmPayment", cancelMethod = "cancelPayment")
    public boolean payment(AccountDTO accountDTO) {
        accountMapper.update(accountDTO);
        return Boolean.TRUE;
    }

    public boolean confirmPayment(AccountDTO accountDTO) {
        return accountMapper.confirm(accountDTO) > 0;
    }

    public boolean cancelPayment(AccountDTO accountDTO) {
        return accountMapper.cancel(accountDTO) > 0;
    }

}
