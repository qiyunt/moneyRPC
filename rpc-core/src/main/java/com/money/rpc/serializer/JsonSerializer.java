package com.money.rpc.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.money.rpc.model.RpcRequest;
import com.money.rpc.model.RpcResponse;

import java.io.IOException;

/**
 * @Author: money
 * @Description: Json 序列化器
 * @Date: 2024/6/3 22:14
 * @Version: 1.0
 */

public class JsonSerializer implements Serializer{

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public <T> byte[] serialize(T object) throws IOException {
        return OBJECT_MAPPER.writeValueAsBytes(object);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws IOException {
        T obj = OBJECT_MAPPER.readValue(bytes,type);
        if (obj instanceof RpcRequest){
            return handleRequest((RpcRequest) obj,type);
        }
        if (obj instanceof RpcResponse){
            return handleResponse((RpcResponse) obj,type);
        }
        return obj;
    }

    /**
     * 由于 Object 的原始对象会被擦除，导致反序列化时会被作为 LinkedHashMap 无法转换成原始对象，因此这里做了特殊处理
     *
     * @param rpcRequest rpc 请求
     * @param type       类型
     * @return {@link T}
     * @throws IOException IO异常
     */
    private <T> T handleRequest(RpcRequest rpcRequest, Class<T> type) throws IOException {
        Class<?>[] parameterTypes = rpcRequest.getParameterTypes();
        Object[] args = rpcRequest.getArgs();

        for(int i = 0;i < parameterTypes.length;i++){
            Class<?> parameterType = parameterTypes[i];
            if (!parameterType.isAssignableFrom(args[i].getClass())){
                byte[] bytes = OBJECT_MAPPER.writeValueAsBytes(args[i]);
                OBJECT_MAPPER.readValue(bytes,parameterType);
            }
        }
        return type.cast(rpcRequest);
    }

    /**
     * 由于 Object 的原始对象会被擦除，导致反序列化时会被作为 LinkedHashMap 无法转换成原始对象，因此这里做了特殊处理
     *
     * @param rpcResponse rpc 响应
     * @param type        类型
     * @return {@link T}
     * @throws IOException IO异常
     */
    private <T> T handleResponse(RpcResponse rpcResponse, Class<T> type) throws IOException {
        // 处理响应数据
        byte[] dataBytes = OBJECT_MAPPER.writeValueAsBytes(rpcResponse.getData());
        rpcResponse.setData(OBJECT_MAPPER.readValue(dataBytes, rpcResponse.getDataType()));
        return type.cast(rpcResponse);
    }


}
