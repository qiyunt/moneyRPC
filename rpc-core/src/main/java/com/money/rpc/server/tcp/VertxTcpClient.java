package com.money.rpc.server.tcp;

import cn.hutool.core.util.IdUtil;
import com.money.rpc.RpcApplication;
import com.money.rpc.model.RpcRequest;
import com.money.rpc.model.RpcResponse;
import com.money.rpc.model.ServiceMetaInfo;
import com.money.rpc.protocol.*;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Author:     money
 * Description:  TODO
 * Date:    2024/6/5 19:43
 * Version:    1.0
 */

public class VertxTcpClient {

    /**
     * 发送请求
     *
     * @param rpcRequest RpcRequest
     * @param serviceMetaInfo serviceMetaInfo
     * @return RpcResponse
     * @throws InterruptedException 1
     * @throws ExecutionException 2
     */
    public static RpcResponse doRequest(RpcRequest rpcRequest, ServiceMetaInfo serviceMetaInfo) throws InterruptedException, ExecutionException{
        // 发送 TCP 请求
        Vertx vertx = Vertx.vertx();
        NetClient netClient = vertx.createNetClient();
        CompletableFuture<RpcResponse> responseCompletableFuture = new CompletableFuture<>();
        netClient.connect(serviceMetaInfo.getServicePort(),serviceMetaInfo.getServiceHost(),
                netSocketAsyncResult -> {
                    if (!netSocketAsyncResult.succeeded()){
                        System.err.println("Failed to connect to TCP server");;
                        return;
                    }
                    NetSocket socket = netSocketAsyncResult.result();
                    // 发送数据
                    // 构造消息
                    ProtocolMessage<RpcRequest> protocolMessage = new ProtocolMessage<>();
                    ProtocolMessage.Header header = new ProtocolMessage.Header();
                    header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
                    header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
                    header.setSerializer((byte) ProtocolMessageSerializerEnum.getEnumByValue(RpcApplication.getRpcConfig().getSerializer()).getKey());
                    header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
                    // 生成全局请求ID
                    header.setRequestId(IdUtil.getSnowflakeNextId());
                    protocolMessage.setHeader(header);
                    protocolMessage.setBody(rpcRequest);
                    // 编码请求
                    try {
                        Buffer encode = ProtocolMessageEncoder.encode(protocolMessage);
                        System.out.println("============客户端发送请求===========");
                        socket.write(encode);
                    }catch (IOException e){
                        throw new RuntimeException("协议消息编码错误");
                    }

                    // 接受响应
                    TcpBufferHandlerWrapper wrapper = new TcpBufferHandlerWrapper(buffer -> {
                        try {
                            ProtocolMessage<RpcResponse> rpcResponseProtocolMessage = (ProtocolMessage<RpcResponse>) ProtocolMessageDecoder.decode(buffer);
                            responseCompletableFuture.complete(rpcResponseProtocolMessage.getBody());

                        } catch (IOException e) {
                            throw new RuntimeException("协议消息解码错误");
                        }
                    });
                    socket.handler(wrapper);
                });
        RpcResponse rpcResponse = responseCompletableFuture.get();
        System.out.println("============客户端接受请求===========");
        // 记得关闭连接
        netClient.close();
        return rpcResponse;
    }


    public void start(){
        // 创建vertx.x 实例
        Vertx vertx = Vertx.vertx();

        vertx.createNetClient().connect(8888,"localhost",netSocketAsyncResult -> {
            if (netSocketAsyncResult.succeeded()){
                System.out.println("connected to tcp server");
                NetSocket socket = netSocketAsyncResult.result();
                for (int i = 0;i < 1000;i++){
                    // 发送数据
                    Buffer buffer = Buffer.buffer();
                    String s = "hello server!hello server!hello server!hello server!";
                    buffer.appendInt(0);
                    buffer.appendInt(s.getBytes().length);
                    buffer.appendBytes(s.getBytes());
                    socket.write(buffer);
//                    socket.write("hello server!hello server!hello server!hello server!");
                }

                // 接受响应
                socket.handler(buffer -> {
                    System.out.println("from server : " + buffer.toString());
                });
            }else{
                System.err.println("failed to connect tcp server");
            }
        });
    }

    public static void main(String[] args) {
        new VertxTcpClient().start();
    }
}
