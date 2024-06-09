package com.money.rpc.fault.tolerant;

import com.money.rpc.model.RpcResponse;

import java.util.Map;

/**
 * @Author: money
 * @Description: 容错策略
 * @Date: 2024/6/8 10:22
 * @Version: 1.0
 */

public interface TolerantStrategy {
    /**
     * 容错
     */
    RpcResponse doTolerant(Map<String,Object> context,Exception e);
}
