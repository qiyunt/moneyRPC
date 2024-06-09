package com.money.rpc.serializer;

import java.io.IOException;

/**
 * @Author: money
 * @Description: TODO
 * @Date: 2024/6/3 15:10
 * @Version: 1.0
 */

public interface Serializer {

    /**
     * 序列化
     *
     * @param
     * @return
     */
    <T> byte[] serialize(T object) throws IOException;

    /**
     * 反序列化
     *
     * @param
     * @return
     */
    <T> T deserialize(byte[] bytes,Class<T> type) throws IOException;
}
