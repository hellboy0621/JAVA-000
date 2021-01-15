package com.xtransformers.kafka.cluster;

import com.alibaba.fastjson.JSON;
import com.xtransformers.kafka.entity.Order;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class ProducerClusterDemo {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9001,localhost:9002,localhost:9003");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(props);

        String topic = "order-cluster-test1";

        Order order = new Order();
        order.setId(1001L);
        order.setTs(System.currentTimeMillis());
        order.setSymbol("USD2CNY");
        order.setPrice(95.0);

        ProducerRecord<String, String> record = new ProducerRecord<>(topic, JSON.toJSONString(order));

        producer.send(record);

        producer.close();
    }
}
