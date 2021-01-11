class23

1、（必做）配置redis的主从复制，sentinel高可用，Cluster集群。

1.安装虚拟机

安装 vagrant 和 Oracle VM Virtual。

在安装 Oracle VM Virtual 后，会自动创建一个虚拟网卡 VirtualBox Host-Only Network，查看其 IP 地址为 192.168.56.1。

在任意文件夹下（如 D:\develop\vagrant\master），运行如下命令后，会自动创建一个 Vagrantfile 文件。

```bash
vagrant init
```

修改 Vagrantfile 文件内容如下，这里如果创建多个虚拟机可以在 boxes 数组中再增加，需要把 name 和 eth1 改成不一样的即可，这里要保证网段跟虚拟网卡在一个网段即可。

```bash
boxes = [
    {
        :name => "master",
        :eth1 => "192.168.56.95",
        :mem => "512",
        :cpu => "1"
    }
]

Vagrant.configure(2) do |config|

  config.vm.box = "centos/7"

   boxes.each do |opts|
      config.vm.define opts[:name] do |config|
        config.vm.hostname = opts[:name]
        config.vm.provider "vmware_fusion" do |v|
          v.vmx["memsize"] = opts[:mem]
          v.vmx["numvcpus"] = opts[:cpu]
        end

        config.vm.provider "virtualbox" do |v|
          v.customize ["modifyvm", :id, "--memory", opts[:mem]]
                  v.customize ["modifyvm", :id, "--cpus", opts[:cpu]]
                  v.customize ["modifyvm", :id, "--name", opts[:name]]
        end

        config.vm.network :private_network, ip: opts[:eth1]
      end
  end

end
```

运行如下命令，启动虚拟机

```bash
vagrant up
```

启动完成后，连接虚拟机

```bash
vagrant ssh
```

切换至 root 用户，并修改 root 用户密码

```bash
sudo -i
passwd
```

在宿主机上查看是否能够 ping 通虚拟机

```bash
C:\Users\hero0>ping 192.168.56.95

正在 Ping 192.168.56.95 具有 32 字节的数据:
来自 192.168.56.95 的回复: 字节=32 时间<1ms TTL=64
来自 192.168.56.95 的回复: 字节=32 时间<1ms TTL=64
来自 192.168.56.95 的回复: 字节=32 时间<1ms TTL=64
来自 192.168.56.95 的回复: 字节=32 时间<1ms TTL=64

192.168.56.95 的 Ping 统计信息:
    数据包: 已发送 = 4，已接收 = 4，丢失 = 0 (0% 丢失)，
往返行程的估计时间(以毫秒为单位):
    最短 = 0ms，最长 = 0ms，平均 = 0ms
```

修改 sshd 配置，允许使用 ssh 工具访问虚拟机，而不仅仅通过 vagrant ssh 来连接。

```bash
vi /etc/ssh/sshd_config
```

找到如下配置

```bash
# To disable tunneled clear text passwords, change to no here!
#PasswordAuthentication yes
#PermitEmptyPasswords no
PasswordAuthentication no
```

修改为

```bash
# To disable tunneled clear text passwords, change to no here!
PasswordAuthentication yes
#PermitEmptyPasswords no
#PasswordAuthentication no
```

使配置生效

```bash
systemctl restart sshd
```

2.安装 Redis 前准备

因为是最小化安装，好多工具需要安装或更新。

```bash
yum install wget -y
```

自 redis 6.0.0 之后，编译 redis 需要支持 C11 特性，C11 特性在 4.9 中被引入。Centos7 默认 gcc 版本为 4.8.5，所以需要升级gcc版本。

如果没有梯子的同学，需要自行配置国内 yum 源，可参考 https://blog.csdn.net/hellboy0621/article/details/80392273 的  3.7 配置国内yum源和epel源。

```bash
yum -y install gcc gcc-c++ make tcl
yum -y install centos-release-scl
yum -y install devtoolset-9-gcc devtoolset-9-gcc-c++ devtoolset-9-binutils
scl enable devtoolset-9 bash
```

3.安装 Redis

到 redis 官网 https://redis.io/download ，有安装流程。

```bash
cd /usr/local/

$ wget https://download.redis.io/releases/redis-6.0.9.tar.gz
$ tar xzf redis-6.0.9.tar.gz
$ cd redis-6.0.9
$ make
```

编译成功后，只有有一个 warning，不会有 error。

