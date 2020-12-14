package com.transformers.server;

import com.transformers.grpc.HelloGrpc;
import com.transformers.grpc.HelloMessage;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;

public class ServerDemo {

    private Server server;

    public static void main(String[] args) throws InterruptedException, IOException {
        final ServerDemo serverDemo = new ServerDemo();

        System.out.println("server启动中...");
        serverDemo.start();
        System.out.println("server启动完成");

        serverDemo.blockUntilShutdown();
    }

    // 启动一个Server实例，监听Client端的请求并处理
    private void start() throws IOException {
        int port = 9500;
        server = ServerBuilder.forPort(port)
                .addService(new HelloImpl())
                .build()
                .start();
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    /**
     * proto文件被编译后，在生成的HelloGrpc的抽象内部类HelloImplBase中包含了 proto中定义的服务接口的简单实现
     */
    static class HelloImpl extends HelloGrpc.HelloImplBase {
        @Override
        public void sayHello(HelloMessage.HelloRequest request,
                             StreamObserver<HelloMessage.HelloResponse> responseObserver) {
            HelloMessage.HelloResponse response = HelloMessage
                    .HelloResponse
                    .newBuilder()
                    .setMessage("[server端 sayHello() 方法处理结果] Hello, " + request.getName())
                    .build();
            // 通知gRPC框架把response从server端发送回client端
            responseObserver.onNext(response);
            // 完成调用
            responseObserver.onCompleted();
        }
    }

}
