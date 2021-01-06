package com.xtransformers.service;

import com.xtransformers.lock.RedisLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class StockServiceImpl {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    /**
     * 库存不足
     */
    public static final int LOW_STOCK = 0;

    /**
     * 不限库存
     */
    public static final long UNINITIALIZED_STOCK = -1L;

    /**
     * 执行扣库存的脚本
     */
    public static final String STOCK_LUA = "" +
            "if (redis.call('exists', KEYS[1]) == 1) then" +
            "   local stock = tonumber(redis.call('get', KEYS[1]));" +
            "   if (stock == -1) then " +
            "       return 1; " +
            "   end;" +
            "   if (stock > 0) then " +
            "       redis.call('incrby', KEYS[1], -1); " +
            "       return stock; " +
            "   end;" +
            "   return 0;" +
            "end;" +
            "return -1;";

    /**
     * 减库存
     *
     * @param key           库存key
     * @param expire        库存有效时间,单位秒
     * @param stockCallback 初始化库存回调函数
     * @return 0:库存不足; -1:库存未初始化; 大于0:扣减库存之前的剩余库存（扣减之前剩余的库存）
     */
    public long stock(String key, long expire, IStockCallback stockCallback) {
        long stock = stock(key);
        // 初始化库存
        if (stock == UNINITIALIZED_STOCK) {
            RedisLock redisLock = new RedisLock(redisTemplate, key);
            // 生成随机key
            String lockValue = UUID.randomUUID().toString();
            try {
                // 获取锁
                if (redisLock.tryLock(lockValue)) {
                    // 双重验证，避免并发时重复回源到数据库
                    stock = stock(key);
                    if (stock == UNINITIALIZED_STOCK) {
                        // 获取初始化库存
                        final int initStock = stockCallback.getStock();
                        // 将库存设置到redis
                        redisTemplate.opsForValue().set(key, initStock, expire, TimeUnit.SECONDS);
                        // 调一次扣库存的操作
                        stock = stock(key);
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                redisLock.unlock(key, lockValue);
            }

        }
        return stock;
    }

    /**
     * 获取库存
     *
     * @param key 库存key
     * @return 0:库存不足; -1:库存未初始化; 大于0:剩余库存
     */
    public int getStock(String key) {
        Integer stock = (Integer) redisTemplate.opsForValue().get(key);
        return stock == null ? -1 : stock;
    }

    /**
     * 使用 Lua 脚本，原子性减库存
     *
     * @param key 库存key
     * @return 扣减之前剩余的库存【0:库存不足; -1:库存未初始化; 大于0:扣减库存之前的剩余库存】
     */
    private Long stock(String key) {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(STOCK_LUA, Long.class);
        return redisTemplate.execute(redisScript, Collections.singletonList(key));
    }

}
