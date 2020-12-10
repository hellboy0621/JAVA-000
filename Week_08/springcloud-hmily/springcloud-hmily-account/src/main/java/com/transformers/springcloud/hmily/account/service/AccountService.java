package com.transformers.springcloud.hmily.account.service;

import com.transformers.springcloud.hmily.account.dto.AccountDTO;

public interface AccountService {

    /**
     * 扣款支付.
     *
     * @param accountDTO 参数dto
     * @return true boolean
     */
    boolean payment(AccountDTO accountDTO);

}
