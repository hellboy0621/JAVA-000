package com.transformers.client;

import com.transformers.grpc.HelloGrpc;
import com.transformers.grpc.HelloMessage;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class ClientDemo {

    public static void main(String[] args) {
        ClientDemo clientDemo = new ClientDemo();
        clientDemo.remoteCall("hellboy0621");
    }

    private void remoteCall(String name) {
        HelloMessage.HelloRequest request = HelloMessage.HelloRequest
                .newBuilder()
                .setName(name)
                .build();
        HelloMessage.HelloResponse response;

        try {
            // 基于访问地址 创建通道
            Channel channel = ManagedChannelBuilder.forAddress("localhost", 9500)
                    .usePlaintext().build();

            // 利用通道，创建一个桩（Stub)对象
            HelloGrpc.HelloBlockingStub blockingStub = HelloGrpc.newBlockingStub(channel);

            // 通过桩对象 调用远程方法
            response = blockingStub.sayHello(request);

        } catch (StatusRuntimeException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("[client端远程调用 sayHello() 方法的结果为：" + response.getMessage());
    }

}
