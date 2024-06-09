package com.money.rpc.server.tcp;

import com.money.rpc.server.HttpServer;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetServer;
import io.vertx.core.parsetools.RecordParser;

/**
 * Author:     money
 * Description:  TODO
 * Date:    2024/6/5 19:34
 * Version:    1.0
 */

public class VertxTcpServer implements HttpServer {

    private byte[] handleRequest(byte[] requestData){
        // 在这里编写处理请求的逻辑，根据requestData构造响应数据并返回
        // 这里只是一个示例，实际逻辑需要根据具体的业务需求来实现

        return "Hello,client!".getBytes();
    }

    @Override
    public void doStart(int port) {
        Vertx vertx = Vertx.vertx();

        // 创建 TCP 服务器
        NetServer server = vertx.createNetServer();

        // 处理请求
//        server.connectHandler(socket -> {
//            String testMsg = "hello server!hello server!hello server!hello server!";
//            int length = testMsg.getBytes().length;
//
//            // 构造parser
//            RecordParser recordParser = RecordParser.newFixed(8);
//            recordParser.setOutput(new Handler<Buffer>() {
//                // 初始化
//                int size = -1;
//                // 一次完整的读取 （头 + 体）
//                Buffer resultBuffer = Buffer.buffer();
//                @Override
//                public void handle(Buffer buffer) {
//                    if (size == -1){
//                        // 读取消息体长度
//                        size = buffer.getInt(4);
//                        recordParser.fixedSizeMode(size);
//                        // 写入头信息到结果
//                        resultBuffer.appendBuffer(buffer);
//                    }else{
//                        // 写入体结果到结果
//                        resultBuffer.appendBuffer(buffer);
//                        System.out.println(resultBuffer.toString());
//                        // 重置一轮
//                        recordParser.fixedSizeMode(8);
//                        size = -1;
//                        resultBuffer = Buffer.buffer();
//                    }
////                    String str = new String(buffer.getBytes());
////                    System.out.println(str);
////                    if (testMsg.equals(str)) {
////                        System.out.println("good");
////                    }
//                }
//            });
//            socket.handler(recordParser);
//
//
//
////            // 处理连接
////            socket.handler(buffer -> {
////                String testMsg = "hello server!hello server!hello server!hello server!";
////                int length = testMsg.getBytes().length;
////
////                // 构造parser
////                RecordParser recordParser = RecordParser.newFixed(length);
////                if (buffer.getBytes().length < length){
////                    System.out.println("半包,length = " + buffer.getBytes().length);
////                    return;
////                }
////                if (buffer.getBytes().length > length){
////                    System.out.println("粘包,length = " + buffer.getBytes().length);
////                    return;
////                }
////                String str = new String(buffer.getBytes(0, length));
////                System.out.println(str);
////                if (testMsg.equals(str)){
////                    System.out.println("good");
////                }
////                // 处理接受到的字节数组
////                byte[] requestData = buffer.getBytes();
////                // 在这里进行自定义的字节数组处理逻辑，比如解析请求、调用服务、构造响应等
////                byte[] responseData = handleRequest(requestData);
//                // 发送响应
////                socket.write(Buffer.buffer(responseData));
//            });
//        });

        server.connectHandler(new TcpServerHandler());

        // 启动 TCP 服务器并监听指定端口
        server.listen(port,netServerAsyncResult -> {
            if (netServerAsyncResult.succeeded()){
                System.out.println("TCP server started on port " + port);
            }else{
                System.err.println("Failed to start TCP server : " + netServerAsyncResult.cause());
            }
        });
    }

    public static void main(String[] args) {
        new VertxTcpServer().doStart(8888);
    }
}
