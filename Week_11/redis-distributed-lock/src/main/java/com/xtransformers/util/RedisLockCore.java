package com.xtransformers.util;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
public class RedisLockCore {

    /**
     * 加锁，基于 set 命令
     * 只有当 key 不存在时，才能获取到锁
     *
     * @param jedis    Jedis 客户端
     * @param key      key
     * @param value    value
     * @param timeout  超时时间
     * @param timeUnit 超时时间单位
     * @return 获取到锁返回 true，否则返回 false
     */
    public boolean lock(Jedis jedis, String key, String value, int timeout, TimeUnit timeUnit) {
        long seconds = timeUnit.toSeconds(timeout);
        String result = jedis.set(key, value, "NX", "EX", seconds);
        return "OK".equals(result);
    }

    /**
     * 基于 del 命令实现释放锁
     * 利用 Lua 脚本保证原子性
     * 先判断 根据 KEY 获取的值是否一致
     *
     * @param jedis Jedis 客户端
     * @param key   key
     * @param value value
     * @return 解锁成功返回 true，否则返回 false
     */
    public boolean unlock(Jedis jedis, String key, String value) {
        String luaScript = "if redis.call('get',KEYS[1]) == ARGV[1]" +
                "then return redis.call('del',KEYS[1])" +
                "else return 0 end";
        Object result = jedis.eval(luaScript, Collections.singletonList(key), Collections.singletonList(value));
        return Objects.equals(1L, result);
    }

}
