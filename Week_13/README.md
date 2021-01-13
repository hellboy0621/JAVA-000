class24

1、（必做）搭建ActiveMQ服务，基于JMS，写代码分别实现对于queue和topic的消息生产和消费，代码提交到github。

详见 activemq 文件夹。

其中，ActivemqQueueDemo 对于 Queue 的消息生产和消费，ActivemqTopicDemo 对于 Topic 的消息生产和消费。

ActiveMQServer 作为项目内嵌的 ActiveMQ，启动后绑定到 tcp://localhost:61615，使用上面两个 Demo 修改连接地址后，可以成功生产和消费数据。

在pom文件中需要额外引入

```xml
<dependency>
    <groupId>org.apache.activemq</groupId>
    <artifactId>activemq-all</artifactId>
    <version>5.16.0</version>
</dependency>
<dependency>
	<groupId>com.fasterxml.jackson.core</groupId>
	<artifactId>jackson-databind</artifactId>
	<version>2.11.3</version>
</dependency>
```



搭建ActiveMQ服务需要注意的点：

在虚拟机中运行，因为没有 Java 环境，报错如下。

```bash
[root@master /]# cd /usr/local/apache-activemq-5.16.0/bin
[root@master bin]# ./activemq start
INFO: Loading '/usr/local/apache-activemq-5.16.0//bin/env'
ERROR: Configuration variable JAVA_HOME or JAVACMD is not defined correctly.
       (JAVA_HOME='', JAVACMD='java')
```

下载 Oracle Jdk，配置好 JAVA_HOME 及 PATH，再次启动

```bash
[root@master bin]# ./activemq start
INFO: Loading '/usr/local/apache-activemq-5.16.0//bin/env'
INFO: Using java '/usr/local/jdk1.8.0_271/bin/java'
INFO: Starting - inspect logfiles specified in logging.properties and log4j.properties to get details
INFO: pidfile created : '/usr/local/apache-activemq-5.16.0//data/activemq.pid' (pid '1172')
```

此时虽然正常启动了，但是在宿主机访问 http://192.168.56.95:8161/admin/index.jsp 时报错，连接不上。

需要修改配置文件 jetty.xml，把 127.0.0.1 改成 0.0.0.0。   

```xml
<bean id="jettyPort" class="org.apache.activemq.web.WebConsolePort" init-method="start">
         <!-- the default port number for the web console -->
    <property name="host" value="127.0.0.1"/>
    <property name="port" value="8161"/>
</bean>
```

再次在宿主机内，即可访问控制页面。



class25

1、（必做）搭建一个3节点Kafka集群，测试功能和性能；实现spring kafka下对kafka集群的操作，将代码提交到github。

1.（可选）下载并解压 Zookeeper（当然，也可以使用 Kafka 内置的 zookeeper）

```bash
[root@master /]# cd /usr/local/
[root@master local]# wget https://apache.mirror.colo-serv.net/zookeeper/zookeeper-3.6.2/apache-zookeeper-3.6.2-bin.tar.gz
[root@master local]# tar zxvf apache-zookeeper-3.6.2-bin.tar.gz
```

复制并修改配置文件

```bash
[root@master conf]# cp zoo_sample.cfg zoo.cfg
[root@master conf]# vim zoo.cfg

# 修改数据存放路径，也可以不修改
dataDir=/opt/zookeeper

[root@master conf]# cd /opt/
[root@master opt]# mkdir zookeeper
```

启动 zookeeper

```bash
[root@master opt]# cd /usr/local/apache-zookeeper-3.6.2-bin/
[root@master apache-zookeeper-3.6.2-bin]# ./bin/zkServer.sh
ZooKeeper JMX enabled by default
Using config: /usr/local/apache-zookeeper-3.6.2-bin/bin/../conf/zoo.cfg
Usage: ./bin/zkServer.sh [--config <conf-dir>] {start|start-foreground|stop|version|restart|status|print-cmd}
[root@master apache-zookeeper-3.6.2-bin]# ./bin/zkServer.sh start
ZooKeeper JMX enabled by default
Using config: /usr/local/apache-zookeeper-3.6.2-bin/bin/../conf/zoo.cfg
Starting zookeeper ... STARTED
```

