package com.xtransformers.redis.jedis.sentinel;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Set;

import static com.xtransformers.redis.Constants.*;
import static com.xtransformers.redis.jedis.sentinel.SentinelJedisConfig.*;

public class SentinelJedisUtil {

    private static JedisSentinelPool POOL = createSentinelPool();

    public static Jedis getJedis() {
        return POOL.getResource();
    }

    public static void close() {
        POOL.close();
    }

    private static JedisSentinelPool createSentinelPool() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(MAX_TOTAL);
        poolConfig.setMaxIdle(MAX_IDLE);
        poolConfig.setMaxWaitMillis(MAX_WAIT_MILLIS);
        poolConfig.setTestOnBorrow(TEST_ON_BORROW);
        poolConfig.setTestWhileIdle(TEST_WHILE_IDLE);
        poolConfig.setTestOnReturn(TEST_ON_RETURN);

        Set<String> sentinels = new HashSet<>();
        sentinels.add(new HostAndPort(REDIS_HOST, SENTINEL_PORT_26379).toString());
        sentinels.add(new HostAndPort(REDIS_HOST, SENTINEL_PORT_26380).toString());
        sentinels.add(new HostAndPort(REDIS_HOST, SENTINEL_PORT_26381).toString());

        return new JedisSentinelPool(SENTINEL_MASTER_NAME, sentinels, poolConfig, TIMEOUT, null);
    }

}
