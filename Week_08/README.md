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

使用Hmily官方SpringCloud例子改造而来。

代码详见springcloud-hmily文件夹。

1.建库建表

```mysql
CREATE DATABASE IF NOT EXISTS `hmily_account` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin ;
USE `hmily_account`;
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(128) NOT NULL,
  `balance` decimal(10,0) NOT NULL COMMENT '用户余额',
  `freeze_amount` decimal(10,0) NOT NULL COMMENT '冻结金额，扣款暂存余额',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
insert  into `account`(`id`,`user_id`,`balance`,`freeze_amount`,`create_time`,`update_time`) values(1,'10000', 10000000,0,'2017-09-18 14:54:22',NULL);

CREATE DATABASE IF NOT EXISTS `hmily_stock` DEFAULT CHARACTER SET utf8mb4;
USE `hmily_stock`;
DROP TABLE IF EXISTS `inventory`;
CREATE TABLE `inventory` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_id` VARCHAR(128) NOT NULL,
  `total_inventory` int(10) NOT NULL COMMENT '总库存',
  `lock_inventory` int(10) NOT NULL COMMENT '锁定库存',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
insert  into `inventory`(`id`,`product_id`,`total_inventory`,`lock_inventory`) values(1,'1',10000000,0);

CREATE DATABASE IF NOT EXISTS `hmily_order` DEFAULT CHARACTER SET utf8mb4;
USE `hmily_order`;
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime NOT NULL,
  `number` varchar(20) COLLATE utf8mb4_bin NOT NULL,
  `status` tinyint(4) NOT NULL,
  `product_id` varchar(128) NOT NULL,
  `total_amount` decimal(10,0) NOT NULL,
  `count` int(4) NOT NULL,
  `user_id` varchar(128) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
```

2.引入hmily依赖，在每个微服务项目中增加

```xml
<!-- 现在父项目中增加依赖管理，这里有个坑，不排除slf4j-log4j12，会报日志错误 -->
<dependency>
    <groupId>org.dromara</groupId>
    <artifactId>hmily-spring-boot-starter-springcloud</artifactId>
    <version>2.1.1</version>
    <exclusions>
        <exclusion>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-log4j12</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<!-- 然后在所有需要使用的子项目中引入依赖即可 -->
<dependency>
    <groupId>org.dromara</groupId>
    <artifactId>hmily-spring-boot-starter-springcloud</artifactId>
</dependency>
```

3.配置文件

这里也有一个坑，metrics的端口号要保证几个项目的都不一样，否则报端口地址被占用，项目是启动不起来的。

```yaml
hmily:
  server:
    configMode: local
    appName: account-sc
  config:
    appName: account-sc
    serializer: kryo
    contextTransmittalMode: threadLocal
    scheduledThreadMax: 16
    scheduledRecoveryDelay: 60
    scheduledCleanDelay: 60
    scheduledPhyDeletedDelay: 600
    scheduledInitDelay: 30
    recoverDelayTime: 60
    cleanDelayTime: 180
    limit: 200
    retryMax: 10
    bufferSize: 8192
    consumerThreads: 16
    asyncRepository: true
    autoSql: true
    phyDeleted: true
    storeDays: 3
    repository: mysql

repository:
  database:
    driverClassName: com.mysql.jdbc.Driver
    url : jdbc:mysql://127.0.0.1:3306/hmily?useUnicode=true&characterEncoding=utf8
    username: root
    password:
    maxActive: 20
    minIdle: 10
    connectionTimeout: 30000
    idleTimeout: 600000
    maxLifetime: 1800000

metrics:
  metricsName: prometheus
  host:
  port: 9081
  async: true
  threadCount : 16
  jmxConfig:
```

4.在需要参与分布式事务的Feign接口方法上增加@Hmily接口标识

```java
/**
 * 用户账户付款.
 *
 * @param accountDO 实体类
 * @return true 成功
 */
@RequestMapping("/account-service/account/payment")
// 该注解为hmily分布式事务接口标识，表示该接口参与hmily分布式事务
@Hmily
Boolean payment(@RequestBody AccountDTO accountDO);

/**
 * 库存扣减.
 *
 * @param inventoryDTO 实体对象
 * @return true 成功
 */
