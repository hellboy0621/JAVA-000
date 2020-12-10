package com.transformers.springcloud.hmily.order.service.impl;

import com.transformers.springcloud.hmily.order.dao.OrderMapper;
import com.transformers.springcloud.hmily.account.dto.AccountDTO;
import com.transformers.springcloud.hmily.inventory.dto.InventoryDTO;
import com.transformers.springcloud.hmily.order.entity.Order;
import com.transformers.springcloud.hmily.order.enums.OrderStatusEnum;
import com.transformers.springcloud.hmily.order.feign.AccountClient;
import com.transformers.springcloud.hmily.order.feign.InventoryClient;
import com.transformers.springcloud.hmily.order.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hmily.annotation.HmilyTCC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private AccountClient accountClient;

    @Autowired
    private InventoryClient inventoryClient;

    @Override
    @HmilyTCC(confirmMethod = "confirmOrderStatus", cancelMethod = "cacelOrderStatus")
    public void makePayment(Order order) {
        updateOrderStatus(order, OrderStatusEnum.PAYING);
        accountClient.payment(buildAccountDTO(order));
        inventoryClient.decrease(buildInventoryDTO(order));
    }

    @Override
    @HmilyTCC(confirmMethod = "confirmOrderStatus", cancelMethod = "cacelOrderStatus")
    public String mockPaymentInventoryWithTryException(Order order) {
        log.debug("===========执行springcloud  mockPaymentInventoryWithTryException 扣减资金接口==========");
        updateOrderStatus(order, OrderStatusEnum.PAYING);
        //扣除用户余额
        accountClient.payment(buildAccountDTO(order));
        inventoryClient.mockWithTryException(buildInventoryDTO(order));
        return "success";
    }

    public void confirmOrderStatus(Order order) {
        updateOrderStatus(order, OrderStatusEnum.PAY_SUCCESS);
    }

    public void cacelOrderStatus(Order order) {
        updateOrderStatus(order, OrderStatusEnum.PAY_FAIL);
    }

    private void updateOrderStatus(Order order, OrderStatusEnum orderStatus) {
        order.setStatus(orderStatus.getCode());
        orderMapper.update(order);
    }

    private AccountDTO buildAccountDTO(Order order) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAmount(order.getTotalAmount());
        accountDTO.setUserId(order.getUserId());
        return accountDTO;
    }

    private InventoryDTO buildInventoryDTO(Order order) {
        InventoryDTO inventoryDTO = new InventoryDTO();
        inventoryDTO.setCount(order.getCount());
        inventoryDTO.setProductId(order.getProductId());
        return inventoryDTO;
    }

}
