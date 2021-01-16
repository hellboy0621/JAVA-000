class25

  2、（选做）安装kafka-manager工具，监控kafka集群状态。   

kafka manager

在虚拟机中搭建3台节点的 kafka 集群，并使用部署在宿主机上的 kafka manager （yahoo / CMAK）进行监控。

CMAK 官方 github 地址，https://github.com/yahoo/CMAK。

CMAK (Cluster Manager for Apache Kafka, previously known as Kafka Manager)。

***

1.搭建3台 kafka 集群。

1.1单独下载 zookeeper，这样宿主机才方便使用 ZooInspector 图形化工具查看 zk 节点内容。（如果你知道怎样把 kafka 内置的 zk 暴露给宿主机，就不需要这么麻烦了，我是因为之前就有单独的 zk。）

启动 zk。

```bash
./bin/zkServer.sh start
```

1.2下载 kafka 安装包，配置环境变量。修改配置文件。

server-9000.properties（server-9001.properties/server-9002.properties）

里面不同的设置有3个地方，broker.id 分别为 0、1、2，listeners 的端口号分别为 9000、9001、9002，log.dirs 路径最后拼接分别为 9000、9001、9002。

```bash
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# see kafka.server.KafkaConfig for additional details and defaults

############################# Server Basics #############################

# The id of the broker. This must be set to a unique integer for each broker.
broker.id=0

############################# Socket Server Settings #############################

# The address the socket server listens on. It will get the value returned from
# java.net.InetAddress.getCanonicalHostName() if not configured.
#   FORMAT:
#     listeners = listener_name://host_name:port
#   EXAMPLE:
#     listeners = PLAINTEXT://your.host.name:9092
#listeners=PLAINTEXT://:9092
listeners=PLAINTEXT://192.168.56.95:9000

# Hostname and port the broker will advertise to producers and consumers. If not set,
# it uses the value for "listeners" if configured.  Otherwise, it will use the value
# returned from java.net.InetAddress.getCanonicalHostName().
#advertised.listeners=PLAINTEXT://your.host.name:9092

# Maps listener names to security protocols, the default is for them to be the same. See the config documentation for more details
#listener.security.protocol.map=PLAINTEXT:PLAINTEXT,SSL:SSL,SASL_PLAINTEXT:SASL_PLAINTEXT,SASL_SSL:SASL_SSL

# The number of threads that the server uses for receiving requests from the network and sending responses to the network
num.network.threads=3

# The number of threads that the server uses for processing requests, which may include disk I/O
num.io.threads=8

# The send buffer (SO_SNDBUF) used by the socket server
socket.send.buffer.bytes=102400

# The receive buffer (SO_RCVBUF) used by the socket server
socket.receive.buffer.bytes=102400

# The maximum size of a request that the socket server will accept (protection against OOM)
socket.request.max.bytes=104857600


############################# Log Basics #############################

# A comma separated list of directories under which to store log files
#log.dirs=/tmp/kafka-logs
log.dirs=/opt/kafka-cluster/kafka-logs-9000

# The default number of log partitions per topic. More partitions allow greater
# parallelism for consumption, but this will also result in more files across
# the brokers.
num.partitions=1

# The number of threads per data directory to be used for log recovery at startup and flushing at shutdown.
# This value is recommended to be increased for installations with data dirs located in RAID array.
num.recovery.threads.per.data.dir=1

############################# Internal Topic Settings  #############################
# The replication factor for the group metadata internal topics "__consumer_offsets" and "__transaction_state"
# For anything other than development testing, a value greater than 1 is recommended to ensure availability such as 3.
offsets.topic.replication.factor=1
transaction.state.log.replication.factor=1
transaction.state.log.min.isr=1

############################# Log Flush Policy #############################

# Messages are immediately written to the filesystem but by default we only fsync() to sync
# the OS cache lazily. The following configurations control the flush of data to disk.
# There are a few important trade-offs here:
#    1. Durability: Unflushed data may be lost if you are not using replication.
#    2. Latency: Very large flush intervals may lead to latency spikes when the flush does occur as there will be a lot of data to flush.
#    3. Throughput: The flush is generally the most expensive operation, and a small flush interval may lead to excessive seeks.
# The settings below allow one to configure the flush policy to flush data after a period of time or
# every N messages (or both). This can be done globally and overridden on a per-topic basis.

# The number of messages to accept before forcing a flush of data to disk
#log.flush.interval.messages=10000

# The maximum amount of time a message can sit in a log before we force a flush
#log.flush.interval.ms=1000

############################# Log Retention Policy #############################

# The following configurations control the disposal of log segments. The policy can
# be set to delete segments after a period of time, or after a given size has accumulated.
# A segment will be deleted whenever *either* of these criteria are met. Deletion always happens
# from the end of the log.

# The minimum age of a log file to be eligible for deletion due to age
log.retention.hours=168

# A size-based retention policy for logs. Segments are pruned from the log unless the remaining
# segments drop below log.retention.bytes. Functions independently of log.retention.hours.
#log.retention.bytes=1073741824

# The maximum size of a log segment file. When this size is reached a new log segment will be created.
log.segment.bytes=1073741824

# The interval at which log segments are checked to see if they can be deleted according
# to the retention policies
log.retention.check.interval.ms=300000

############################# Zookeeper #############################

# Zookeeper connection string (see zookeeper docs for details).
# This is a comma separated host:port pairs, each corresponding to a zk
# server. e.g. "127.0.0.1:3000,127.0.0.1:3001,127.0.0.1:3002".
# You can also append an optional chroot string to the urls to specify the
# root directory for all kafka znodes.
zookeeper.connect=localhost:2181

# Timeout in ms for connecting to zookeeper
zookeeper.connection.timeout.ms=18000


############################# Group Coordinator Settings #############################

# The following configuration specifies the time, in milliseconds, that the GroupCoordinator will delay the initial consumer rebalance.
# The rebalance will be further delayed by the value of group.initial.rebalance.delay.ms as new members join the group, up to a maximum of max.poll.interval.ms.
# The default value for this is 3 seconds.
# We override this to 0 here as it makes for a better out-of-the-box experience for development and testing.
# However, in production environments the default value of 3 seconds is more suitable as this will help to avoid unnecessary, and potentially expensive, rebalances during application startup.
group.initial.rebalance.delay.ms=0
```

