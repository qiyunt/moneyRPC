package com.money.rpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.money.rpc.RpcApplication;
import com.money.rpc.config.RpcConfig;
import com.money.rpc.constant.RpcConstant;
import com.money.rpc.fault.retry.RetryStrategy;
import com.money.rpc.fault.retry.RetryStrategyFactory;
import com.money.rpc.fault.tolerant.TolerantStrategy;
import com.money.rpc.fault.tolerant.TolerantStrategyFactory;
import com.money.rpc.loadbalancer.LoadBalancer;
import com.money.rpc.loadbalancer.LoadBalancerFactory;
import com.money.rpc.model.RpcRequest;
import com.money.rpc.model.RpcResponse;
import com.money.rpc.model.ServiceMetaInfo;
import com.money.rpc.protocol.*;
import com.money.rpc.registry.Registry;
import com.money.rpc.registry.RegistryFactory;
import com.money.rpc.serializer.JDKSerializer;
import com.money.rpc.serializer.Serializer;
import com.money.rpc.serializer.SerializerFactory;
import com.money.rpc.server.tcp.VertxTcpClient;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @Author: money
 * @Description: 服务代理  (JDK 动态代理)
 * @Date: 2024/6/3 15:54
 * @Version: 1.0
 */

public class ServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 动态获取序列化器
        Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());

        // 构造请求
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();

        // 序列化
//        byte[] serialize = serializer.serialize(rpcRequest);

        // 从注册中心获取服务提供者请求地址
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);

        List<ServiceMetaInfo> serviceMetaInfos = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());

        if (CollUtil.isEmpty(serviceMetaInfos)){
            throw new RuntimeException("暂无服务地址");
        }

//        // 暂时取第一个 TODO
//        ServiceMetaInfo selectedServiceMetaInfo = serviceMetaInfos.get(0);
        // 负载均衡
        LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
        // 将调用方法名（请求路劲）作为负载均衡参数
        Map<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("methodName",rpcRequest.getMethodName());
        ServiceMetaInfo select = loadBalancer.select(stringObjectHashMap, serviceMetaInfos);

        // 发送tcp请求
        // 使用重试策略
        RpcResponse rpcResponse;
        try {
            RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(rpcConfig.getRetryStrategy());
            rpcResponse = retryStrategy.doRetry(() ->
                    VertxTcpClient.doRequest(rpcRequest, select)
            );
        }catch (Exception e){
            // 容错机制
            TolerantStrategy tolerantStrategy = TolerantStrategyFactory.getInstance(rpcConfig.getTolerantStrategy());
            rpcResponse = tolerantStrategy.doTolerant(null,e);
        }
        return rpcResponse.getData();

//        Vertx vertx = Vertx.vertx();
//        NetClient netClient = vertx.createNetClient();
//        CompletableFuture<RpcResponse> responseFuture = new CompletableFuture<>();
//        netClient.connect(selectedServiceMetaInfo.getServicePort(),selectedServiceMetaInfo.getServiceHost(),
//                result->{
//            if (result.succeeded()){
//                System.out.println("Connected to TCP server");;
//                NetSocket socket = result.result();
//                // 发送数据
//                // 构造消息
//                ProtocolMessage<RpcRequest> protocolMessage = new ProtocolMessage<>();
//                ProtocolMessage.Header header = new ProtocolMessage.Header();
//                header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
//                header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
//                header.setSerializer((byte) ProtocolMessageSerializerEnum.getEnumByValue(RpcApplication.getRpcConfig().getSerializer()).getKey());
//                header.setStatus((byte) ProtocolMessageStatusEnum.OK.getValue());
//                header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
//                header.setRequestId(IdUtil.getSnowflakeNextId());
//                protocolMessage.setHeader(header);
//                protocolMessage.setBody(rpcRequest);
//                // 编码请求
//                try {
//                    Buffer encode = ProtocolMessageEncoder.encode(protocolMessage);
//                    socket.write(encode);
//                }catch (IOException e){
//                    throw new RuntimeException("协议消息编码错误");
//                }
//
//                // 接收响应
//                socket.handler(buffer -> {
//                    try {
//                        ProtocolMessage<RpcResponse> rpcResponseProtocolMessage = (ProtocolMessage<RpcResponse>) ProtocolMessageDecoder.decode(buffer);
//                        responseFuture.complete(rpcResponseProtocolMessage.getBody());
//
//                    }catch (IOException e){
//                        throw new RuntimeException("协议消息解码错误");
//                    }
//                });
//            }else{
//                System.err.println("Failed to connect to TCP server");
//            }
//        });
//
//        RpcResponse rpcResponse = responseFuture.get();
//        // 记得关闭连接
//        netClient.close();
//        return rpcResponse.getData();


//        http 请求
//        try (HttpResponse httpResponse = HttpRequest.post(serviceMetaInfo1.getServiceHost() + ":" + serviceMetaInfo1.getServicePort())
//                .body(serialize)
//                .execute()) {
//            byte[] result = httpResponse.bodyBytes();
//            // 反序列化
//            RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
//            return rpcResponse.getData();
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//        return null;
    }
}
