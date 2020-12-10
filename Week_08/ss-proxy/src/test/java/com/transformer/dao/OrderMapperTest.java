package com.transformer.dao;

import com.transformer.AbstractSpringIntegrationTest;
import com.transformer.entity.OrderDO;
import org.junit.Test;

import javax.annotation.Resource;

import static org.junit.Assert.assertEquals;

public class OrderMapperTest extends AbstractSpringIntegrationTest {

    @Resource
    private OrderMapper orderMapper;

    @Test
    public void selectById() {
        OrderDO orderDO = buildOrderDO();
        int insert = orderMapper.insert(orderDO);
        assertEquals(1, insert);

        OrderDO orderDO1 = orderMapper.selectById(orderDO.getOrderId());
        assertEquals(orderDO, orderDO1);

        int delete = orderMapper.delete(2L);
        assertEquals(1, delete);
    }

    @Test
    public void insert() {
        OrderDO orderDO = buildOrderDO();
        int insert = orderMapper.insert(orderDO);
        assertEquals(1, insert);

        int delete = orderMapper.delete(2L);
        assertEquals(1, delete);
    }

    @Test
    public void delete() {
        OrderDO orderDO = buildOrderDO();
        int insert = orderMapper.insert(orderDO);
        assertEquals(1, insert);

        int delete = orderMapper.delete(2L);
        assertEquals(1, delete);
    }

    @Test
    public void update() {
        OrderDO orderDO = buildOrderDO();
        int insert = orderMapper.insert(orderDO);
        assertEquals(1, insert);

        orderDO.setRemark("test-update");
        orderDO.setUpdateTime(System.currentTimeMillis());
        int update = orderMapper.update(orderDO);
        assertEquals(1, update);

        OrderDO orderDO1 = orderMapper.selectById(2L);
        assertEquals(orderDO, orderDO1);
        assertEquals("test-update", orderDO1.getRemark());

        int delete = orderMapper.delete(2L);
        assertEquals(1, delete);
    }

    private OrderDO buildOrderDO() {
        OrderDO orderDO = new OrderDO();
        orderDO.setOrderId(2L);
        orderDO.setUserId(2L);
        orderDO.setDiscount(1099L);
        orderDO.setTotalPrice(1099L);
        orderDO.setRemark("remark2");
        orderDO.setCreateTime(System.currentTimeMillis());
        orderDO.setUpdateTime(System.currentTimeMillis());
        return orderDO;
    }
}