```bash
make[1]: Leaving directory `/usr/local/redis-6.0.9/src'
```

将 redis 命令加入环境变量中。

4.主从复制

规划：主节点端口号使用6379，从节点端口号使用6380。

配置文件放在 /opt/config/ 路径下，编辑配置文件。

```bash
cd /opt/
mkdir config
cd config
mkdir redis_6379
mkdir redis_6380
vi redis_6379.conf
```

redis_6379.conf

```bash
bind 0.0.0.0

protected-mode yes

port 6379

tcp-backlog 511

timeout 0

tcp-keepalive 300

daemonize no

supervised no

pidfile /var/run/redis_6379.pid

loglevel notice

logfile ""

always-show-logo yes

save 900 1
save 300 10
save 60 10000

stop-writes-on-bgsave-error yes

rdbcompression yes

rdbchecksum yes

dbfilename dump.rdb

rdb-del-sync-files no

dir /opt/config/redis_6379/

# replicaof <masterip> <masterport>

replica-serve-stale-data yes

replica-read-only yes

repl-diskless-sync no

repl-diskless-sync-delay 5

repl-diskless-load disabled

repl-disable-tcp-nodelay no

replica-priority 100

lazyfree-lazy-eviction no
lazyfree-lazy-expire no
lazyfree-lazy-server-del no
replica-lazy-flush no

lazyfree-lazy-user-del no

oom-score-adj no

oom-score-adj-values 0 200 800

appendonly no

appendfilename "appendonly.aof"

# appendfsync always
appendfsync everysec
# appendfsync no

no-appendfsync-on-rewrite no

auto-aof-rewrite-percentage 100
auto-aof-rewrite-min-size 64mb

aof-load-truncated yes

aof-use-rdb-preamble yes

lua-time-limit 5000

slowlog-log-slower-than 10000

slowlog-max-len 128

latency-monitor-threshold 0

notify-keyspace-events ""

hash-max-ziplist-entries 512
hash-max-ziplist-value 64

list-max-ziplist-size -2

list-compress-depth 0

set-max-intset-entries 512

zset-max-ziplist-entries 128
zset-max-ziplist-value 64

hll-sparse-max-bytes 3000

stream-node-max-bytes 4096
stream-node-max-entries 100

activerehashing yes

client-output-buffer-limit normal 0 0 0
client-output-buffer-limit replica 256mb 64mb 60
client-output-buffer-limit pubsub 32mb 8mb 60

hz 10

dynamic-hz yes

aof-rewrite-incremental-fsync yes

rdb-save-incremental-fsync yes
```

redis_6380.conf

与 redis_6379.conf 不同的地方有4行

```bash
port 6380
pidfile /var/run/redis_6380.pid
dir /opt/config/redis_6380/
replicaof 192.168.56.95 6379
```

整个文件内容如下：

```bash
bind 0.0.0.0

protected-mode yes

port 6380

tcp-backlog 511

timeout 0

tcp-keepalive 300

daemonize no

supervised no

pidfile /var/run/redis_6380.pid

loglevel notice

logfile ""

always-show-logo yes

save 900 1
save 300 10
save 60 10000

stop-writes-on-bgsave-error yes

rdbcompression yes

rdbchecksum yes

dbfilename dump.rdb

rdb-del-sync-files no

dir /opt/config/redis_6380/

replicaof 192.168.56.95 6379

replica-serve-stale-data yes

replica-read-only yes

repl-diskless-sync no

repl-diskless-sync-delay 5

repl-diskless-load disabled

repl-disable-tcp-nodelay no

replica-priority 100

lazyfree-lazy-eviction no
lazyfree-lazy-expire no
lazyfree-lazy-server-del no
replica-lazy-flush no

lazyfree-lazy-user-del no

oom-score-adj no

oom-score-adj-values 0 200 800

appendonly no

appendfilename "appendonly.aof"

# appendfsync always
appendfsync everysec
# appendfsync no

no-appendfsync-on-rewrite no

auto-aof-rewrite-percentage 100
auto-aof-rewrite-min-size 64mb

aof-load-truncated yes

aof-use-rdb-preamble yes

lua-time-limit 5000

slowlog-log-slower-than 10000

slowlog-max-len 128

latency-monitor-threshold 0

notify-keyspace-events ""

hash-max-ziplist-entries 512
hash-max-ziplist-value 64

list-max-ziplist-size -2

list-compress-depth 0

set-max-intset-entries 512

zset-max-ziplist-entries 128
zset-max-ziplist-value 64

hll-sparse-max-bytes 3000

stream-node-max-bytes 4096
stream-node-max-entries 100

activerehashing yes

client-output-buffer-limit normal 0 0 0
client-output-buffer-limit replica 256mb 64mb 60
client-output-buffer-limit pubsub 32mb 8mb 60

hz 10

dynamic-hz yes

aof-rewrite-incremental-fsync yes

