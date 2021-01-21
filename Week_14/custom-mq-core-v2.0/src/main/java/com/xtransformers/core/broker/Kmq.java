package com.xtransformers.core.broker;

import com.xtransformers.core.entity.KmqMessage;
import lombok.Getter;
import lombok.SneakyThrows;

public final class Kmq<T> {

    @Getter
    private String topic;

    @Getter
    private int capacity;

    private int writeIndex;

    private KmqMessage<T>[] queue;

    public Kmq(String topic, int capacity) {
        this.topic = topic;
        this.capacity = capacity;
        this.queue = new KmqMessage[capacity];
        this.writeIndex = 0;
    }

    public boolean send(KmqMessage<T> message) {
        if (writeIndex >= capacity)
            return false;
        queue[writeIndex++] = message;
        return true;
    }

    @SneakyThrows
    public KmqMessage<T> poll(int offset) {
        return queue[offset];
    }

}