使用 jps 命令可以看到 QuorumPeerMain，已经正常启动

```bash
[root@master apache-zookeeper-3.6.2-bin]# jps
1056 Jps
998 QuorumPeerMain
```

可以使用 ZooInspector 连接上刚刚启动好的 Zookeeper。

2.下载并解压缩 Kafka

```bash
[root@master /]# cd /usr/local/
[root@master local]# wget https://httpd-mirror.sergal.org/apache/kafka/2.7.0/kafka_2.13-2.7.0.tgz
[root@master local]# tar zxvf kafka_2.13-2.7.0.tgz
```

Kafka 内置的 zookeeper 启动命令

```bash
[root@master kafka_2.13-2.7.0]# nohup ./bin/zookeeper-server-start.sh ./config/zookeeper.properties &
```

单机安装部署，修改配置文件

```bash
[root@master local]# cd kafka_2.13-2.7.0/config
[root@master config]# vim server.properties
# 增加一句配置
listeners=PLAINTEXT://192.168.56.95:9092
```

3.启动单节点的 Kafka

```bash
[root@master kafka_2.13-2.7.0]# nohup ./bin/kafka-server-start.sh config/server.properties &
[root@master kafka_2.13-2.7.0]# jps
998 QuorumPeerMain
1549 Jps
1119 Kafka
```

如果重启时不能正常启动，首先把 zookeeper 除了自带的节点都删除，其次还需要把文件清空，如下命令可以查看文件路径

```bash
[root@master kafka_2.13-2.7.0]# cat ./conf/server.properties | grep log.dir
log.dirs=/tmp/kafka-logs
```

启动后，查看 topic 列表，现在是空的。

```bash
[root@master kafka_2.13-2.7.0]# bin/kafka-topics.sh --zookeeper localhost:2181 --list
```

创建一个 topic，testk 4个partitions，1个副本；但是创建的 test3-2失败了，因为目前只有1个 broker，创建2个副本是没有意义的。（副本是用来容错的。）

```bash
[root@master kafka_2.13-2.7.0]# bin/kafka-topics.sh --zookeeper localhost:2181 --create --topic testk --partitions 4 --replication-factor 1
Created topic testk.

[root@master kafka_2.13-2.7.0]# bin/kafka-topics.sh --zookeeper localhost:2181 --create --topic test3-2 --partitions 3 --replication-factor 2
Error while executing topic command : Replication factor: 2 larger than available brokers: 1.
[2021-01-13 15:25:08,888] ERROR org.apache.kafka.common.errors.InvalidReplicationFactorException: Replication factor: 2 larger than available brokers: 1.
 (kafka.admin.TopicCommand$)
```

查看刚创建的 testk topic

```bash
[root@master kafka_2.13-2.7.0]# bin/kafka-topics.sh --zookeeper localhost:2181 --describe --topic testk
Topic: testk    PartitionCount: 4       ReplicationFactor: 1    Configs:
        Topic: testk    Partition: 0    Leader: 0       Replicas: 0     Isr: 0
        Topic: testk    Partition: 1    Leader: 0       Replicas: 0     Isr: 0
        Topic: testk    Partition: 2    Leader: 0       Replicas: 0     Isr: 0
        Topic: testk    Partition: 3    Leader: 0       Replicas: 0     Isr: 0
```

再次查看列表

```bash
[root@master kafka_2.13-2.7.0]# bin/kafka-topics.sh --zookeeper localhost:2181 --list
testk
```

在一个窗口中，创建 testk 这个 topic 1个消费者 consumer

```bash
[root@master kafka_2.13-2.7.0]# bin/kafka-console-consumer.sh --bootstrap-server 192.168.56.95:9092 --from-beginning --topic testk


```

在另一个窗口中，创建 testk 这个 topic 的1个 producer

