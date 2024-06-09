package com.money.rpc.fault.retry;

import com.money.rpc.model.RpcResponse;
import org.junit.Test;

/**
 * Author:     money
 * Description:  重试策略测试
 * Date:    2024/6/8 9:36
 * Version:    1.0
 */


public class RetryStrategyTest {
//    RetryStrategy retryStrategy = new NoRetryStrategy();
    RetryStrategy retryStrategy = new FixedIntervalRetryStrategy();

    @Test
    public void doRetry(){
        try {
            RpcResponse rpcResponse = retryStrategy.doRetry(() -> {
                System.out.println("测试重试");
                throw new RuntimeException("模拟重试失败");
            });
            System.out.println(rpcResponse);
        }catch (Exception e){
            System.out.println("重试多次失败");
            e.printStackTrace();
        }
    }
}
