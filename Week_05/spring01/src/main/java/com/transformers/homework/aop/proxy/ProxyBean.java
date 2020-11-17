package com.transformers.homework.aop.proxy;

import com.transformers.homework.aop.interceptor.Interceptor;
import com.transformers.homework.aop.invoke.Invocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyBean implements InvocationHandler {

    private Object target;
    private Interceptor interceptor;

    public static Object getProxyBean(Object target, Interceptor interceptor) {
        ProxyBean proxyBean = new ProxyBean();
        proxyBean.target = target;
        proxyBean.interceptor = interceptor;
        return Proxy.newProxyInstance(target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                proxyBean);
    }

    @Override

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 异常标识
        boolean exceptionFlag = false;
        Invocation invocation = new Invocation(args, method, target);
        Object retObj = null;
        try {
            if (interceptor.before() && interceptor.useAround()) {
                retObj = interceptor.around(invocation);
            } else {
                retObj = method.invoke(target, args);
            }
        } catch (Exception e) {
            exceptionFlag = true;
        }
        interceptor.after();
        if (exceptionFlag) {
            interceptor.afterThrowing();
        } else {
            interceptor.afterReturning();
            return retObj;
        }
        return null;
    }
}
