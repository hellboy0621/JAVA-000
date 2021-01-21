package com.xtransformers.core.broker;

import com.xtransformers.core.entity.KmqMessage;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public final class Kmq<T> {

    @Getter
    private String topic;

    @Getter
    private int capacity;

    private LinkedBlockingQueue<KmqMessage<T>> queue;

    public Kmq(String topic, int capacity) {
        this.topic = topic;
        this.capacity = capacity;
        this.queue = new LinkedBlockingQueue<>(capacity);
    }

    public boolean send(KmqMessage<T> message) {
        return queue.offer(message);
    }

    public KmqMessage<T> poll() {
        return queue.poll();
    }

    @SneakyThrows
    public KmqMessage<T> poll(long timeout) {
        return queue.poll(timeout, TimeUnit.MILLISECONDS);
    }

}
