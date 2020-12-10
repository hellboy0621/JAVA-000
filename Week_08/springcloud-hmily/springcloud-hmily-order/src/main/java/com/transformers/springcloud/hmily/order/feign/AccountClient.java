package com.transformers.springcloud.hmily.order.feign;

import com.transformers.springcloud.hmily.account.dto.AccountDTO;
import org.dromara.hmily.annotation.Hmily;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "account-service")
public interface AccountClient {

    /**
     * 用户账户付款.
     *
     * @param accountDO 实体类
     * @return true 成功
     */
    @RequestMapping("/account-service/account/payment")
    // 该注解为hmily分布式事务接口标识，表示该接口参与hmily分布式事务
    @Hmily
    Boolean payment(@RequestBody AccountDTO accountDO);

}
