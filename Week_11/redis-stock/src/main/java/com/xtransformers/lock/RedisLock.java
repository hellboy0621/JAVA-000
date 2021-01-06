package com.xtransformers.lock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RedisLock {

    private RedisTemplate<Object, Object> redisTemplate;

    /**
     * 锁标志对应的key
     */
    private String lockKey;

    /**
     * 默认请求锁的超时时间(ms 毫秒)
     */
    private static final long TIME_OUT = 100;

    /**
     * 默认锁的有效时间(s)
     */
    public static final int EXPIRE = 60;

    /**
     * 锁标记
     */
    private volatile boolean locked = false;

    final Random random = new Random();

    public RedisLock(RedisTemplate<Object, Object> redisTemplate, String lockKey) {
        this.redisTemplate = redisTemplate;
        this.lockKey = lockKey + "_lock";
    }

    public boolean tryLock(String lockValue) {
        // 请求锁超时时间，纳秒
        long timeout = TIME_OUT * 1000000;
        // 系统当前时间，纳秒
        long nowTime = System.nanoTime();
        while ((System.nanoTime() - nowTime) < timeout) {
            if (Objects.equals(Boolean.TRUE, set(lockKey, lockValue, EXPIRE))) {
                locked = true;
                // 上锁成功结束请求
                return true;
            }

            // 每次请求等待一段时间
            seleep(10, 50000);
        }
        return locked;
    }

    private Boolean set(final String key, final String value, final long seconds) {
        Assert.isTrue(!StringUtils.isEmpty(key), "key不能为空");
        return redisTemplate.opsForValue().setIfAbsent(key, value, seconds, TimeUnit.SECONDS);
    }

    private void seleep(long millis, int nanos) {
        try {
            Thread.sleep(millis, random.nextInt(nanos));
        } catch (InterruptedException e) {
            log.info("获取分布式锁休眠被中断：", e);
        }
    }

    /**
     * 释放锁
     *
     * @param key   锁key
     * @param value 锁value
     * @return 释放锁成功返回 true，否则返回false
     */
    public Boolean unlock(String key, String value) {
        // 只有加锁成功并且锁还有效才去释放锁
        if (locked) {
            String luaScript = "" +
                    "if redis.call('get',KEYS[1]) == ARGV[1] then" +
                    "   return redis.call('del',KEYS[1])" +
                    "else" +
                    "   return 0" +
                    "end";
            // 指定 lua 脚本，并且指定返回值类型
            DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(luaScript, Long.class);
            // 参数一：redisScript，参数二：key列表，参数三：arg（可多个）
            Long result = redisTemplate.execute(redisScript, Collections.singletonList(key), Collections.singletonList(value));
            return Objects.equals(1L, result);
        }
        return true;
    }

}
