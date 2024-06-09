package com.money.rpc.server;

import io.vertx.core.Vertx;

/**
 * @Author: money
 * @Description: web服务器
 * @Date: 2024/6/3 14:27
 * @Version: 1.0
 */

public class VertxHttpServer implements HttpServer{
    @Override
    public void doStart(int port) {
        Vertx vertx = Vertx.vertx();

        io.vertx.core.http.HttpServer server = vertx.createHttpServer();

//        server.requestHandler(httpServerRequest -> {
//            System.out.println("请求 ： " + httpServerRequest.uri() + " " + httpServerRequest.method());
//            httpServerRequest.response()
//                    .putHeader("content-type","text/plain")
//                    .end("hello from vert.x http server");
//        });
        server.requestHandler(new HttpServerHandler());

        server.listen(port,httpServerAsyncResult -> {
            if (httpServerAsyncResult.succeeded()){
                System.out.println("正在监听端口： "  + port);
            }else {
                System.err.println("Failed to start server: "+httpServerAsyncResult.cause());
            }
        });
    }
}
