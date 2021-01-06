package com.xtransformers.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RedisLock {

    /**
     * 分布式锁的键
     *
     * @return 分布式锁的键
     */
    String key();

    /**
     * 过期时间，默认5
     *
     * @return 过期时间
     */
    int expire() default 5;

    /**
     * 尝试加锁等待时间
     *
     * @return 等待时间
     */
    long waitTime() default Long.MAX_VALUE;

    /**
     * 时间单位，默认为秒
     *
     * @return 时间单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

}
