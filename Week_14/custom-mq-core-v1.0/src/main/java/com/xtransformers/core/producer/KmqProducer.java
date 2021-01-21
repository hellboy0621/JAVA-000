package com.xtransformers.core.producer;

import com.xtransformers.core.broker.Kmq;
import com.xtransformers.core.broker.KmqBroker;
import com.xtransformers.core.entity.KmqMessage;

public class KmqProducer<T> {

    private KmqBroker<T> kmqBroker;

    public KmqProducer(KmqBroker<T> kmqBroker) {
        this.kmqBroker = kmqBroker;
    }

    public boolean send(String topic, KmqMessage<T> kmqMessage) {
        Kmq<T> kmq = kmqBroker.findKmq(topic);
        if (kmq == null) {
            throw new RuntimeException("Topic[" + topic + "] does't exist.");
        }
        return kmq.send(kmqMessage);
    }
}
