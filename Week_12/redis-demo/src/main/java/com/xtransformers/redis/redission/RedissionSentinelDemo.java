package com.xtransformers.redis.redission;

import com.alibaba.fastjson.JSON;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.api.redisnode.RedisMasterSlave;
import org.redisson.api.redisnode.RedisNode;
import org.redisson.api.redisnode.RedisNodes;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;

import java.util.Map;

import static com.xtransformers.redis.Constants.*;

public class RedissionSentinelDemo {

    // 5. 基于 Redission 的 Sentinel 配置
    public static void main(String[] args) {
        Config config = new Config();
        config.setCodec(new StringCodec())
                .useSentinelServers()
                .setMasterName(SENTINEL_MASTER_NAME)
                .addSentinelAddress("redis://" + REDIS_HOST + ":" + SENTINEL_PORT_26379)
                .addSentinelAddress("redis://" + REDIS_HOST + ":" + SENTINEL_PORT_26380)
                .addSentinelAddress("redis://" + REDIS_HOST + ":" + SENTINEL_PORT_26381);
        RedissonClient client = Redisson.create(config);

        RedisMasterSlave redisNodes = client.getRedisNodes(RedisNodes.SENTINEL_MASTER_SLAVE);
        Map<String, String> masterInfo = redisNodes.getMaster().info(RedisNode.InfoSection.REPLICATION);
        System.out.println(JSON.toJSONString(masterInfo));

        // RBucket      string
        // RMap         hash
        // RSortedSet   Sorted set
        // RSet         set
        // RList        list
        RBucket<Object> uptime5 = client.getBucket("uptime5");
        uptime5.set(String.valueOf(System.currentTimeMillis()));
        System.out.println(uptime5.get());

        RBucket<Object> methodName = client.getBucket("methodName");
        methodName.set("RedissionSentinelDemo.main");
        System.out.println(methodName.get());

        client.shutdown();
    }

    /*
     * {
     * 	"second_repl_offset": "2805516",
     * 	"repl_backlog_first_byte_offset": "3703507",
     * 	"role": "master",
     * 	"repl_backlog_active": "1",
     * 	"repl_backlog_size": "1048576",
     * 	"connected_slaves": "2",
     * 	"slave0": "ip=192.168.56.95,port=6381,state=online,offset=4752068,lag=1",
     * 	"repl_backlog_histlen": "1048576",
     * 	"slave1": "ip=192.168.56.95,port=6379,state=online,offset=4752068,lag=1",
     * 	"master_replid": "f24d5c60df919f719d7d0d5ce1d26c929229ea32",
     * 	"master_replid2": "b86da707318692b7b7f04536f74aa9b089651d3f",
     * 	"master_repl_offset": "4752082"
     * }
     *
     * 1610295797968
     * RedissionSentinelDemo.main
     */
}
