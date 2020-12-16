package io.kimmking.rpcfx.client;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class RpcfxXstreamInvocationHandler implements InvocationHandler {

    public static final MediaType JSONTYPE = MediaType.get("application/json; charset=utf-8");
    public static final MediaType XML_TYPE = MediaType.get("application/xml");

    private final Class<?> serviceClass;
    private final String url;
    public <T> RpcfxXstreamInvocationHandler(Class<T> serviceClass, String url) {
        this.serviceClass = serviceClass;
        this.url = url;
    }

    // 可以尝试，自己去写对象序列化，二进制还是文本的，，，rpcfx是xml自定义序列化、反序列化，json: code.google.com/p/rpcfx
    // int byte char float double long bool
    // [], data class

    private XStream xstream = new XStream(new DomDriver());

    @Override
    public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {
        RpcfxRequest request = new RpcfxRequest();
        request.setServiceClass(this.serviceClass.getName());
        request.setMethod(method.getName());
        request.setParams(params);

        String reqJson = xstream.toXML(request);
        System.out.println("req xml: "+reqJson);

        final Request request1 = new Request.Builder()
                .url(url)
                .post(RequestBody.create(XML_TYPE, reqJson))
                .build();
        String respJson = client.newCall(request1).execute().body().string();
        System.out.println("resp xml: "+respJson);

        RpcfxResponse response = (RpcfxResponse) xstream.fromXML(respJson);

        // 这里判断response.status，处理异常
        // 考虑封装一个全局的RpcfxException
        if (response.isStatus()) {
            return response.getResult();
        }
        throw response.getRpcfxException();
    }

    // 1.可以复用client
    // 2.尝试使用httpclient或者netty client
    private final OkHttpClient client = new OkHttpClient();

}
