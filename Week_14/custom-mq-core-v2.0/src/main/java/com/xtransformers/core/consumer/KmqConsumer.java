package com.xtransformers.core.consumer;

import com.xtransformers.core.broker.Kmq;
import com.xtransformers.core.broker.KmqBroker;
import com.xtransformers.core.entity.KmqMessage;

import java.util.HashMap;
import java.util.Map;

public class KmqConsumer<T> {

    private final String name;

    private final KmqBroker<T> kmqBroker;

    private Kmq<T> kmq;

    private final Map<Kmq<T>, Integer> offsetMap;

    public KmqConsumer(String name, KmqBroker<T> kmqBroker) {
        this.name = name;
        this.kmqBroker = kmqBroker;
        this.offsetMap = new HashMap<>();
    }

    public void subscribe(String topic) {
        this.kmq = kmqBroker.findKmq(topic);
        if (kmq == null)
            throw new RuntimeException("Topic[" + topic + "] doesn't exist.");
        offsetMap.putIfAbsent(kmq, 0);
    }

    public KmqMessage<T> poll() {
        KmqMessage<T> kmqMessage = kmq.poll(offsetMap.get(kmq));
        if (kmqMessage != null) {
            offsetMap.put(kmq, offsetMap.get(kmq) + 1);
        }
        return kmqMessage;
    }
}
