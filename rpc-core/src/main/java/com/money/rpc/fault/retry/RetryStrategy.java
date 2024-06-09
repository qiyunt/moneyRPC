package com.money.rpc.fault.retry;

import com.money.rpc.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * Author:     money
 * Description:  重试策略
 * Date:    2024/6/8 9:27
 * Version:    1.0
 */

public interface RetryStrategy {
    /**
     * 重试
     */
    RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception;
}
