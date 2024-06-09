package com.money.consumer;

import com.money.rpc.serializer.Serializer;

import java.io.IOException;

/**
 * Author:     money
 * Description:  TODO
 * Date:    2024/6/4 9:58
 * Version:    1.0
 */

public class MoneySerializer implements Serializer {
    @Override
    public <T> byte[] serialize(T object) throws IOException {
        return new byte[0];
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws IOException {
        return null;
    }
}
