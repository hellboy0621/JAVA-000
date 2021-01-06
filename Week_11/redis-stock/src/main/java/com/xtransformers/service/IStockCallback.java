package com.xtransformers.service;

/**
 * 初始化库存回调
 */
public interface IStockCallback {

    /**
     * 获得初始化库存
     *
     * @return 初始化库存量
     */
    int getStock();

}
