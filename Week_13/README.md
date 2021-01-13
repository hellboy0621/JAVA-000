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