rdb-save-incremental-fsync yes
```

启动 Redis 主从节点服务

```bash
redis-server /opt/config/redis_6379.conf >/dev/null &
redis-server /opt/config/redis_6380.conf >/dev/null &
```

使用客户端连接，查看信息

```bash
redis-cli -h 192.168.56.95 -p 6379

192.168.56.95:6379> info replication
# Replication
role:master
connected_slaves:1
slave0:ip=192.168.56.95,port=6380,state=online,offset=182,lag=0
master_replid:948c768af079df695f69540960998adb4f1059e1
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:182
second_repl_offset:-1
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:1
repl_backlog_histlen:182

192.168.56.95:6379> hset set1 a 1 b 2 c 3
(integer) 3
192.168.56.95:6379> hget set1 b
"2"
192.168.56.95:6379> dbsize
(integer) 1
```

```bash
redis-cli -h 192.168.56.95 -p 6380

192.168.56.95:6380> info replication
# Replication
role:slave
master_host:192.168.56.95
master_port:6379
master_link_status:up
master_last_io_seconds_ago:4
master_sync_in_progress:0
slave_repl_offset:154
slave_priority:100
slave_read_only:1
connected_slaves:0
master_replid:948c768af079df695f69540960998adb4f1059e1
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:154
second_repl_offset:-1
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:1
repl_backlog_histlen:154

192.168.56.95:6380> dbsize
(integer) 1
192.168.56.95:6380> hget set1 c
"3"
```

主从复制搭建完成。

5.哨兵高可用

在第4步主从复制的基础上，再搭建2个 sentine l哨兵节点 sentinel_0 和 sentinel_1。

编辑配置文件

```bash
cd /opt/config/
vi sentinel_0.conf
```

```bash
sentinel deny-scripts-reconfig yes
sentinel monitor mymaster 192.168.56.95 6379 2
sentinel down-after-milliseconds mymaster 10000
```

先启动 sentinel_0 节点

```bash
redis-sentinel /opt/config/sentinel_0.conf >/dev/null &
```

启动后，配置文件会被修改成如下内容

```bash
[root@master config]# cat sentinel_0.conf
sentinel myid 9351bcb1305a89e68b1e9a4a21f9de98d2234cd1
sentinel deny-scripts-reconfig yes
sentinel monitor mymaster 192.168.56.95 6379 2
# Generated by CONFIG REWRITE
protected-mode no
port 26379
user default on nopass ~* +@all
dir "/opt/config"
sentinel down-after-milliseconds mymaster 10000
sentinel config-epoch mymaster 0
sentinel leader-epoch mymaster 0
sentinel known-replica mymaster 192.168.56.95 6380
sentinel current-epoch 0
```

默认端口号为26379，如果再启动 sentinel_1.conf 不修改端口号的话，不能正常启动，因此编辑 sentinel_1.conf 内容如下：

```bash
cd /opt/config/
vi sentinel_1.conf
```

```bash
sentinel deny-scripts-reconfig yes
sentinel monitor mymaster 192.168.56.95 6379 2
sentinel down-after-milliseconds mymaster 10000
port 26380
```

启动 sentinel_1 哨兵节点

```bash
redis-sentinel /opt/config/sentinel_1.conf >/dev/null &
```

查看 sentinel_1.conf

```bash
[root@master config]# cat sentinel_1.conf
sentinel myid 29fa71bef74d232b9c1ebe85620ce05e0c82a404
sentinel deny-scripts-reconfig yes
sentinel monitor mymaster 192.168.56.95 6379 2
port 26380
# Generated by CONFIG REWRITE
protected-mode no
user default on nopass ~* +@all
dir "/opt/config"
sentinel down-after-milliseconds mymaster 10000
sentinel config-epoch mymaster 0
sentinel leader-epoch mymaster 0
sentinel known-replica mymaster 192.168.56.95 6380
sentinel known-sentinel mymaster 192.168.56.95 26379 9351bcb1305a89e68b1e9a4a21f9de98d2234cd1
sentinel current-epoch 0
```

查看 Redis 相关后台启动的服务

```bash
[root@master config]# ps -ef | grep redis
root     27418  3482  0 06:21 pts/1    00:00:01 redis-server 0.0.0.0:6379
root     27423  3482  0 06:22 pts/1    00:00:01 redis-server 0.0.0.0:6380
root     27497 27445  0 06:23 pts/0    00:00:00 redis-cli -h 192.168.56.95 -p 6379
root     27498 27477  0 06:23 pts/2    00:00:00 redis-cli -h 192.168.56.95 -p 6380
root     27531  3482  0 06:33 pts/1    00:00:00 redis-sentinel *:26379 [sentinel]
root     27567  3482  0 06:38 pts/1    00:00:00 redis-sentinel *:26380 [sentinel]
root     27581  3482  0 06:40 pts/1    00:00:00 grep --color=auto redis
```

因为 redis-sentinel 本质上是 redis-server ，可以连接上查看信息

```bash
[root@master config]# redis-cli -p 26379
127.0.0.1:26379> info Sentinel
# Sentinel
sentinel_masters:1
sentinel_tilt:0
sentinel_running_scripts:0
sentinel_scripts_queue_length:0
sentinel_simulate_failure_flags:0
master0:name=mymaster,status=ok,address=192.168.56.95:6379,slaves=1,sentinels=2
```



验证哨兵的故障转移是否正常。

现在主节点是6379，结束其进程，查看6380的主从信息

```bash
kill -9 27418
```

6379 进程结束前后命令

```bash
192.168.56.95:6379> info replication
# Replication
role:master
connected_slaves:1
slave0:ip=192.168.56.95,port=6380,state=online,offset=106909,lag=1
master_replid:948c768af079df695f69540960998adb4f1059e1
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:106909
second_repl_offset:-1
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:1
repl_backlog_histlen:106909

