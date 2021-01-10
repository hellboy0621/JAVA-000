package com.xtransformers.redis.jedis;

import redis.clients.jedis.Jedis;

import static com.xtransformers.redis.Constants.REDIS_HOST;
import static com.xtransformers.redis.Constants.REDIS_PORT_6379;

public class JedisDemo {

    // 1. 最简单 Jedis Demo
    public static void main(String[] args) {
        Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT_6379);
        System.out.println(jedis.info());

        jedis.set("uptime", String.valueOf(System.currentTimeMillis()));
        System.out.println(jedis.get("uptime"));
    }

    /*
     * # Server
     * redis_version:6.0.9
     * redis_git_sha1:00000000
     * redis_git_dirty:0
     * redis_build_id:d32720aa5e0eaae1
     * redis_mode:standalone
     * os:Linux 3.10.0-1127.el7.x86_64 x86_64
     * arch_bits:64
     * multiplexing_api:epoll
     * atomicvar_api:atomic-builtin
     * gcc_version:9.3.1
     * process_id:1361
     * run_id:6e8cf246123575dd062b05c16db8ccb27ec856d8
     * tcp_port:6379
     * uptime_in_seconds:12586
     * uptime_in_days:0
     * hz:10
     * configured_hz:10
     * lru_clock:16450564
     * executable:/opt/config/redis-server
     * config_file:/opt/config/redis_6379.conf
     * io_threads_active:0
     *
     * # Clients
     * connected_clients:8
     * client_recent_max_input_buffer:8
     * client_recent_max_output_buffer:0
     * blocked_clients:0
     * tracking_clients:0
     * clients_in_timeout_table:0
     *
     * # Memory
     * used_memory:2693432
     * used_memory_human:2.57M
     * used_memory_rss:4952064
     * used_memory_rss_human:4.72M
     * used_memory_peak:2693432
     * used_memory_peak_human:2.57M
     * used_memory_peak_perc:100.06%
     * used_memory_overhead:2037848
     * used_memory_startup:803320
     * used_memory_dataset:655584
     * used_memory_dataset_perc:34.68%
     * allocator_allocated:2707344
     * allocator_active:3100672
     * allocator_resident:5390336
     * total_system_memory:510652416
     * total_system_memory_human:487.00M
     * used_memory_lua:43008
     * used_memory_lua_human:42.00K
     * used_memory_scripts:240
     * used_memory_scripts_human:240B
     * number_of_cached_scripts:2
     * maxmemory:0
     * maxmemory_human:0B
     * maxmemory_policy:noeviction
     * allocator_frag_ratio:1.15
     * allocator_frag_bytes:393328
     * allocator_rss_ratio:1.74
     * allocator_rss_bytes:2289664
     * rss_overhead_ratio:0.92
     * rss_overhead_bytes:-438272
     * mem_fragmentation_ratio:1.91
     * mem_fragmentation_bytes:2363040
     * mem_not_counted_for_evict:0
     * mem_replication_backlog:1048576
     * mem_clients_slaves:41024
     * mem_clients_normal:143520
     * mem_aof_buffer:0
     * mem_allocator:jemalloc-5.1.0
     * active_defrag_running:0
     * lazyfree_pending_objects:0
     *
     * # Persistence
     * loading:0
     * rdb_changes_since_last_save:0
     * rdb_bgsave_in_progress:0
     * rdb_last_save_time:1610286069
     * rdb_last_bgsave_status:ok
     * rdb_last_bgsave_time_sec:0
     * rdb_current_bgsave_time_sec:-1
     * rdb_last_cow_size:487424
     * aof_enabled:0
     * aof_rewrite_in_progress:0
     * aof_rewrite_scheduled:0
     * aof_last_rewrite_time_sec:-1
     * aof_current_rewrite_time_sec:-1
     * aof_last_bgrewrite_status:ok
     * aof_last_write_status:ok
     * aof_last_cow_size:0
     * module_fork_in_progress:0
     * module_fork_last_cow_size:0
     *
     * # Stats
     * total_connections_received:31
     * total_commands_processed:44282
     * instantaneous_ops_per_sec:6
     * total_net_input_bytes:3264618
     * total_net_output_bytes:17762602
     * instantaneous_input_kbps:0.29
     * instantaneous_output_kbps:0.88
     * rejected_connections:0
     * sync_full:1
     * sync_partial_ok:1
     * sync_partial_err:1
     * expired_keys:0
     * expired_stale_perc:0.00
     * expired_time_cap_reached_count:0
     * expire_cycle_cpu_milliseconds:1
     * evicted_keys:0
     * keyspace_hits:0
     * keyspace_misses:0
     * pubsub_channels:1
     * pubsub_patterns:0
     * latest_fork_usec:119
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
     * total_reads_processed:43323
     * total_writes_processed:85996
     * io_threaded_reads_processed:0
     * io_threaded_writes_processed:0
     *
     * # Replication
     * role:master
     * connected_slaves:2
     * slave0:ip=192.168.56.95,port=6380,state=online,offset=2360366,lag=1
     * slave1:ip=192.168.56.95,port=6381,state=online,offset=2360507,lag=0
     * master_replid:b86da707318692b7b7f04536f74aa9b089651d3f
     * master_replid2:db858cd9ecb624f21bad9d3b8d7fdc0162a12324
     * master_repl_offset:2360507
     * second_repl_offset:2334773
     * repl_backlog_active:1
     * repl_backlog_size:1048576
     * repl_backlog_first_byte_offset:1311932
     * repl_backlog_histlen:1048576
     *
     * # CPU
     * used_cpu_sys:7.101266
     * used_cpu_user:1.660403
     * used_cpu_sys_children:0.002196
     * used_cpu_user_children:0.000000
     *
     * # Modules
     *
     * # Cluster
     * cluster_enabled:0
     *
     * # Keyspace
     * db0:keys=22,expires=0,avg_ttl=0
     *
     * 1610286085205
     */

}
