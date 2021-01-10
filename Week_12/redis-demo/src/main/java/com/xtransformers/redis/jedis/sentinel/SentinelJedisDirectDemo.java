package com.xtransformers.redis.jedis.sentinel;

import com.alibaba.fastjson.JSON;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;

import static com.xtransformers.redis.Constants.*;

public class SentinelJedisDirectDemo {

    // 3. 直接连接 Sentinel 操作 Demo
    public static void main(String[] args) {
        Jedis sentinelJedis = new Jedis(REDIS_HOST, SENTINEL_PORT_26379);
        List<Map<String, String>> sentinelMasters = sentinelJedis.sentinelMasters();
        System.out.println(JSON.toJSONString(sentinelMasters));

        List<Map<String, String>> sentinelSlaves = sentinelJedis.sentinelSlaves(SENTINEL_MASTER_NAME);
        System.out.println(JSON.toJSONString(sentinelSlaves));
    }
    /*
     * [
     *        {
     * 		"role-reported": "master",
     * 		"info-refresh": "3662",
     * 		"config-epoch": "5",
     * 		"last-ping-sent": "0",
     * 		"role-reported-time": "807130",
     * 		"ip": "192.168.56.95",
     * 		"quorum": "2",
     * 		"flags": "master",
     * 		"parallel-syncs": "1",
     * 		"num-slaves": "2",
     * 		"link-pending-commands": "0",
     * 		"failover-timeout": "180000",
     * 		"port": "6380",
     * 		"num-other-sentinels": "2",
     * 		"name": "mymaster",
     * 		"last-ok-ping-reply": "975",
     * 		"last-ping-reply": "975",
     * 		"runid": "9c8637a59286ccec27d0b0faf12418c4a294db71",
     * 		"link-refcount": "1",
     * 		"down-after-milliseconds": "10000"
     *    }
     * ]
     */

    /*
     * [
     *        {
     * 		"role-reported": "slave",
     * 		"info-refresh": "3297",
     * 		"last-ping-sent": "0",
     * 		"role-reported-time": "807272",
     * 		"ip": "192.168.56.95",
     * 		"flags": "slave",
     * 		"slave-repl-offset": "2974008",
     * 		"master-port": "6380",
     * 		"link-pending-commands": "0",
     * 		"master-host": "192.168.56.95",
     * 		"slave-priority": "100",
     * 		"port": "6381",
     * 		"name": "192.168.56.95:6381",
     * 		"last-ok-ping-reply": "76",
     * 		"last-ping-reply": "76",
     * 		"runid": "d6f7cb212b953e53fc775e77d6c8a315e7bd760b",
     * 		"link-refcount": "1",
     * 		"master-link-status": "ok",
     * 		"master-link-down-time": "0",
     * 		"down-after-milliseconds": "10000"
     *    },
     *    {
     * 		"role-reported": "slave",
     * 		"info-refresh": "6511",
     * 		"last-ping-sent": "0",
     * 		"role-reported-time": "538346",
     * 		"ip": "192.168.56.95",
     * 		"flags": "slave",
     * 		"slave-repl-offset": "2973303",
     * 		"master-port": "6380",
     * 		"link-pending-commands": "0",
     * 		"master-host": "192.168.56.95",
     * 		"slave-priority": "100",
     * 		"port": "6379",
     * 		"name": "192.168.56.95:6379",
     * 		"last-ok-ping-reply": "76",
     * 		"last-ping-reply": "76",
     * 		"runid": "6363bb57f3e2ba1f2dffe425b17c7f8bcff961ba",
     * 		"link-refcount": "1",
     * 		"master-link-status": "ok",
     * 		"master-link-down-time": "0",
     * 		"down-after-milliseconds": "10000"
     *    }
     * ]
     */
}
