package com.transformers.springcloud.hmily.order.service;

import com.transformers.springcloud.hmily.order.entity.Order;

public interface PaymentService {

    void makePayment(Order order);

    /**
     * mock订单支付的时候库存异常.
     *
     * @param order 订单实体
     * @return String string
     */
    String mockPaymentInventoryWithTryException(Order order);
}