因为 CMAK 监控需要通过 JMX 获取 Kafka 相关数据，启动时需要开启 JMX。

查了很多资料，修改起来都很麻烦，以下链接是 CMAK 的 官方 issue，可以参考下 https://github.com/yahoo/CMAK/issues/187。

我这里偷了个懒，在启动命令前面加上 JMX_PORT 参数。

```bash
JMX_PORT=9900 kafka-server-start.sh /opt/kafka-cluster/server-9000.properties
JMX_PORT=9901 kafka-server-start.sh /opt/kafka-cluster/server-9001.properties
JMX_PORT=9902 kafka-server-start.sh /opt/kafka-cluster/server-9002.properties
```

2.下载 cmak 压缩包。

我下载的是 cmak-3.0.0.5.zip，解压缩，修改配置文件。

这里有一个坑，官网明确提出需要使用 Java 11+，否则会报如下错误（我开始使用的 jdk1.8）：

```bash
java.lang.UnsupportedClassVersionError: controllers/routes has been compiled by a more recent version of the Java Runtime (class file version 55.0), this version of the Java Runtime only recognizes class file versions up to 52.0
```

application.conf ，只需要修改2行，把默认的 kafka-manager-zookeeper 改成虚拟机 IP 地址即可。

```bash
# Copyright 2015 Yahoo Inc. Licensed under the Apache License, Version 2.0
# See accompanying LICENSE file.

# This is the main configuration file for the application.
# ~~~~~

# Secret key
# ~~~~~
# The secret key is used to secure cryptographics functions.
# If you deploy your application to several instances be sure to use the same key!
play.crypto.secret="^<csmm5Fx4d=r2HEX8pelM3iBkFVv?k[mc;IZE<_Qoq8EkX_/7@Zt6dP05Pzea3U"
play.crypto.secret=${?APPLICATION_SECRET}
play.http.session.maxAge="1h"

# The application languages
# ~~~~~
play.i18n.langs=["en"]

play.http.requestHandler = "play.http.DefaultHttpRequestHandler"
play.http.context = "/"
play.application.loader=loader.KafkaManagerLoader

# Settings prefixed with 'kafka-manager.' will be deprecated, use 'cmak.' instead.
# https://github.com/yahoo/CMAK/issues/713
kafka-manager.zkhosts="192.168.56.95:2181"
kafka-manager.zkhosts=${?ZK_HOSTS}
cmak.zkhosts="192.168.56.95:2181"
cmak.zkhosts=${?ZK_HOSTS}

pinned-dispatcher.type="PinnedDispatcher"
pinned-dispatcher.executor="thread-pool-executor"
application.features=["KMClusterManagerFeature","KMTopicManagerFeature","KMPreferredReplicaElectionFeature","KMReassignPartitionsFeature", "KMScheduleLeaderElectionFeature"]

akka {
  loggers = ["akka.event.slf4j.Slf4jLogger"]
  loglevel = "INFO"
}

akka.logger-startup-timeout = 60s

basicAuthentication.enabled=false
basicAuthentication.enabled=${?KAFKA_MANAGER_AUTH_ENABLED}

basicAuthentication.ldap.enabled=false
basicAuthentication.ldap.enabled=${?KAFKA_MANAGER_LDAP_ENABLED}
basicAuthentication.ldap.server=""
basicAuthentication.ldap.server=${?KAFKA_MANAGER_LDAP_SERVER}
basicAuthentication.ldap.port=389
basicAuthentication.ldap.port=${?KAFKA_MANAGER_LDAP_PORT}
basicAuthentication.ldap.username=""
basicAuthentication.ldap.username=${?KAFKA_MANAGER_LDAP_USERNAME}
basicAuthentication.ldap.password=""
basicAuthentication.ldap.password=${?KAFKA_MANAGER_LDAP_PASSWORD}
basicAuthentication.ldap.search-base-dn=""
basicAuthentication.ldap.search-base-dn=${?KAFKA_MANAGER_LDAP_SEARCH_BASE_DN}
basicAuthentication.ldap.search-filter="(uid=$capturedLogin$)"
basicAuthentication.ldap.search-filter=${?KAFKA_MANAGER_LDAP_SEARCH_FILTER}
basicAuthentication.ldap.group-filter=""
basicAuthentication.ldap.group-filter=${?KAFKA_MANAGER_LDAP_GROUP_FILTER}
basicAuthentication.ldap.connection-pool-size=10
basicAuthentication.ldap.connection-pool-size=${?KAFKA_MANAGER_LDAP_CONNECTION_POOL_SIZE}
basicAuthentication.ldap.ssl=false
basicAuthentication.ldap.ssl=${?KAFKA_MANAGER_LDAP_SSL}
basicAuthentication.ldap.ssl-trust-all=false
basicAuthentication.ldap.ssl-trust-all=${?KAFKA_MANAGER_LDAP_SSL_TRUST_ALL}

basicAuthentication.username="admin"
basicAuthentication.username=${?KAFKA_MANAGER_USERNAME}
basicAuthentication.password="password"
basicAuthentication.password=${?KAFKA_MANAGER_PASSWORD}

basicAuthentication.realm="Kafka-Manager"
basicAuthentication.excluded=["/api/health"] # ping the health of your instance without authentification

kafka-manager.consumer.properties.file=${?CONSUMER_PROPERTIES_FILE}
```

