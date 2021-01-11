package com.xtransformers.redis.jedis.cluster;

import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.Map;

public class ClusterJedisDemo {

    // 7. 基于 Jedis 的 Cluster 操作 Demo
    public static void main(String[] args) {
        JedisCluster jedisCluster = ClusterJedisUtil.getJedisCluster();

        Map<String, JedisPool> clusterNodes = jedisCluster.getClusterNodes();
        JedisPool jedisPool = clusterNodes.values().iterator().next();
        System.out.println(jedisPool.getResource().info());

        for (int i = 0; i < 100; i++) {
            jedisCluster.set("cluster:" + i, "data:" + i);
        }

        System.out.println(jedisCluster.get("cluster:95"));

        ClusterJedisUtil.close();
    }

    /*
     * # Server
     * redis_version:6.0.9
     * redis_git_sha1:00000000
     * redis_git_dirty:0
     * redis_build_id:d32720aa5e0eaae1
     * redis_mode:cluster
     * os:Linux 3.10.0-1127.el7.x86_64 x86_64
     * arch_bits:64
     * multiplexing_api:epoll
     * atomicvar_api:atomic-builtin
     * gcc_version:9.3.1
     * process_id:2284
     * run_id:ccc02dd1e3104a2ba71b253419fd22ff67c06e5d
     * tcp_port:7004
     * uptime_in_seconds:1090
     * uptime_in_days:0
     * hz:10
     * configured_hz:10
     * lru_clock:16509089
     * executable:/opt/config/redis-server
     * config_file:/opt/redis-cluster/7004/redis.conf
     * io_threads_active:0
     *
     * # Clients
     * connected_clients:2
     * client_recent_max_input_buffer:8
     * client_recent_max_output_buffer:0
     * blocked_clients:0
     * tracking_clients:0
     * clients_in_timeout_table:0
     *
     * # Memory
     * used_memory:2674752
     * used_memory_human:2.55M
     * used_memory_rss:3739648
     * used_memory_rss_human:3.57M
     * used_memory_peak:2920912
     * used_memory_peak_human:2.79M
     * used_memory_peak_perc:91.57%
     * used_memory_overhead:2545648
     * used_memory_startup:1474920
     * used_memory_dataset:129104
     * used_memory_dataset_perc:10.76%
     * allocator_allocated:2832160
     * allocator_active:3125248
     * allocator_resident:5361664
     * total_system_memory:510652416
     * total_system_memory_human:487.00M
     * used_memory_lua:37888
     * used_memory_lua_human:37.00K
     * used_memory_scripts:0
     * used_memory_scripts_human:0B
     * number_of_cached_scripts:0
     * maxmemory:0
     * maxmemory_human:0B
     * maxmemory_policy:noeviction
     * allocator_frag_ratio:1.10
     * allocator_frag_bytes:293088
     * allocator_rss_ratio:1.72
     * allocator_rss_bytes:2236416
     * rss_overhead_ratio:0.70
     * rss_overhead_bytes:-1622016
     * mem_fragmentation_ratio:1.43
     * mem_fragmentation_bytes:1128376
     * mem_not_counted_for_evict:126
     * mem_replication_backlog:1048576
     * mem_clients_slaves:0
     * mem_clients_normal:20496
     * mem_aof_buffer:160
     * mem_allocator:jemalloc-5.1.0
     * active_defrag_running:0
     * lazyfree_pending_objects:0
     *
     * # Persistence
     * loading:0
     * rdb_changes_since_last_save:31
     * rdb_bgsave_in_progress:0
     * rdb_last_save_time:1610343519
     * rdb_last_bgsave_status:ok
     * rdb_last_bgsave_time_sec:-1
     * rdb_current_bgsave_time_sec:-1
     * rdb_last_cow_size:0
     * aof_enabled:1
     * aof_rewrite_in_progress:0
     * aof_rewrite_scheduled:0
     * aof_last_rewrite_time_sec:0
     * aof_current_rewrite_time_sec:-1
     * aof_last_bgrewrite_status:ok
     * aof_last_write_status:ok
     * aof_last_cow_size:229376
     * module_fork_in_progress:0
     * module_fork_last_cow_size:0
     * aof_current_size:1439
     * aof_base_size:92
     * aof_pending_rewrite:0
     * aof_buffer_length:0
     * aof_rewrite_buffer_length:0
     * aof_pending_bio_fsync:0
     * aof_delayed_fsync:0
     *
     * # Stats
     * total_connections_received:7
     * total_commands_processed:145
     * instantaneous_ops_per_sec:0
     * total_net_input_bytes:3119
     * total_net_output_bytes:40099
     * instantaneous_input_kbps:0.00
     * instantaneous_output_kbps:0.04
     * rejected_connections:0
     * sync_full:0
     * sync_partial_ok:0
     * sync_partial_err:0
     * expired_keys:0
     * expired_stale_perc:0.00
     * expired_time_cap_reached_count:0
     * expire_cycle_cpu_milliseconds:0
     * evicted_keys:0
     * keyspace_hits:0
     * keyspace_misses:0
     * pubsub_channels:0
     * pubsub_patterns:0
     * latest_fork_usec:149
     * migrate_cached_sockets:0
     * slave_expires_tracked_keys:0
     * active_defrag_hits:0
     * active_defrag_misses:0
     * active_defrag_key_hits:0
     * active_defrag_key_misses:0
     * tracking_total_keys:0
     * tracking_total_items:0
     * tracking_total_prefixes:0
     * unexpected_error_replies:0
     * total_reads_processed:146
     * total_writes_processed:1087
     * io_threaded_reads_processed:0
     * io_threaded_writes_processed:0
     *
     * # Replication
     * role:slave
     * master_host:192.168.56.95
     * master_port:7001
     * master_link_status:up
     * master_last_io_seconds_ago:3
     * master_sync_in_progress:0
     * slave_repl_offset:5701
     * slave_priority:100
     * slave_read_only:1
     * connected_slaves:0
     * master_replid:2195bbbe1e2ec362a7ccb10d0724d43cd350e9f1
     * master_replid2:0000000000000000000000000000000000000000
     * master_repl_offset:5701
     * second_repl_offset:-1
     * repl_backlog_active:1
     * repl_backlog_size:1048576
     * repl_backlog_first_byte_offset:2829
     * repl_backlog_histlen:2873
     *
     * # CPU
     * used_cpu_sys:0.802063
     * used_cpu_user:0.100724
     * used_cpu_sys_children:0.003369
     * used_cpu_user_children:0.000000
     *
     * # Modules
     *
     * # Cluster
     * cluster_enabled:1
     *
     * # Keyspace
     * db0:keys=31,expires=0,avg_ttl=0
     *
     * data:95
     */

}
