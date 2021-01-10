package com.xtransformers.redis.jedis.sentinel;

public interface SentinelJedisConfig {

    /**
     * 可用连接实例的最大数目，默认为8；
     * 如果赋值为-1，则表示不限制，如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)
     */
    Integer MAX_TOTAL = 16;

    /**
     * 控制一个pool最多有多少个状态为idle(空闲)的jedis实例，默认值是8
     */
    Integer MAX_IDLE = 12;

    /**
     * 等待可用连接的最大时间，单位是毫秒，默认值为-1，表示永不超时。
     * 如果超过等待时间，则直接抛出JedisConnectionException
     */
    Integer MAX_WAIT_MILLIS = 10000;

    /**
     * 客户端超时时间配置
     */
    Integer TIMEOUT = 10000;

    /**
     * 在 borrow 一个 jedis 实例时，是否提前进行 validate 操作
     * 如果为true，则得到的jedis实例均是可用的
     */
    Boolean TEST_ON_BORROW = true;

    /**
     * 在空闲时检查有效性, 默认false
     */
    Boolean TEST_WHILE_IDLE = true;

    /**
     * 是否进行有效性检查
     */
    Boolean TEST_ON_RETURN = true;
}
