class22

4、（必做）基于Redis封装分布式数据操作：

1）在Java中实现一个简单的分布式锁；

详见项目 redis-distributed-lock。

简易分布式锁实现思路：

1.基于 Spring Boot + Jedis + AOP 实现。
2.获取锁，基于 set（SET KEY VALUE EX seconds NX） 命令。
3.释放锁，基于 del 命令，使用 Lua 脚本保证原子性，需要先判断根据 key 获取的值是否一致。

测试步骤：

1.启动项目。
2.在5s内连续多次调用接口 http://localhost:8080/hello/test ，在后台日志中就能看到"获取锁失败"的异常信息。

```bash
2021-01-06 13:33:46.200  INFO 33244 --- [nio-8080-exec-6] com.xtransformers.aop.LockMethodAspect   : isLock : true
2021-01-06 13:33:47.553  INFO 33244 --- [nio-8080-exec-7] com.xtransformers.aop.LockMethodAspect   : isLock : false
2021-01-06 13:33:47.554 ERROR 33244 --- [nio-8080-exec-7] com.xtransformers.aop.LockMethodAspect   : get lock failed.
2021-01-06 13:33:47.554  INFO 33244 --- [nio-8080-exec-7] com.xtransformers.aop.LockMethodAspect   : unlock.
2021-01-06 13:33:47.564 ERROR 33244 --- [nio-8080-exec-7] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed; nested exception is java.lang.RuntimeException: get lock failed.] with root cause

java.lang.RuntimeException: get lock failed.
	at com.xtransformers.aop.LockMethodAspect.around(LockMethodAspect.java:47) ~[classes/:na]
```



2）在Java中实现一个分布式计数器，模拟减库存。

详见项目 redis-stock。

实现思路：

1.基于 Spring Boot + RedisTemplate 实现。
2.使用 Lua 脚本，保证 Redis 减库存的原子性。

测试步骤：

1.启动项目。
2.调用获取库存接口 http://localhost:8080/getStock，如果是第一次调用，返回 -1。
3.调用减库存接口 http://localhost:8080/stock，默认库存为1000，超时时间设为1小时。每调用一次库存减一。
4.使用 wrk 压测，可以保证只要调用超过1000次，获取库存数量为0。