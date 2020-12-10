package com.transformer.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class OrderDO implements Serializable {

    private Long orderId;

    private Long userId ;

    /**
     * 总优惠，以分为单位
     */
    private Long discount ;

    /**
     * 订单支付总价，以分为单位
     */
    private Long totalPrice;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Long createTime ;

    /**
     * 更新时间
     */
    private Long updateTime ;

}
