package com.transformers.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 获得和设置上下文环境 主要负责改变上下文数据源的名称
 */
@Component
@Slf4j
public class DataSourceContextHolder {

    /**
     * 线程独立
     */
    private static ThreadLocal<String> contextHolder = new ThreadLocal<String>();
    /**
     * 主库
     */
    public static final String DB_TEST_MASTER = "master";


    /**
     * 获取数据源名
     *
     * @return 数据库名
     */
    public static String getDataBaseType() {
        return contextHolder.get();
    }

    /**
     * 设置数据源名（切换数据源）
     *
     * @param dataBase 数据库类型
     */
    public static void setDataBaseType(String dataBase) {
        log.info("set dataSource ：{}", dataBase);
        contextHolder.set(dataBase);
    }

    /**
     * 清除数据源名
     */
    public static void clearDataBaseType() {
        contextHolder.remove();
    }

}
