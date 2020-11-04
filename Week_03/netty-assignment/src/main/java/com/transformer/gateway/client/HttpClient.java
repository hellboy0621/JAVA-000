package com.transformer.gateway.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.URI;

public class HttpClient {

    private String backendUrl;

    public HttpClient(String backendUrl) {
        this.backendUrl = backendUrl;
    }

    public void handle(final ChannelHandlerContext ctx, final FullHttpRequest request) throws Exception {
        final URI uri = new URI(backendUrl);
        String host = uri.getHost();
        int port = uri.getPort();

        final String requestUri = request.uri();

        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new HttpResponseDecoder());
                    pipeline.addLast(new HttpRequestEncoder());
                    pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                    pipeline.addLast(new HttpClientInboundHandler(requestUri, ctx));
                }
            });
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();

            channelFuture.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
