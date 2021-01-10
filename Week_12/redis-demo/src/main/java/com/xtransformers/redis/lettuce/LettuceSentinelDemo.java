package com.xtransformers.redis.lettuce;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

import static com.xtransformers.redis.Constants.*;

public class LettuceSentinelDemo {

    // 4. 基于 Lettuce 的 Sentinel 配置
    public static void main(String[] args) {
        RedisURI redisUri = RedisURI.Builder
                .sentinel(REDIS_HOST, SENTINEL_PORT_26379, SENTINEL_MASTER_NAME)
                .withSentinel(REDIS_HOST, SENTINEL_PORT_26380)
                .withSentinel(REDIS_HOST, SENTINEL_PORT_26381)
                .build();
        RedisClient client = RedisClient.create(redisUri);

        StatefulRedisConnection<String, String> connect = client.connect();
        RedisCommands<String, String> sync = connect.sync();

        System.out.println(sync.info("replication"));

        sync.set("uptime4", String.valueOf(System.currentTimeMillis()));
        System.out.println(sync.get("uptime4"));

        sync.set("methodName", "LettuceDemo.main");
        System.out.println(sync.get("methodName"));
    }

    /*
     * # Replication
     * role:master
     * connected_slaves:2
     * slave0:ip=192.168.56.95,port=6381,state=online,offset=4036858,lag=0
     * slave1:ip=192.168.56.95,port=6379,state=online,offset=4036858,lag=0
     * master_replid:f24d5c60df919f719d7d0d5ce1d26c929229ea32
     * master_replid2:b86da707318692b7b7f04536f74aa9b089651d3f
     * master_repl_offset:4036858
     * second_repl_offset:2805516
     * repl_backlog_active:1
     * repl_backlog_size:1048576
     * repl_backlog_first_byte_offset:2988283
     * repl_backlog_histlen:1048576
     *
     * 1610294116301
     * LettuceSentinelDemo.main
     *
     * Process finished with exit code 0
     */

}
