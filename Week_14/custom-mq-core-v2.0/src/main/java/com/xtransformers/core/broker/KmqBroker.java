package com.xtransformers.core.broker;

import com.xtransformers.core.consumer.KmqConsumer;
import com.xtransformers.core.producer.KmqProducer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Broker + Connection
 */
public class KmqBroker<T> {

    public static final int CAPACITY = 10000;

    private final Map<String, Kmq<T>> kmqMap = new ConcurrentHashMap<>();

    public void createTopic(String topic) {
        if (kmqMap.get(topic) != null)
            throw new RuntimeException("Topic[" + topic + "] already exist.");
        kmqMap.putIfAbsent(topic, new Kmq<>(topic, CAPACITY));
    }

    public Kmq<T> findKmq(String topic) {
        return kmqMap.get(topic);
    }

    public KmqProducer<T> createProducer() {
        return new KmqProducer<>(this);
    }

    public KmqConsumer<T> createConsumer(String name) {
        return new KmqConsumer<>(name, this);
    }

}
