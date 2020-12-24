package com.transformers.api.service;

import com.transformers.api.dto.AccountDTO;
import org.dromara.hmily.annotation.Hmily;

public interface AccountService {

    @Hmily
    int exchangeUsdToRmb(AccountDTO accountDTO);

    @Hmily
    int exchangeRmbToUsd(AccountDTO accountDTO);
}
