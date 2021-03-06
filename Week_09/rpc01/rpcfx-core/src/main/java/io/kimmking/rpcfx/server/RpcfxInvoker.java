package io.kimmking.rpcfx.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResolver;
import io.kimmking.rpcfx.api.RpcfxResponse;
import io.kimmking.rpcfx.exception.RpcfxException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

@Slf4j
public class RpcfxInvoker {

    private RpcfxResolver resolver;

    private XStream xstream = new XStream(new DomDriver());

    public RpcfxInvoker(RpcfxResolver resolver) {
        this.resolver = resolver;
    }

    public RpcfxResponse invoke(RpcfxRequest request) {
        RpcfxResponse response = new RpcfxResponse();
        String serviceClass = request.getServiceClass();

        try {
            Class<?> klass = Class.forName(serviceClass);
            // 作业1：改成泛型和反射
            Object service = resolver.resolve(klass);//this.applicationContext.getBean(serviceClass);
            Method method = resolveMethodFromClass(service.getClass(), request.getMethod());
            Object result = method.invoke(service, request.getParams()); // dubbo, fastjson,
            // 两次json序列化能否合并成一个
            response.setResult(JSON.toJSONString(result, SerializerFeature.WriteClassName));
            response.setStatus(true);
            return response;

        } catch (IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {

            // 3.Xstream

            // 2.封装一个统一的RpcfxException
            // 客户端也需要判断异常
            RpcfxException rpcfxException = new RpcfxException("RpcfxInvoker#invoke", e);
            log.error("RpcfxInvoker#invoke", rpcfxException);
            response.setRpcfxException(rpcfxException);
            response.setStatus(false);
            return response;
        }
    }

    private Method resolveMethodFromClass(Class<?> klass, String methodName) {
        return Arrays.stream(klass.getMethods()).filter(m -> methodName.equals(m.getName())).findFirst().get();
    }

    public String xstreamInvoke(String requestStr) {
        RpcfxRequest request = (RpcfxRequest) xstream.fromXML(requestStr);
        RpcfxResponse response = new RpcfxResponse();
        String serviceClass = request.getServiceClass();

        try {
            Class<?> klass = Class.forName(serviceClass);
            // 作业1：改成泛型和反射
            Object service = resolver.resolve(klass);//this.applicationContext.getBean(serviceClass);
            Method method = resolveMethodFromClass(service.getClass(), request.getMethod());
            Object result = method.invoke(service, request.getParams()); // dubbo, fastjson,
            response.setResult(result);
            response.setStatus(true);
            return xstream.toXML(response);

        } catch (IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {

            // 3.Xstream

            // 2.封装一个统一的RpcfxException
            // 客户端也需要判断异常
            RpcfxException rpcfxException = new RpcfxException("RpcfxInvoker#invoke", e);
            log.error("RpcfxInvoker#invoke", rpcfxException);
            response.setRpcfxException(rpcfxException);
            response.setStatus(false);
            return xstream.toXML(response);
        }
    }
}
