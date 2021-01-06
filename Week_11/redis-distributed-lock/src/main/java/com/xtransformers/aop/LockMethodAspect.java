package com.xtransformers.aop;

import com.xtransformers.annotation.RedisLock;
import com.xtransformers.util.RedisLockCore;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@Aspect
@Slf4j
public class LockMethodAspect {

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private RedisLockCore redisLockCore;

    @Around("@annotation(com.xtransformers.annotation.RedisLock)")
    public Object around(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        RedisLock redisLock = method.getAnnotation(RedisLock.class);
        String key = redisLock.key();
        String value = UUID.randomUUID().toString();
        int expire = redisLock.expire();
        TimeUnit timeUnit = redisLock.timeUnit();

        Jedis jedis = jedisPool.getResource();
        try {
            boolean isLock = redisLockCore.lock(jedis, key, value, expire, timeUnit);
            log.info("isLock : {}", isLock);
            if (!isLock) {
                log.error("get lock failed.");
                throw new RuntimeException("get lock failed.");
            }

            try {
                return joinPoint.proceed();
            } catch (Throwable throwable) {
                throw new RuntimeException("system error.", throwable);
            }
        } finally {
            log.info("unlock.");
            redisLockCore.unlock(jedis, key, value);
            jedis.close();
        }
    }

}
