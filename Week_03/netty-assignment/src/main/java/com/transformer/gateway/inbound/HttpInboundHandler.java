package com.transformer.gateway.inbound;

import com.transformer.gateway.client.HttpClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;

import java.util.Objects;

public class HttpInboundHandler extends ChannelInboundHandlerAdapter {

    private String proxyServer;
    private HttpClient client;

    public HttpInboundHandler(String proxyServer) {
        this.proxyServer = proxyServer;
        client = new HttpClient(this.proxyServer);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            FullHttpRequest request = (FullHttpRequest) msg;
            // 过滤其他请求
            if (Objects.equals("/favicon.ico", request.uri())) {
                return;
            }
            client.handle(ctx, request);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }

    }
}
