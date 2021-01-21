package com.xtransformers.core.consumer;

import com.xtransformers.core.broker.Kmq;
import com.xtransformers.core.broker.KmqBroker;
import com.xtransformers.core.entity.KmqMessage;

public class KmqConsumer<T> {

    private final KmqBroker<T> kmqBroker;

    private Kmq<T> kmq;

    public KmqConsumer(KmqBroker<T> kmqBroker) {
        this.kmqBroker = kmqBroker;
    }

    public void subscribe(String topic) {
        this.kmq = kmqBroker.findKmq(topic);
        if (kmq == null)
            throw new RuntimeException("Topic[" + topic + "] doesn't exist.");
    }

    public KmqMessage<T> poll() {
        return kmq.poll();
    }

    public KmqMessage<T> poll(long timeout) {
        return kmq.poll(timeout);
    }
}
