class26

2、（必做）思考和设计自定义MQ第二个版本或第三个版本，写代码实现其中至少一
个功能点，把设计思路和实现代码，提交到github。

version1.0 - 内存Queue

1、基于内存Queue实现生产和消费API（已经完成）
1）创建内存Queue，作为底层消息存储
2）定义Topic，支持多个Topic
3）定义Producer，支持Send消息
4）定义Consumer，支持Poll消息

version2.0 - 自定义Queue

2、去掉内存Queue，设计自定义Queue，实现消息确认和消费offset
1）自定义内存Message数组模拟Queue。
2）使用指针记录当前消息写入位置。
3）对于每个命名消费者，用指针记录消费位置。

实现思路：

1.将 version1.0 版本中 JDK 内置的 BlockingQueue，替换成 KmqMessage 数组。
2.参考 Kafka 模型，在服务端（Kmq）记录消息写入位置（writeIndex）。
3.在消费者端（KmqConsumer），使用 Map<Kmq<T>, Integer> 记录消费位置（offset）。
4.在 KmqDemo 中，启动2个 Consumer 监听同一个 topic。

version3.0 - 基于SpringMVC实现MQServer

3、拆分broker和client(包括producer和consumer)
1）将Queue保存到web server端
2）设计消息读写API接口，确认接口，提交offset接口
3）producer和consumer通过httpclient访问Queue
4）实现消息确认，offset提交
5）实现consumer从offset增量拉取