```bash
[root@master kafka_2.13-2.7.0]# bin/kafka-console-producer.sh --bootstrap-server 192.168.56.95:9092 --topic testk
>hello
>this is a message from producer.
>
```

再切回 consumer 窗口，可以看到消费者已经把消息打印出来了

```bash
[root@master kafka_2.13-2.7.0]# bin/kafka-console-consumer.sh --bootstrap-server 192.168.56.95:9092 --from-beginning --topic testk
hello
this is a message from producer.

```

4.单节点性能测试

10w 条数据，每条数据大小1000字节，流控每秒2000条数据

```bash
[root@master kafka_2.13-2.7.0]# bin/kafka-producer-perf-test.sh --topic testk --num-records 100000 --record-size 1000 --throughput 2000 --producer-props bootstrap.servers=192.168.56.95:9092
9986 records sent, 1997.2 records/sec (1.90 MB/sec), 29.4 ms avg latency, 611.0 ms max latency.
10010 records sent, 2001.6 records/sec (1.91 MB/sec), 2.3 ms avg latency, 60.0 ms max latency.
10006 records sent, 2000.4 records/sec (1.91 MB/sec), 1.4 ms avg latency, 61.0 ms max latency.
9980 records sent, 1992.4 records/sec (1.90 MB/sec), 3.3 ms avg latency, 134.0 ms max latency.
10019 records sent, 2001.0 records/sec (1.91 MB/sec), 3.0 ms avg latency, 99.0 ms max latency.
10037 records sent, 2007.4 records/sec (1.91 MB/sec), 2.5 ms avg latency, 73.0 ms max latency.
10004 records sent, 2000.4 records/sec (1.91 MB/sec), 0.9 ms avg latency, 46.0 ms max latency.
10004 records sent, 2000.4 records/sec (1.91 MB/sec), 0.7 ms avg latency, 51.0 ms max latency.
10004 records sent, 2000.4 records/sec (1.91 MB/sec), 0.5 ms avg latency, 28.0 ms max latency.
9950 records sent, 1975.8 records/sec (1.88 MB/sec), 1.1 ms avg latency, 62.0 ms max latency.
100000 records sent, 1984.560122 records/sec (1.89 MB/sec), 4.50 ms avg latency, 611.00 ms max latency, 1 ms 50th, 11 ms 95th, 163 ms 99th, 262 ms 99.9th.
```

把流控改为每秒20w，总数改为100w，虚拟机居然慢成狗了。

```bash
[root@master kafka_2.13-2.7.0]# bin/kafka-producer-perf-test.sh --topic testk --num-records 1000000 --record-size 1000 --throughput 200000 --producer-props bootstrap.servers=192.168.56.95:9092
25537 records sent, 4880.9 records/sec (4.65 MB/sec), 969.1 ms avg latency, 3775.0 ms max latency.
26176 records sent, 5124.5 records/sec (4.89 MB/sec), 6203.1 ms avg latency, 8154.0 ms max latency.
71360 records sent, 14272.0 records/sec (13.61 MB/sec), 3353.9 ms avg latency, 8411.0 ms max latency.
90112 records sent, 18018.8 records/sec (17.18 MB/sec), 1882.8 ms avg latency, 2654.0 ms max latency.
47232 records sent, 9351.0 records/sec (8.92 MB/sec), 3431.0 ms avg latency, 4495.0 ms max latency.
45120 records sent, 8700.3 records/sec (8.30 MB/sec), 3592.5 ms avg latency, 4859.0 ms max latency.
22976 records sent, 4193.5 records/sec (4.00 MB/sec), 3546.0 ms avg latency, 7275.0 ms max latency.
47040 records sent, 9406.1 records/sec (8.97 MB/sec), 5730.7 ms avg latency, 8179.0 ms max latency.
68608 records sent, 13566.9 records/sec (12.94 MB/sec), 2559.6 ms avg latency, 4285.0 ms max latency.
960 records sent, 182.7 records/sec (0.17 MB/sec), 5686.1 ms avg latency, 8915.0 ms max latency.
6208 records sent, 1133.3 records/sec (1.08 MB/sec), 10142.0 ms avg latency, 14487.0 ms max latency.
31936 records sent, 6368.1 records/sec (6.07 MB/sec), 15463.7 ms avg latency, 18549.0 ms max latency.
117376 records sent, 23312.0 records/sec (22.23 MB/sec), 1813.2 ms avg latency, 18309.0 ms max latency.
69440 records sent, 13888.0 records/sec (13.24 MB/sec), 2170.3 ms avg latency, 3287.0 ms max latency.
59840 records sent, 11903.7 records/sec (11.35 MB/sec), 2661.1 ms avg latency, 3858.0 ms max latency.
50048 records sent, 9582.2 records/sec (9.14 MB/sec), 3564.4 ms avg latency, 5779.0 ms max latency.
87232 records sent, 17425.5 records/sec (16.62 MB/sec), 2161.7 ms avg latency, 4563.0 ms max latency.
88896 records sent, 17645.1 records/sec (16.83 MB/sec), 1710.0 ms avg latency, 2910.0 ms max latency.
1000000 records sent, 10039.152696 records/sec (9.57 MB/sec), 3163.25 ms avg latency, 18549.00 ms max latency, 2342 ms 50th, 7916 ms 95th, 17895 ms 99th, 18427 ms 99.9th.
```

