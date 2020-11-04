package com.transformer.gateway.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;
import java.util.Map;

public class HttpClientInboundHandler extends ChannelInboundHandlerAdapter {

    private String requestUri;
    private ChannelHandlerContext ctx;

    public HttpClientInboundHandler(String requestUri, ChannelHandlerContext ctx) {
        this.requestUri = requestUri;
        this.ctx = ctx;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpResponse) {
            HttpResponse response = (HttpResponse) msg;
            for (Map.Entry<String, String> entry : response.headers().entries()) {
                System.out.println(entry.getKey() + " -> " + entry.getValue());
            }
        }
        if (msg instanceof HttpContent) {
            HttpContent content = (HttpContent) msg;
            ByteBuf buf = content.content();
            String resp = buf.toString(CharsetUtil.UTF_8);
            System.out.println(resp);
            handleResponse(resp);
            buf.release();
        }
    }

    private void handleResponse(String resp) {
        byte[] body = resp.getBytes();
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK, Unpooled.wrappedBuffer(body));
        response.headers().set("Content-Type", "text/plain;charset=UTF-8");
        response.headers().setInt("Content-Length", body.length);
        this.ctx.writeAndFlush(response);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        URI uri = new URI(requestUri);
        //配置HttpRequest的请求数据和一些配置信息
        FullHttpRequest request = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_0, HttpMethod.GET, uri.toASCIIString());

        request.headers()
                .set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=UTF-8")
                // 开启长连接
                // .set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE)
                // 设置传递请求内容的长度
                .set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());

        //发送数据
        ctx.writeAndFlush(request);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
