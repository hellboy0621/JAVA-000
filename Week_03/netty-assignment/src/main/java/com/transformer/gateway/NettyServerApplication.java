package com.transformer.gateway;

import com.transformer.gateway.inbound.HttpInboundServer;

import static com.transformer.gateway.Constant.*;

public class NettyServerApplication {

    public static void main(String[] args) {
        System.out.println(GATEWAY_NAME + " " + GATEWAY_VERSION + " starting ...");
        try {
            new HttpInboundServer(SERVER_PORT, PROXY_SERVER).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(GATEWAY_NAME + " " + GATEWAY_VERSION + " started at http://localhost:" + SERVER_PORT
                + " for server:" + PROXY_SERVER);
    }

}
