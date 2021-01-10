package com.xtransformers.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.util.Properties;

@SpringBootApplication
public class SentinelApplication implements ApplicationRunner {

    // 6. 基于 Spring Boot/Spring Data Redis 的 Sentinel 配置
    public static void main(String[] args) {
        SpringApplication.run(SentinelApplication.class, args);
    }

    @Autowired
    private LettuceConnectionFactory connectionFactory;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        RedisConnection connection = connectionFactory.getConnection();
        Properties info = connection.serverCommands().info("replication");
        System.out.println(JSON.toJSONString(info));

        RedisStringCommands redisStringCommands = connection.stringCommands();
        redisStringCommands.set("uptime6".getBytes(), String.valueOf(System.currentTimeMillis()).getBytes());
        System.out.println(new String(redisStringCommands.get("uptime6".getBytes())));

        redisStringCommands.set("methodName".getBytes(), "SentinelApplication.main".getBytes());
        System.out.println(new String(redisStringCommands.get("methodName".getBytes())));

        // 会把服务端关掉，慎用
        // connection.shutdown();
    }
    /*
     * {
     *     "master_replid": "d877bc6dcffb5b203ccd6b1b930753142c2ea2d7",
     *     "repl_backlog_size": "1048576",
     *     "connected_slaves": "2",
     *     "second_repl_offset": "-1",
     *     "repl_backlog_histlen": "64212",
     *     "repl_backlog_active": "1",
     *     "repl_backlog_first_byte_offset": "1",
     *     "role": "master",
     *     "master_replid2": "0000000000000000000000000000000000000000",
     *     "master_repl_offset": "64212",
     *     "slave1": "ip=192.168.56.95,port=6379,state=online,offset=64198,lag=1",
     *     "slave0": "ip=192.168.56.95,port=6380,state=online,offset=64212,lag=0"
     * }
     *
     * 1610299233714
     * SentinelApplication.main
     */
}
