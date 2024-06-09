package com.money.rpc.server.tcp;

import com.money.rpc.model.RpcRequest;
import com.money.rpc.model.RpcResponse;
import com.money.rpc.protocol.ProtocolMessage;
import com.money.rpc.protocol.ProtocolMessageDecoder;
import com.money.rpc.protocol.ProtocolMessageEncoder;
import com.money.rpc.protocol.ProtocolMessageTypeEnum;
import com.money.rpc.registry.LocalRegistry;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Author:     money
 * Description:  TODO
 * Date:    2024/6/5 21:10
 * Version:    1.0
 */

public class TcpServerHandler implements Handler<NetSocket> {

    @Override
    public void handle(NetSocket netSocket) {
        TcpBufferHandlerWrapper wrapper = new TcpBufferHandlerWrapper(buffer -> {
            // 接受请求，解码
            ProtocolMessage<RpcRequest> protocolMessage;
            try {
                protocolMessage = (ProtocolMessage<RpcRequest>) ProtocolMessageDecoder.decode(buffer);
            } catch (IOException e) {
                throw new RuntimeException("协议信息解码错误");
            }
            RpcRequest rpcRequest = protocolMessage.getBody();
            // 处理请求
            // 构造响应结果对象
            RpcResponse rpcResponse = new RpcResponse();
            try {
                // 获取要调用的服务实现类，通过反射调用
                Class<?> aClass = LocalRegistry.get(rpcRequest.getServiceName());

                Method method = aClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                // 代理调用远程服务
                Object invoke = method.invoke(aClass.newInstance(), rpcRequest.getArgs());

                // 封装返回结果
                rpcResponse.setData(invoke);
                rpcResponse.setDataType(method.getReturnType());
                rpcResponse.setMessage("ok");
            } catch (Exception e) {
                e.printStackTrace();
                rpcResponse.setMessage(e.getMessage());
                rpcResponse.setException(e);
//                throw new RuntimeException(e);
            }

            // 发送响应，编码
            ProtocolMessage.Header header = protocolMessage.getHeader();
            header.setType((byte) ProtocolMessageTypeEnum.RESPONSE.getKey());
            ProtocolMessage<RpcResponse> rpcResponseProtocolMessage = new ProtocolMessage<>(header, rpcResponse);
            try {
                Buffer encode = ProtocolMessageEncoder.encode(rpcResponseProtocolMessage);
                System.out.println("============服务端发送请求===========");
                netSocket.write(encode);
            } catch (IOException e) {
                throw new RuntimeException("协议消息编码错误");
            }
//            // 处理请求
//            netSocket.handler(buffer -> {
//
//            });
        });

        netSocket.handler(wrapper);
    }
}
