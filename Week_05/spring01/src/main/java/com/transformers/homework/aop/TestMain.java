package com.transformers.homework.aop;

import com.transformers.homework.aop.interceptor.impl.MyInterceptor;
import com.transformers.homework.aop.proxy.ProxyBean;
import com.transformers.homework.aop.service.HelloService;
import com.transformers.homework.aop.service.impl.HelloServiceImpl;

public class TestMain {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        Object proxyBean = ProxyBean.getProxyBean(helloService, new MyInterceptor());
        ((HelloService) proxyBean).sayHello("hellboy0621");
    }
    /**
     * MyInterceptor.before
     * MyInterceptor.around - before
     * hello hellboy0621
     * MyInterceptor.around - after
     * MyInterceptor.after
     * MyInterceptor.afterReturning
     */
}
