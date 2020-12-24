package com.transformers.consumer.service.impl;

import com.transformers.api.dto.AccountDTO;
import com.transformers.api.entity.Transaction;
import com.transformers.api.service.AccountService;
import com.transformers.api.service.TransactionService;
import com.transformers.consumer.dao.TransactionMapper;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class TransactionServiceImpl implements TransactionService {

    @DubboReference(version = "1.1.0")
    private AccountService accountService;

    @Autowired
    private AccountServiceImplB accountServiceImplB;

    @Autowired
    private TransactionMapper transactionMapper;

    @Override
    @Transactional
    public void trans(Transaction transaction) {
        // A 用1美元换了 B 7元人民币

        // A 冻结1美元
        // B 冻结7元人民币

        // A B 冻结清空
        // A 增加7元人民币
        // B 增加1美元
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAccountId(1L);
        accountDTO.setUsdAmount(1.0);
        accountDTO.setRmbAmount(7.0);
        accountService.exchangeUsdToRmb(accountDTO);

        // 怎么调用同一个接口下不同的实现类？
        accountDTO = new AccountDTO();
        accountDTO.setAccountId(2L);
        accountDTO.setUsdAmount(1.0);
        accountDTO.setRmbAmount(7.0);
        accountServiceImplB.exchangeRmbToUsd(accountDTO);

        long now = new Date().getTime();
        transaction.setCreateTime(now);
        transaction.setUpdateTime(now);
        insertTransaction(transaction);
    }

    @Override
    public int insertTransaction(Transaction transaction) {
        return transactionMapper.insert(transaction);
    }


}
