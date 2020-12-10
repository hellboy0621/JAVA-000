package com.transformers.springcloud.hmily.order.service.impl;

import com.transformers.springcloud.hmily.order.dao.OrderMapper;
import com.transformers.springcloud.hmily.order.entity.Order;
import com.transformers.springcloud.hmily.order.enums.OrderStatusEnum;
import com.transformers.springcloud.hmily.order.service.OrderService;
import com.transformers.springcloud.hmily.order.service.PaymentService;
import com.transformers.springcloud.hmily.utils.IdWorkerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private PaymentService paymentService;

    @Override
    public String orderPay(Integer count, BigDecimal amount) {
        long start = System.currentTimeMillis();
        final Order order = saveOrder(count, amount);
        paymentService.makePayment(order);
        System.out.println("hmily-cloud分布式事务耗时：" + (System.currentTimeMillis() - start));
        return "success";
    }

    private Order saveOrder(Integer count, BigDecimal amount) {
        final Order order = new Order();
        order.setCreateTime(new Date());
        order.setNumber(String.valueOf(IdWorkerUtils.getInstance().createUUID()));
        //demo中的表里只有商品id为 1的数据
        order.setProductId("1");
        order.setStatus(OrderStatusEnum.NOT_PAY.getCode());
        order.setTotalAmount(amount);
        order.setCount(count);
        //demo中 表里面存的用户id为10000
        order.setUserId("10000");

        orderMapper.save(order);
        return order;
    }

    @Override
    public String mockInventoryWithTryException(Integer count, BigDecimal amount) {
        Order order = saveOrder(count, amount);
        return paymentService.mockPaymentInventoryWithTryException(order);
    }

}
