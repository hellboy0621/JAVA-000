package com.xtransformers.redis.jedis.cluster;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

import static com.xtransformers.redis.Constants.*;

public class ClusterJedisUtil {

    private static JedisCluster CLUSTER = createJedisCluster();

    public static JedisCluster getJedisCluster() {
        return CLUSTER;
    }

    public static void close() {
        CLUSTER.close();
    }

    private static JedisCluster createJedisCluster() {
        // 集群的服务节点
        Set<HostAndPort> hostAndPortSet = new HashSet<>();
        hostAndPortSet.add(new HostAndPort(CLUSTER_REDIS_HOST, CLUSTER_REDIS_PORT_7000));
        hostAndPortSet.add(new HostAndPort(CLUSTER_REDIS_HOST, CLUSTER_REDIS_PORT_7001));
        hostAndPortSet.add(new HostAndPort(CLUSTER_REDIS_HOST, CLUSTER_REDIS_PORT_7002));

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 最大空闲连接数，默认8
        jedisPoolConfig.setMaxIdle(12);
        // 最大连接数，默认8
        jedisPoolConfig.setMaxTotal(16);
        // 最小空闲连接数，默认0
        jedisPoolConfig.setMinIdle(4);
        // 获取连接最大等待时间
        jedisPoolConfig.setMaxWaitMillis(2000);
        // 对获取到的连接校验
        jedisPoolConfig.setTestOnBorrow(true);

        return new JedisCluster(hostAndPortSet, jedisPoolConfig);
    }


}
