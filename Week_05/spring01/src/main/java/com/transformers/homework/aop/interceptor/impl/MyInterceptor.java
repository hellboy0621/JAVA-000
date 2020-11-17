package com.transformers.homework.aop.interceptor.impl;

import com.transformers.homework.aop.interceptor.Interceptor;
import com.transformers.homework.aop.invoke.Invocation;

import java.lang.reflect.InvocationTargetException;

public class MyInterceptor implements Interceptor {
    @Override
    public boolean before() {
        System.out.println("MyInterceptor.before");
        return true;
    }

    @Override
    public void after() {
        System.out.println("MyInterceptor.after");
    }

    @Override
    public Object around(Invocation invocation) throws InvocationTargetException, IllegalAccessException {
        System.out.println("MyInterceptor.around - before");
        Object obj = invocation.process();
        System.out.println("MyInterceptor.around - after");
        return obj;
    }

    @Override
    public void afterReturning() {
        System.out.println("MyInterceptor.afterReturning");
    }

    @Override
    public void afterThrowing() {
        System.out.println("MyInterceptor.afterThrowing");
    }

    @Override
    public boolean useAround() {
        return true;
    }
}
