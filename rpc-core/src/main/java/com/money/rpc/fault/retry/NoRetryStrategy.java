package com.money.rpc.fault.retry;

import com.money.rpc.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * Author:     money
 * Description:  TODO
 * Date:    2024/6/8 9:29
 * Version:    1.0
 */

public class NoRetryStrategy implements RetryStrategy{

    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        return callable.call();
    }
}
