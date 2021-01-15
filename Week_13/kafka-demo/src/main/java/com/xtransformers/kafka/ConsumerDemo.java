package com.xtransformers.kafka;

import com.alibaba.fastjson.JSON;
import com.xtransformers.kafka.entity.Order;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class ConsumerDemo {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("group.id", "group1");

        Consumer<String, String> consumer = new KafkaConsumer<>(props);

        String topic = "order-test1";

        consumer.subscribe(Collections.singletonList(topic));

        for (; ; ) {
            ConsumerRecords<String, String> poll = consumer.poll(Duration.ofSeconds(1L));
            poll.forEach(each -> {
                ConsumerRecord<String, String> record = each;
                Order order = JSON.parseObject(record.value(), Order.class);
                System.out.println(order);
            });
        }

//        consumer.close();
    }
}