换成 Win10 后，性能有所改观。

```bash
D:\develop\kafka_2.13-2.7.0\bin\windows>kafka-producer-perf-test.bat --topic testk --num-records 1000000 --record-size 1000 --throughput 200000 --producer-props bootstrap.servers=localhost:9092
226433 records sent, 45277.5 records/sec (43.18 MB/sec), 605.0 ms avg latency, 904.0 ms max latency.
486656 records sent, 97331.2 records/sec (92.82 MB/sec), 351.2 ms avg latency, 759.0 ms max latency.
1000000 records sent, 81247.968801 records/sec (77.48 MB/sec), 380.34 ms avg latency, 904.00 ms max latency, 327 ms 50th, 708 ms 95th, 803 ms 99th, 881 ms 99.9th.

D:\develop\kafka_2.13-2.7.0\bin\windows>kafka-producer-perf-test.bat --topic testk --num-records 1000000 --record-size 1000 --throughput 1000000 --producer-props bootstrap.servers=localhost:9092
250113 records sent, 50022.6 records/sec (47.71 MB/sec), 519.5 ms avg latency, 657.0 ms max latency.
416256 records sent, 83251.2 records/sec (79.39 MB/sec), 401.0 ms avg latency, 664.0 ms max latency.
1000000 records sent, 76458.444835 records/sec (72.92 MB/sec), 396.91 ms avg latency, 664.00 ms max latency, 373 ms 50th, 606 ms 95th, 639 ms 99th, 655 ms 99.9th.
```

再重新创建只有1个 partition 的 testk1，并压测，性能比4个的 partition 慢了。

```bash
D:\develop\kafka_2.13-2.7.0\bin\windows>kafka-topics.bat --zookeeper localhost:2181 --create --topic testk1 --partitions 1 --replication-factor 1
Created topic testk1.

D:\develop\kafka_2.13-2.7.0\bin\windows>kafka-producer-perf-test.bat --topic testk1 --num-records 1000000 --record-size 1000 --throughput 1000000 --producer-props bootstrap.servers=localhost:9092
234481 records sent, 46896.2 records/sec (44.72 MB/sec), 598.4 ms avg latency, 705.0 ms max latency.
380528 records sent, 76105.6 records/sec (72.58 MB/sec), 439.2 ms avg latency, 625.0 ms max latency.
1000000 records sent, 67141.130657 records/sec (64.03 MB/sec), 467.74 ms avg latency, 705.00 ms max latency, 429 ms 50th, 672 ms 95th, 694 ms 99th, 703 ms 99.9th.
```

再重新创建有16个 partition 的 testk16，并压测，性能飙升，总数由 100w 改为 400w，性能又提升了一些。

