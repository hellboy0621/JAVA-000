package com.transformers.config;

import com.alibaba.druid.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

@Aspect
@Component
@Order(-1)
@Slf4j
public class DynamicDataSourceAspect {

    @Before("@annotation(DataSource)")
    public void beforeSwitchDataSource(JoinPoint point) {
        //获得当前访问的class
        Class<?> className = point.getTarget().getClass();
        //获得访问的方法名
        String methodName = point.getSignature().getName();
        //得到方法的参数的类型
        Class[] argClass = ((MethodSignature) point.getSignature()).getParameterTypes();
        String dataSource = DataSourceContextHolder.DB_TEST_MASTER;
        try {
            // 得到访问的方法对象
            Method method = className.getMethod(methodName, argClass);
            // 判断是否存在@DataSource注解
            if (method.isAnnotationPresent(DataSource.class)) {
                DataSource annotation = method.getAnnotation(DataSource.class);
                // 取出注解中的数据源名
                dataSource = annotation.value();
            }
        } catch (Exception e) {
            log.error("动态切换数据源失败");
        }
        // 切换数据源
        if (StringUtils.equals(DataSourceContextHolder.DB_TEST_MASTER, dataSource)) {
            DataSourceContextHolder.setDataBaseType(dataSource);
        } else {
            DataSourceContextHolder.setDataBaseType(roundRobinSlaveKey());
        }
    }

    private static AtomicInteger counter = new AtomicInteger(-1);

    private static String roundRobinSlaveKey() {
        //先获取再增加
        int index = counter.incrementAndGet() % 2;
        if (counter.get() > 9999) {
            counter.set(-1);
        }
        if (index == 0) {
            return DataSourceConfig.DB_TEST_SLAVE;
        } else {
            return DataSourceConfig.DB_TEST_SLAVE2;
        }
    }

    @After("@annotation(DataSource)")
    public void afterSwitchDataSource(JoinPoint point) {
        DataSourceContextHolder.clearDataBaseType();
    }
}
