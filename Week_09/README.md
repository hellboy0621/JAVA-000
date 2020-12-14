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
2）尝试将客户端动态代理改成AOP
3）尝试使用Netty+HTTP作为client端传输方式
4、（选做☆☆）升级自定义RPC的程序：
1）尝试使用压测并分析优化RPC性能
2）尝试使用Netty+TCP作为两端传输方式
3）尝试自定义二进制序列化
4）尝试压测改进后的RPC并分析优化，有问题欢迎群里讨论
5）尝试将fastjson改成xstream
6）尝试使用字节码生成方式代替服务端反射



