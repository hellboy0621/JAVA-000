package com.transformers.homework.aop.invoke;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Data
@AllArgsConstructor
public class Invocation {

    private Object[] params;
    private Method method;
    private Object target;

    // 反射方法
    public Object process() throws InvocationTargetException, IllegalAccessException {
        return method.invoke(target, params);
    }
}
