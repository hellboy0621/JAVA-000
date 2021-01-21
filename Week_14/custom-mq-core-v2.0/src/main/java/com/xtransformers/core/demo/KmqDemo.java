package com.xtransformers.core.demo;

import com.xtransformers.core.broker.KmqBroker;
import com.xtransformers.core.consumer.KmqConsumer;
import com.xtransformers.core.entity.KmqMessage;
import com.xtransformers.core.producer.KmqProducer;
import lombok.SneakyThrows;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class KmqDemo {

    @SneakyThrows
    public static void main(String[] args) {
        String topic = "topic-test";

        KmqBroker<Order> broker = new KmqBroker<>();
        broker.createTopic(topic);

        createConsumerThread(topic, broker, "consumer-test");

        createConsumerThread(topic, broker, "consumer-test2");

        Random random = new Random();
        String[] symbolArray = new String[]{"CNY2USD", "USD2CNY"};

        KmqProducer<Order> producer = broker.createProducer();
        for (int i = 0; i < 10; i++) {
            Order order = new Order()
                    .setId((long) i)
                    .setTs(System.currentTimeMillis())
                    .setSymbol(symbolArray[random.nextInt(2)])
                    .setPrice(random.nextDouble());
            producer.send(topic, new KmqMessage<>(null, order));
        }

        TimeUnit.MILLISECONDS.sleep(500);

        while (true) {
            char c = (char) System.in.read();

            if (c == 'e' || c == 'q')
                break;

            if (c > 20) {
                Order order = new Order()
                        .setId(random.nextLong())
                        .setTs(System.currentTimeMillis())
                        .setSymbol(symbolArray[random.nextInt(2)])
                        .setPrice(random.nextDouble());
                producer.send(topic, new KmqMessage<>(null, order));
            }
        }
        System.out.println("producer exit.");
    }

    private static void createConsumerThread(String topic, KmqBroker<Order> broker, String name) {
        KmqConsumer<Order> consumer = broker.createConsumer(name);
        consumer.subscribe(topic);

        Thread consumerThread = new Thread(() -> {
            while (true) {
                KmqMessage<Order> message = consumer.poll();
                if (message != null) {
                    System.out.println(Thread.currentThread().getName() + " " + message.getBody());
                }
            }
        });
        // 利用守护线程机制，退出程序
        consumerThread.setDaemon(true);
        consumerThread.start();
    }

}
