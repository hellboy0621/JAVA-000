package com.xtransformers;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;

import javax.jms.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ActivemqTopicDemo {

    public static void main(String[] args) {
        String name = "test.topic";
//        String brokerURL = "tcp://192.168.56.95:61616";
        String brokerURL = "tcp://127.0.0.1:61615";

        Destination destination = new ActiveMQTopic(name);

        testTopic(brokerURL, destination);
    }

    private static void testTopic(String brokerURL, Destination destination) {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(brokerURL);
        ActiveMQConnection connection = null;
        Session session = null;
        try {
            connection = (ActiveMQConnection) factory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // 消费者
            MessageConsumer consumer = session.createConsumer(destination);
            final AtomicInteger count = new AtomicInteger(0);
            MessageListener listener = msg -> {
                System.out.println("[" + count.getAndIncrement() + "] receive message from "
                        + destination.toString() + ", message [" + msg + "]");
            };
            // 绑定消息监听器
            consumer.setMessageListener(listener);

            // 生产者
            MessageProducer producer = session.createProducer(destination);
            for (int i = 0; i < 10; i++) {
                TextMessage textMessage = session.createTextMessage("producer-message-" + i);
                producer.send(textMessage);
            }

            // 断开连接
            TimeUnit.SECONDS.sleep(2);

        } catch (JMSException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                if (session != null)
                    session.close();
                if (connection != null)
                    connection.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

}