@RequestMapping("/inventory-service/inventory/decrease")
// 该注解为hmily分布式事务接口标识，表示该接口参与hmily分布式事务
@Hmily
Boolean decrease(@RequestBody InventoryDTO inventoryDTO);
```

5.把具体的业务流程拆解为两阶段三部分，并使用@HmilyTCC注解，写明Confirm和Cancel需要执行的方法名称。

如：用户账户付款方法，拆分为

(1)Try 阶段：将账户余额减去扣款金额，将冻结金额加上扣款金额

(2)Confirm 阶段：所有步骤都成功了，将冻结金额释放，此时从外部整体看，就是账户余额减了一个扣款金额。

(3)Cancel 阶段：如果有步骤失败了，将Try对数据的修改恢复原样，把账户余额加上扣款金额，冻结金额减去扣款金额，此时从外部整体看，就是账户没有任何变化

```java
@Override
@HmilyTCC(confirmMethod = "confirmPayment", cancelMethod = "cancelPayment")
public boolean payment(AccountDTO accountDTO) {
    accountMapper.update(accountDTO);
    return Boolean.TRUE;
}

public boolean confirmPayment(AccountDTO accountDTO) {
    return accountMapper.confirm(accountDTO) > 0;
}

public boolean cancelPayment(AccountDTO accountDTO) {
    return accountMapper.cancel(accountDTO) > 0;
}
```

6.启动项目

7.测试正常流程

在swagger页面（http://localhost:8090/swagger-ui.html）调用正常执行接口，调用http://localhost:8090/order/orderPay?count=1&amount=1，成功后到数据库查看3张表数据正常。

```bash
mysql> select * from hmily_account.account;
+----+---------+---------+---------------+---------------------+---------------------+
| id | user_id | balance | freeze_amount | create_time         | update_time         |
+----+---------+---------+---------------+---------------------+---------------------+
|  1 | 10000   | 9999999 |             0 | 2017-09-18 14:54:22 | 2020-12-10 19:04:11 |
+----+---------+---------+---------------+---------------------+---------------------+
1 row in set (0.00 sec)

mysql> select * from hmily_order.order;
+----+---------------------+---------------------+--------+------------+--------------+-------+---------+
| id | create_time         | number              | status | product_id | total_amount | count | user_id |
+----+---------------------+---------------------+--------+------------+--------------+-------+---------+
| 11 | 2020-12-10 19:04:04 | 5890193653420609536 |      4 | 1          |            1 |     1 | 10000   |
+----+---------------------+---------------------+--------+------------+--------------+-------+---------+
1 row in set (0.01 sec)

mysql> select * from hmily_stock.inventory;
+----+------------+-----------------+----------------+
| id | product_id | total_inventory | lock_inventory |
+----+------------+-----------------+----------------+
|  1 | 1          |         9999999 |              0 |
+----+------------+-----------------+----------------+
1 row in set (0.00 sec)
```

8.模拟异常流程

调用模拟 Try 阶段异常接口http://localhost:8090/order/mockInventoryWithTryException?count=1&amount=1

错误响应如下

```jso
{
  "timestamp": "2020-12-10T11:54:33.841+0000",
  "status": 500,
  "error": "Internal Server Error",
  "message": "status 500 reading InventoryClient#mockWithTryException(InventoryDTO); content:\n{\"timestamp\":\"2020-12-10T11:54:33.784+0000\",\"status\":500,\"error\":\"Internal Server Error\",\"message\":\"库存扣减异常！\",\"path\":\"/inventory-service/inventory/mockWithTryException\"}",
  "path": "/order/mockInventoryWithTryException"
}
```

查询数据库，除了更新时间修改了，多了一条支付失败的订单，其他都没变

```bash
mysql> select * from hmily_account.account;
+----+---------+---------+---------------+---------------------+---------------------+
| id | user_id | balance | freeze_amount | create_time         | update_time         |
+----+---------+---------+---------------+---------------------+---------------------+
|  1 | 10000   | 9999999 |             0 | 2017-09-18 14:54:22 | 2020-12-10 19:54:33 |
+----+---------+---------+---------------+---------------------+---------------------+
1 row in set (0.00 sec)

mysql> select * from hmily_order.order;
+----+---------------------+---------------------+--------+------------+--------------+-------+---------+
| id | create_time         | number              | status | product_id | total_amount | count | user_id |
+----+---------------------+---------------------+--------+------------+--------------+-------+---------+
| 11 | 2020-12-10 19:04:04 | 5890193653420609536 |      4 | 1          |            1 |     1 | 10000   |
| 12 | 2020-12-10 19:54:33 | 5890600208853602304 |      3 | 1          |            1 |     1 | 10000   |
+----+---------------------+---------------------+--------+------------+--------------+-------+---------+
2 rows in set (0.00 sec)

mysql> select * from hmily_stock.inventory;
+----+------------+-----------------+----------------+
| id | product_id | total_inventory | lock_inventory |
+----+------------+-----------------+----------------+
|  1 | 1          |         9999999 |              0 |
+----+------------+-----------------+----------------+
```

```
OrderStatusEnum
PAY_FAIL(3, "支付失败"),
PAY_SUCCESS(4, "支付成功");
```

