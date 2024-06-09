package com.money.rpc.server;

import com.money.rpc.RpcApplication;
import com.money.rpc.model.RpcRequest;
import com.money.rpc.model.RpcResponse;
import com.money.rpc.model.ServiceMetaInfo;
import com.money.rpc.registry.EtcdRegistry;
import com.money.rpc.registry.LocalRegistry;
import com.money.rpc.registry.Registry;
import com.money.rpc.registry.RegistryFactory;
import com.money.rpc.serializer.JDKSerializer;
import com.money.rpc.serializer.Serializer;
import com.money.rpc.serializer.SerializerFactory;
import com.money.rpc.serializer.SerializerKeys;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @Author: money
 * @Description: TODO
 * @Date: 2024/6/3 15:21
 * @Version: 1.0
 */

public class HttpServerHandler implements Handler<HttpServerRequest> {
    @Override
    public void handle(HttpServerRequest httpServerRequest) {
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());

        System.out.println("Received request ： " + httpServerRequest.method() + " " + httpServerRequest.uri());

        httpServerRequest.bodyHandler(body -> {
            byte[] bytes = body.getBytes();
            RpcRequest rpcRequest = null;
            try{
                rpcRequest = serializer.deserialize(bytes,RpcRequest.class);
            }catch (Exception e){
                e.printStackTrace();
            }

            RpcResponse rpcResponse = new RpcResponse();
            if (rpcRequest == null){
                rpcResponse.setMessage("rpcRequest is null");
                doResponse(httpServerRequest,rpcResponse,serializer);
                return;
            }

            Class<?> aClass = LocalRegistry.get(rpcRequest.getServiceName());

            try {
                Method method = aClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                Object invoke = method.invoke(aClass.newInstance(), rpcRequest.getArgs());

                rpcResponse.setData(invoke);
                rpcResponse.setDataType(method.getReturnType());
                rpcResponse.setMessage("ok");
            } catch (Exception e) {
//                throw new RuntimeException(e);
                e.printStackTrace();
                rpcResponse.setMessage(e.getMessage());
                rpcResponse.setException(e);
            }
            doResponse(httpServerRequest,rpcResponse,serializer);

        });
    }

    private void doResponse(HttpServerRequest httpServerRequest, RpcResponse rpcResponse, Serializer serializer) {
        HttpServerResponse httpServerResponse = httpServerRequest.response()
                .putHeader("content-type", "application/json");

        byte[] serialize = new byte[0];
        try {
            serialize = serializer.serialize(rpcResponse);
        } catch (IOException e) {
//            throw new RuntimeException(e);
            e.printStackTrace();
            httpServerResponse.end(Buffer.buffer());
        }
        httpServerResponse.end(Buffer.buffer(serialize));
    }
}
