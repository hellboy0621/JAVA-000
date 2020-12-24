package com.transformers.consumer.service.impl;

import com.transformers.api.dto.AccountDTO;
import com.transformers.api.service.AccountService;
import com.transformers.consumer.dao.AccountMapper;
import org.dromara.hmily.annotation.HmilyTCC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImplB implements AccountService {

    @Autowired
    private AccountMapper accountMapper;

    @Override
    @HmilyTCC(confirmMethod = "confirmExchangeUsdToRmb", cancelMethod = "cancelExchangeUsdToRmb")
    public int exchangeUsdToRmb(AccountDTO accountDTO) {
        return accountMapper.freezeUsd(accountDTO);
    }

    public int confirmExchangeUsdToRmb(AccountDTO accountDTO) {
        return accountMapper.confirmUsdToRmb(accountDTO);
    }

    public int cancelExchangeUsdToRmb(AccountDTO accountDTO) {
        return accountMapper.cancelUsdToRmb(accountDTO);
    }

    @Override
    @HmilyTCC(confirmMethod = "confirmExchangeRmbToUsd", cancelMethod = "cancelExchangeRmbToUsd")
    public int exchangeRmbToUsd(AccountDTO accountDTO) {
        return accountMapper.freezeRmb(accountDTO);
    }

    public int confirmExchangeRmbToUsd(AccountDTO accountDTO) {
        return accountMapper.confirmRmbToUsd(accountDTO);
    }

    public int cancelExchangeRmbToUsd(AccountDTO accountDTO) {
        return accountMapper.cancelRmbToUsd(accountDTO);
    }

}
