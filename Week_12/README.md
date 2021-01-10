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





class23-2 练习示例代码里下列类中的作业题

详见文件夹 redis-demo。

1. 最简单 Jedis Demo（com.xtransformers.redis.jedis.JedisDemo）
2. 基于 Sentinel 和连接池的 Demo（com.xtransformers.redis.jedis.sentinel.SentinelJedisDemo）
3. 直接连接 Sentinel 操作 Demo（com.xtransformers.redis.jedis.sentinel.SentinelJedisDirectDemo）
4. 基于 Lettuce 的 Sentinel 配置（com.xtransformers.redis.lettuce.LettuceSentinelDemo）
5. 基于 Redission 的 Sentinel 配置（com.xtransformers.redis.redission.RedissionSentinelDemo）
6. 基于 Spring Boot/Spring Data Redis 的 Sentinel 配置（com.xtransformers.redis.SentinelApplication 及 application.yml）

未完待续。。。