192.168.56.95:6379> info replication
Could not connect to Redis at 192.168.56.95:6379: Connection refused
not connected>

```

6380节点，等待6379服务关闭后10s后，切换为主节点

```bash
192.168.56.95:6380> info replication
# Replication
role:slave
master_host:192.168.56.95
master_port:6379
master_link_status:down
master_last_io_seconds_ago:-1
master_sync_in_progress:0
slave_repl_offset:108474
master_link_down_since_seconds:10
slave_priority:100
slave_read_only:1
connected_slaves:0
master_replid:948c768af079df695f69540960998adb4f1059e1
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:108474
second_repl_offset:-1
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:1
repl_backlog_histlen:108474

192.168.56.95:6380> info replication
# Replication
role:master
connected_slaves:0
master_replid:94e457fd4571a2993ce0b90f77af5912caead5bd
master_replid2:948c768af079df695f69540960998adb4f1059e1
master_repl_offset:108474
second_repl_offset:108475
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:1
repl_backlog_histlen:108474
```



6.集群

```bash
cd /opt
mkdir redis-cluster
cd redis-cluster
mkdir 7000 7001 7002 7003 7004 7005
```

分别在文件夹下创建自己的配置文件 redis.conf，只要修改每个文件的端口号。

```bash
# /opt/redis-cluster/7000/redis.conf
port 7000
cluster-enabled yes
cluster-config-file /opt/redis-cluster/7000/nodes.conf
cluster-node-timeout 5000
appendonly yes
bind 0.0.0.0
```

这里有个坑，因为我是用的虚拟机，如果没有最后一行的 bind 命令，如果你在宿主机上打算连接这个集群时，会报错如下所示：

```bash
(error) DENIED Redis is running in protected mode because protected mode is enabled, no bind address was specified, no authentication password is requested to clients. In this mode connections are only accepted from the loopback interface. If you want to connect from external computers to Redis you may adopt one of the following solutions: 1) Just disable protected mode sending the command 'CONFIG SET protected-mode no' from the loopback interface by connecting to Redis from the same host the server is running, however MAKE SURE Redis is not publicly accessible from internet if you do so. Use CONFIG REWRITE to make this change permanent. 2) Alternatively you can just disable the protected mode by editing the Redis configuration file, and setting the protected mode option to 'no', and then restarting the server. 3) If you started the server manually just for testing, restart it with the '--protected-mode no' option. 4) Setup a bind address or an authentication password. NOTE: You only need to do one of the above things in order for the server to start accepting connections from the outside.
```

所以这里使用虚拟机 IP 地址进行创建：

```bash
redis-cli --cluster create 192.168.56.95:7000 192.168.56.95:7001 \
> 192.168.56.95:7002 192.168.56.95:7003 192.168.56.95:7004 192.168.56.95:7005 \
> --cluster-replicas 1
```

执行上述命令后，redis-cli 会提出一个建议，如果接受，键入 yes 即可，具体如下：

```bash
[root@master redis-cluster]# redis-cli --cluster create 192.168.56.95:7000 192.168.56.95:7001 \
> 192.168.56.95:7002 192.168.56.95:7003 192.168.56.95:7004 192.168.56.95:7005 \
> --cluster-replicas 1
>>> Performing hash slots allocation on 6 nodes...
Master[0] -> Slots 0 - 5460
Master[1] -> Slots 5461 - 10922
Master[2] -> Slots 10923 - 16383
Adding replica 192.168.56.95:7004 to 192.168.56.95:7000
Adding replica 192.168.56.95:7005 to 192.168.56.95:7001
Adding replica 192.168.56.95:7003 to 192.168.56.95:7002
>>> Trying to optimize slaves allocation for anti-affinity
[WARNING] Some slaves are in the same host as their master
M: 6ca88a5bd6cdc139c33dc3b90501c686567d7088 192.168.56.95:7000
   slots:[0-5460] (5461 slots) master
