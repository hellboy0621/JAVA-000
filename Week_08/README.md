class15

2、（必做）设计对前面的订单表数据进行水平分库分表，拆分2个库，每个库16张表。
并在新结构在演示常见的增删改查操作。代码、sql 和配置文件，上传到 Github。

订单表拆分2个库，分别为ds_0和ds_1，分库规则使用order_id对2取模。

每个库，拆分16张表，表名为t_order_0到t_order_15，分表规则使用order_id先除以2，得到整数部分，再对16取模。

使用shardingsphere-proxy进行分片及管理。

配置文件 config-sharding.yaml 内容如下

```yaml

schemaName: sharding_db

dataSourceCommon:
  username: root
  password:
  connectionTimeoutMilliseconds: 30000
  idleTimeoutMilliseconds: 60000
  maxLifetimeMilliseconds: 1800000
  maxPoolSize: 50
  minPoolSize: 1
  maintenanceIntervalMilliseconds: 30000

dataSources:
  ds_0:
    url: jdbc:mysql://127.0.0.1:3305/ds_0?serverTimezone=UTC&useSSL=false
  ds_1:
    url: jdbc:mysql://127.0.0.1:3305/ds_1?serverTimezone=UTC&useSSL=false

rules:
- !SHARDING
 tables:
   t_order:
     actualDataNodes: ds_${0..1}.t_order_${0..16}
     tableStrategy:
       standard:
         shardingColumn: order_id
         shardingAlgorithmName: t_order_inline
     keyGenerateStrategy:
       column: order_id
       keyGeneratorName: snowflake
 defaultDatabaseStrategy:
   standard:
     shardingColumn: order_id
     shardingAlgorithmName: database_inline
 defaultTableStrategy:
   none:
 
 shardingAlgorithms:
   database_inline:
     type: INLINE
     props:
       algorithm-expression: ds_${order_id % 2}
       allow-range-query-with-inline-sharding: true
   t_order_inline:
     type: INLINE
     props:
       algorithm-expression: t_order_${((order_id/2) as int) % 16}
       allow-range-query-with-inline-sharding: true
 
 keyGenerators:
   snowflake:
     type: SNOWFLAKE
     props:
       worker-id: 123
```

server.yaml 内容如下

```yaml
authentication:
  users:
    root:
      password: 123456

props:
  max-connections-size-per-query: 1
  acceptor-size: 16  # The default value is available processors count * 2.
  executor-size: 16  # Infinite by default.
  proxy-frontend-flush-threshold: 128  # The default value is 128.
   # LOCAL: Proxy will run with LOCAL transaction.
   # XA: Proxy will run with XA transaction.
   # BASE: Proxy will run with B.A.S.E transaction.
  proxy-transaction-type: LOCAL
  proxy-opentracing-enabled: false
  proxy-hint-enabled: false
  query-with-cipher-column: true
  sql-show: true
  check-table-metadata-enabled: false
```

在启动SS-Proxy时，加上端口号3310

```bash
start.bat 3310
```

通过mysql客户端，连接上SS-Proxy

```bash
mysql -uroot -p123456 -P3310
```

建表

```mysql
use sharding_db;
CREATE TABLE IF NOT EXISTS t_order(
	order_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '订单自增主键',
    user_id INT NOT NULL COMMENT '用户ID',
    discount INT DEFAULT 0 COMMENT '总优惠，以分为单位',
    total_price INT DEFAULT 0 COMMENT '订单支付总价，以分为单位',
    remark VARCHAR(100) DEFAULT NULL COMMENT '备注',
    create_time BIGINT NOT NULL COMMENT '创建时间', 
    update_time BIGINT NOT NULL COMMENT '更新时间', 
    PRIMARY KEY (order_id)
) comment '订单表';
```

连上本来的数据库，查看ds_0/ds_1数据库下，就自动把表建好了

```bash
mysql> use ds_1
Database changed
mysql> show tables;
+----------------+
| Tables_in_ds_1 |
+----------------+
| t_order_0      |
| t_order_1      |
| t_order_10     |
| t_order_11     |
| t_order_12     |
| t_order_13     |
| t_order_14     |
| t_order_15     |
| t_order_16     |
| t_order_2      |
| t_order_3      |
| t_order_4      |
| t_order_5      |
| t_order_6      |
| t_order_7      |
| t_order_8      |
| t_order_9      |
+----------------+
17 rows in set (0.00 sec)

mysql> use ds_0
Database changed
mysql> show tables;
+----------------+
| Tables_in_ds_0 |
+----------------+
| t_order_0      |
| t_order_1      |
| t_order_10     |
| t_order_11     |
| t_order_12     |
| t_order_13     |
| t_order_14     |
| t_order_15     |
| t_order_16     |
| t_order_2      |
| t_order_3      |
| t_order_4      |
| t_order_5      |
| t_order_6      |
| t_order_7      |
| t_order_8      |
| t_order_9      |
+----------------+
17 rows in set (0.00 sec)
```

写一个最简单的springboot项目，数据源写成SS-Proxy即可，项目见ss-proxy文件夹，增删改查详见测试类ss-proxy\src\test\java\com\transformer\dao\OrderMapperTest.java

测试类中使用的order_id为2，在SS-Proxy控制台中可以看到如下日志，说明order_id=2这条数据被分配到了ds_0:t_order_1这个库表里了

```bash
[INFO ] 09:59:46.913 [ShardingSphere-Command-4] ShardingSphere-SQL - Logic SQL: delete from t_order where order_id = 2
[INFO ] 09:59:46.913 [ShardingSphere-Command-4] ShardingSphere-SQL - SQLStatement: MySQLDeleteStatement(orderBy=Optional.empty, limit=Optional.empty)
[INFO ] 09:59:46.914 [ShardingSphere-Command-4] ShardingSphere-SQL - Actual SQL: ds_0 ::: delete from t_order_1 where order_id = 2
```



***



class16

2、（必做）基于hmily TCC或ShardingSphere的Atomikos XA实现一个简单的分布式
事务应用demo（二选一），提交到github。

