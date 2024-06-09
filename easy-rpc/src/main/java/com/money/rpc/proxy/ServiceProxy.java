package com.money.rpc.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.money.rpc.model.RpcRequest;
import com.money.rpc.model.RpcResponse;
import com.money.rpc.serializer.JDKSerializer;
import com.money.rpc.serializer.Serializer;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Author: money
 * @Description: 服务代理  (JDK 动态代理)
 * @Date: 2024/6/3 15:54
 * @Version: 1.0
 */

public class ServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Serializer serializer = new JDKSerializer();

        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();

        byte[] serialize = serializer.serialize(rpcRequest);
        try (HttpResponse httpResponse = HttpRequest.post("http://localhost:8080")
                .body(serialize)
                .execute()) {
            byte[] result = httpResponse.bodyBytes();
            // 反序列化
            RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
            return rpcResponse.getData();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