在 cmd 黑框里，使用 cmak.bat 启动总是报错，我使用 git-bash 启动。

```bash
cd cmak-3.0.0.5/bin
./cmak
```

启动后，可以在日志文件中看到 cmak 的 web 默认端口号是 9000，打开浏览器，http://localhost:9000/。

添加集群，需要输入的内容：

- 输入集群名称（随意输入）
- zk 地址（多个使用 IP:PORT，逗号分隔）
- 勾选 Enable JMX Polling 复选框
- JMX Auth Username/JMX Auth Password 默认为 username/password
- 其他都默认

直接 save。



3.创建 topic，并性能测试

```bash
[root@master /]# kafka-topics.sh --zookeeper 192.168.56.95:2181 --list
__consumer_offsets
[root@master /]# kafka-topics.sh --zookeeper 192.168.56.95:2181 --create --topic test32 --partitions 3 --replication-factor 2
Created topic test32.
[root@master /]# kafka-topics.sh --zookeeper 192.168.56.95:2181 --describe --topic test32
Topic: test32   PartitionCount: 3       ReplicationFactor: 2    Configs:
        Topic: test32   Partition: 0    Leader: 1       Replicas: 1,2   Isr: 1,2
        Topic: test32   Partition: 1    Leader: 2       Replicas: 2,0   Isr: 2,0
        Topic: test32   Partition: 2    Leader: 0       Replicas: 0,1   Isr: 0,1
[root@master /]# kafka-topics.sh --zookeeper 192.168.56.95:2181 --list
__consumer_offsets
test32
```

创建 topic test32 后，可以在 cmak web 页面看到详情。

```bash
[root@master /]# kafka-producer-perf-test.sh --topic test32 --num-records 10000 --record-size 1000 --throughput 10000 --producer-props bootstrap.servers=192.168.56.95:9000,192.168.56.95:9001,192.168.56.95:9002
10000 records sent, 3065.603924 records/sec (2.92 MB/sec), 1298.76 ms avg latency, 2229.00 ms max latency, 1351 ms 50th, 2177 ms 95th, 2217 ms 99th, 2229 ms 99.9th.

[root@master /]# kafka-producer-perf-test.sh --topic test32 --num-records 50000 --record-size 1000 --throughput 10000 --producer-props bootstrap.servers=192.168.56.95:9000,192.168.56.95:9001,192.168.56.95:9002
28993 records sent, 5766.3 records/sec (5.50 MB/sec), 1465.9 ms avg latency, 2375.0 ms max latency.
50000 records sent, 6470.816617 records/sec (6.17 MB/sec), 1812.94 ms avg latency, 3165.00 ms max latency, 1801 ms 50th, 2914 ms 95th, 3093 ms 99th, 3154 ms 99.9th.
```

测试性能后，在 cmak 页面看不到具体的 metrics，不知道是不是跟我偷懒启动 JMX 有关系，官网 issue，https://github.com/yahoo/CMAK/issues/221，后续再处理吧。