M: 1974cd8abd06ff9638004b5dbaf968cff6e54a55 192.168.56.95:7001
   slots:[5461-10922] (5462 slots) master
M: bc7c30276b6fe86d9121e07eb5c8e16a784572a9 192.168.56.95:7002
   slots:[10923-16383] (5461 slots) master
S: 9cd58bd9b083ecfae691600394bba5241943db97 192.168.56.95:7003
   replicates 6ca88a5bd6cdc139c33dc3b90501c686567d7088
S: 9abdbc210808c6fba9272459aa840db0bf34d457 192.168.56.95:7004
   replicates 1974cd8abd06ff9638004b5dbaf968cff6e54a55
S: c86d5ecfbed627320f2fcc423ebb19c7917fee48 192.168.56.95:7005
   replicates bc7c30276b6fe86d9121e07eb5c8e16a784572a9
Can I set the above configuration? (type 'yes' to accept): yes
>>> Nodes configuration updated
>>> Assign a different config epoch to each node
>>> Sending CLUSTER MEET messages to join the cluster
Waiting for the cluster to join
..
>>> Performing Cluster Check (using node 192.168.56.95:7000)
M: 6ca88a5bd6cdc139c33dc3b90501c686567d7088 192.168.56.95:7000
   slots:[0-5460] (5461 slots) master
   1 additional replica(s)
S: 9cd58bd9b083ecfae691600394bba5241943db97 192.168.56.95:7003
   slots: (0 slots) slave
   replicates 6ca88a5bd6cdc139c33dc3b90501c686567d7088
M: bc7c30276b6fe86d9121e07eb5c8e16a784572a9 192.168.56.95:7002
   slots:[10923-16383] (5461 slots) master
   1 additional replica(s)
S: c86d5ecfbed627320f2fcc423ebb19c7917fee48 192.168.56.95:7005
   slots: (0 slots) slave
   replicates bc7c30276b6fe86d9121e07eb5c8e16a784572a9
S: 9abdbc210808c6fba9272459aa840db0bf34d457 192.168.56.95:7004
   slots: (0 slots) slave
   replicates 1974cd8abd06ff9638004b5dbaf968cff6e54a55
M: 1974cd8abd06ff9638004b5dbaf968cff6e54a55 192.168.56.95:7001
   slots:[5461-10922] (5462 slots) master
   1 additional replica(s)
[OK] All nodes agree about slots configuration.
>>> Check for open slots...
>>> Check slots coverage...
[OK] All 16384 slots covered.
```

连接集群，设值并读取

```bash
[root@master redis-cluster]# redis-cli -c -p 7000
127.0.0.1:7000> keys *
(empty array)
127.0.0.1:7000> set foo bar
-> Redirected to slot [12182] located at 192.168.56.95:7002
OK
192.168.56.95:7002> set hello world
-> Redirected to slot [866] located at 192.168.56.95:7000
OK
192.168.56.95:7000> keys *
1) "hello"
192.168.56.95:7000> get foo
-> Redirected to slot [12182] located at 192.168.56.95:7002
"bar"
192.168.56.95:7002> keys *
1) "foo"
192.168.56.95:7002> get hello
-> Redirected to slot [866] located at 192.168.56.95:7000
"world"
```









class23-2 练习示例代码里下列类中的作业题

详见文件夹 redis-demo。

1. 最简单 Jedis Demo（com.xtransformers.redis.jedis.JedisDemo）
2. 基于 Sentinel 和连接池的 Demo（com.xtransformers.redis.jedis.sentinel.SentinelJedisDemo）
3. 直接连接 Sentinel 操作 Demo（com.xtransformers.redis.jedis.sentinel.SentinelJedisDirectDemo）
4. 基于 Lettuce 的 Sentinel 配置（com.xtransformers.redis.lettuce.LettuceSentinelDemo）
5. 基于 Redission 的 Sentinel 配置（com.xtransformers.redis.redission.RedissionSentinelDemo）
6. 基于 Spring Boot/Spring Data Redis 的 Sentinel 配置（com.xtransformers.redis.SentinelApplication 及 application.yml）

未完待续。。。