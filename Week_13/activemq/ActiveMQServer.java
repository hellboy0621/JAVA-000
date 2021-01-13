package com.xtransformers;

import org.apache.activemq.broker.BrokerService;

public class ActiveMQServer {

    /*
     * 启动 ActiveMQServer，在日志中看到启动成功信息
     *  INFO | Using Persistence Adapter: KahaDBPersistenceAdapter[D:\workspace\idea2019.3.5\java-advanced-learning\activemq-data\localhost\KahaDB]
     *  INFO | KahaDB is version 7
     *  INFO | PListStore:[D:\workspace\idea2019.3.5\java-advanced-learning\activemq-data\localhost\tmp_storage] started
     *  INFO | Apache ActiveMQ 5.16.0 (localhost, ID:hellboy0621-59919-1610506705807-0:1) is starting
     *  INFO | Listening for connections at: tcp://127.0.0.1:61615
     *  INFO | Connector tcp://127.0.0.1:61615 started
     *  INFO | Apache ActiveMQ 5.16.0 (localhost, ID:hellboy0621-59919-1610506705807-0:1) started
     *  INFO | For help or more information please see: http://activemq.apache.org
     *
     * 在项目根路径下会自动创建 activemq-data 文件夹，存储 ActiveMQ 服务相关数据。
     *
     *  然后，分别启动 ActivemqQueueDemo 和 ActivemqTopicDemo，连接启动的 tcp://127.0.0.1:61615 发送和获取消息。
     *
     */
    public static void main(String[] args) throws Exception {
        // 尝试用java代码启动一个ActiveMQ broker server
        // 然后用前面的测试demo代码，连接这个嵌入式的server

        BrokerService broker = new BrokerService();

        // configure the broker
        broker.addConnector("tcp://localhost:61615");

        broker.start();

        System.in.read();
    }

}
