第 17 课作业实践
1、（选做）实现简单的Protocol Buffer/Thrift/gRPC(选任一个)远程调用demo。

代码见grpc-study-demo文件夹。

gRPC使用的序列化协议是 Protocol Buffer，使用的传输协议是 HTTP2.0协议。

运行ServerDemo#main函数，再运行ClientDemo#main函数，即可测试rpc远程调用结果

```bash
server启动中...
server启动完成

[client端远程调用 sayHello() 方法的结果为：[server端 sayHello() 方法处理结果] Hello, hellboy0621
```

2、（选做）实现简单的WebService-Axis2/CXF远程调用demo。
3、（必做）改造自定义RPC的程序，提交到github：
1）尝试将服务端写死查找接口实现类变成泛型和反射
2）尝试将客户端动态代理改成字节码增强
3）尝试使用Netty+HTTP作为client端传输方式

作业在秦老师代码基础上改的，详见rpc01文件夹。

1. 封装一个全局的RpcfxException
2. 客户端动态代理使用ByteBuddy字节码增强技术替换
3. 去掉 @Bean 的 name 属性，使用类型依赖查找
4. 增加 xstream 序列化方式



4、（选做☆☆）升级自定义RPC的程序：
1）尝试使用压测并分析优化RPC性能
2）尝试使用Netty+TCP作为两端传输方式
3）尝试自定义二进制序列化
4）尝试压测改进后的RPC并分析优化，有问题欢迎群里讨论
5）尝试将fastjson改成xstream
6）尝试使用字节码生成方式代替服务端反射



***

第 18 课作业实践
1、（选做）按课程第二部分练习各个技术点的应用。
2、（选做）按dubbo-samples项目的各个demo学习具体功能使用。
3、（必做）结合dubbo+hmily，实现一个TCC外汇交易处理，代码提交到github：
1）用户A的美元账户和人民币账户都在A库，使用1美元兑换7人民币；
2）用户B的美元账户和人民币账户都在B库，使用7人民币兑换1美元；
3）设计账户表，冻结资产表，实现上述两个本地事务的分布式事务。

设计两张表：

​	一张账户表，记录用户的账户余额及冻结金额信息；

​	一张交易表，记录两个账户之间交易的详细信息。

一共2个数据库：（假设当前汇率美元:人民币=1:7）

​	用户A(user_id=1,account_id=1)在transaction_a库，初始余额usd_balance=1000，rmb_balance=0；

​	用户B(user_id=2,account_id=2)在transaction_a库，初始余额usd_balance=0，rmb_balance=1000；

```mysql
create table `t_account`(
	`account_id` bigint unsigned not null auto_increment,
    `user_id` bigint unsigned not null comment '用户ID',
    `usd_balance` decimal(10,2) unsigned DEFAULT '0.00' comment '美金账户余额',
    `rmb_balance` decimal(10,2) unsigned DEFAULT '0.00' comment '人民币账户余额',
    `usd_freeze_amount` decimal(10,2) NOT NULL COMMENT '美金账户冻结金额，扣款暂存余额',
    `rmb_freeze_amount` decimal(10,2) NOT NULL COMMENT '人民币账户冻结金额，扣款暂存余额',
    `create_time` bigint unsigned not null default 0 comment '创建时间',
    `update_time` bigint unsigned not null default 0 comment '更新时间',
    primary key (`account_id`)
) comment='账户表' engine=InnoDB charset=utf8mb4 COLLATE = 'utf8mb4_general_ci';

insert into t_account
values(null, 1, 1, 0, 0, 0, 1608727171156, 1608727171156);
insert into t_account
values(null, 2, 0, 7, 0, 0, 1608727171156, 1608727171156);

create table `t_transaction`(
	`transaction_id` bigint unsigned not null auto_increment,
    `from_account_id` bigint unsigned not null comment '用户ID',
    `to_account_id` bigint unsigned not null comment '用户ID',
    `type` varchar(16) not null default '' comment '交易币种类型 usd rmb',
    `amount` decimal(10,2) NOT NULL COMMENT '交易金额',
    `create_time` bigint unsigned not null default 0 comment '创建时间',
    `update_time` bigint unsigned not null default 0 comment '更新时间',
    primary key (`transaction_id`)
) comment='交易表' engine=InnoDB charset=utf8mb4 COLLATE = 'utf8mb4_general_ci';

```

代码详见dubbo-study文件夹。

依次启动项目DubboServerApplication和DubboClientApplication，必须按照顺序启动，否则在启动client时报找不到dubbo指定的service引用而启动失败。

启动后，在浏览器或postman上以GET方式调用接口http://localhost:8089/transaction/trans，成功后显示"success"。

成功后，查看账户余额，A用户usd_balance由1000减为999，rmb_balance由0增为7；B用户usd_balance由0增为1，rmb_balance由1000减为993。

```bash
mysql> select * from transaction_a.t_account;
+------------+---------+-------------+-------------+-------------------+-------------------+---------------+---------------+
| account_id | user_id | usd_balance | rmb_balance | usd_freeze_amount | rmb_freeze_amount | create_time   | update_time   |
+------------+---------+-------------+-------------+-------------------+-------------------+---------------+---------------+
|          1 |       1 |      999.00 |        7.00 |              0.00 |              0.00 | 1608727171156 | 1608727171156 |
+------------+---------+-------------+-------------+-------------------+-------------------+---------------+---------------+
1 row in set (0.00 sec)

mysql> select * from transaction_b.t_account;
+------------+---------+-------------+-------------+-------------------+-------------------+---------------+---------------+
| account_id | user_id | usd_balance | rmb_balance | usd_freeze_amount | rmb_freeze_amount | create_time   | update_time   |
+------------+---------+-------------+-------------+-------------------+-------------------+---------------+---------------+
|          2 |       2 |        1.00 |      993.00 |              0.00 |              0.00 | 1608727171156 | 1608727171156 |
+------------+---------+-------------+-------------+-------------------+-------------------+---------------+---------------+
1 row in set (0.00 sec)
```



踩坑记录：

1.使用注解的MyBatis，在Mapper接口中的参数需要加上@Param，否则报没有getter方法。

2.引入Hmily依赖时，需要排除一些jar包，否则冲突造成项目不能正常启动。

```xml
<dependency>
    <groupId>org.dromara</groupId>
    <artifactId>hmily-spring-boot-starter-apache-dubbo</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.dromara</groupId>
            <artifactId>hmily-repository-mongodb</artifactId>
        </exclusion>
        <exclusion>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
        </exclusion>
        <exclusion>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-log4j12</artifactId>
        </exclusion>
        <exclusion>
            <groupId>log4j</groupId>
            <artifactId>log4j</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```



4、（挑战☆☆）尝试扩展Dubbo
1）基于上次作业的自定义序列化，实现Dubbo的序列化扩展；
2）基于上次作业的自定义RPC，实现Dubbo的RPC扩展；
3）在Dubbo的filter机制上，实现REST权限控制，可参考dubbox；
4）实现一个自定义Dubbo的Cluster/Loadbalance扩展，如果一分钟内调用某个服务/
提供者超过10次，则拒绝提供服务直到下一分钟；
5）整合Dubbo+Sentinel，实现限流功能；
6）整合Dubbo与Skywalking，实现全链路性能监控。