```bash
D:\develop\kafka_2.13-2.7.0\bin\windows>kafka-topics.bat --zookeeper localhost:2181 --create --topic testk16 --partitions 16 --replication-factor 1
Created topic testk16.

D:\develop\kafka_2.13-2.7.0\bin\windows>kafka-producer-perf-test.bat --topic testk16 --num-records 1000000 --record-size 1000 --throughput 1000000 --producer-props bootstrap.servers=localhost:9092
736025 records sent, 147175.6 records/sec (140.36 MB/sec), 200.0 ms avg latency, 552.0 ms max latency.
1000000 records sent, 151446.312282 records/sec (144.43 MB/sec), 198.63 ms avg latency, 552.00 ms max latency, 188 ms 50th, 339 ms 95th, 450 ms 99th, 531 ms 99.9th.

D:\develop\kafka_2.13-2.7.0\bin\windows>kafka-producer-perf-test.bat --topic testk16 --num-records 4000000 --record-size 1000 --throughput 1000000 --producer-props bootstrap.servers=localhost:9092
691556 records sent, 138283.5 records/sec (131.88 MB/sec), 212.8 ms avg latency, 432.0 ms max latency.
845392 records sent, 168740.9 records/sec (160.92 MB/sec), 193.1 ms avg latency, 458.0 ms max latency.
877008 records sent, 175401.6 records/sec (167.28 MB/sec), 187.3 ms avg latency, 442.0 ms max latency.
904364 records sent, 180836.6 records/sec (172.46 MB/sec), 180.6 ms avg latency, 503.0 ms max latency.
4000000 records sent, 167420.056923 records/sec (159.66 MB/sec), 191.35 ms avg latency, 516.00 ms max latency, 180 ms 50th, 386 ms 95th, 451 ms 99th, 494 ms 99.9th.
```

消费者性能测试

```bash
D:\develop\kafka_2.13-2.7.0\bin\windows>kafka-consumer-perf-test.bat --bootstrap-server localhost:9092 --topic testk --fetch-size 1048576 --messages 100000 --threads 1
WARNING: option [threads] and [num-fetch-threads] have been deprecated and will be ignored by the test
start.time, end.time, data.consumed.in.MB, MB.sec, data.consumed.in.nMsg, nMsg.sec, rebalance.time.ms, fetch.time.ms, fetch.MB.sec, fetch.nMsg.sec
2021-01-14 00:16:22:058, 2021-01-14 00:16:24:481, 95.6802, 39.4883, 100328, 41406.5208, 1610554583317, -1610554580894, -0.0000, -0.0001

D:\develop\kafka_2.13-2.7.0\bin\windows>kafka-consumer-perf-test.bat --bootstrap-server localhost:9092 --topic testk --fetch-size 1048576 --messages 1000000 --threads 1
WARNING: option [threads] and [num-fetch-threads] have been deprecated and will be ignored by the test
start.time, end.time, data.consumed.in.MB, MB.sec, data.consumed.in.nMsg, nMsg.sec, rebalance.time.ms, fetch.time.ms, fetch.MB.sec, fetch.nMsg.sec
2021-01-14 00:18:58:958, 2021-01-14 00:19:08:840, 954.0825, 96.5475, 1000428, 101237.4013, 1610554739276, -1610554729394, -0.0000, -0.0006

D:\develop\kafka_2.13-2.7.0\bin\windows>kafka-consumer-perf-test.bat --bootstrap-server localhost:9092 --topic testk16 --fetch-size 1048576 --messages 1000000 --threads 4
WARNING: option [threads] and [num-fetch-threads] have been deprecated and will be ignored by the test
start.time, end.time, data.consumed.in.MB, MB.sec, data.consumed.in.nMsg, nMsg.sec, rebalance.time.ms, fetch.time.ms, fetch.MB.sec, fetch.nMsg.sec
2021-01-14 00:20:16:504, 2021-01-14 00:20:19:284, 953.8574, 343.1142, 1000192, 359781.2950, 1610554816855, -1610554814075, -0.0000, -0.0006
```

