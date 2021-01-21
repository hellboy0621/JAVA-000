package com.xtransformers.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;

@Data
@AllArgsConstructor
public class KmqMessage<T> {

    private HashMap<String, Object> headers;

    private T body;

}
