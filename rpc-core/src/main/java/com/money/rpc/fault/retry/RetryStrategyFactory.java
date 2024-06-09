package com.money.rpc.fault.retry;

import com.money.rpc.spi.SpiLoader;

/**
 * Author:     money
 * Description:  重试策略工厂(用于获取重试器对象)
 * Date:    2024/6/8 9:48
 * Version:    1.0
 */

public class RetryStrategyFactory {

    static {
        SpiLoader.load(RetryStrategy.class);
    }

    /**
     * 默认重试器
     */
    private static final RetryStrategy DEFAULT_RETRY_STRATEGY = new NoRetryStrategy();

    /**
     * 获取实例
     */
    public static RetryStrategy getInstance(String key){
        return SpiLoader.getInstance(RetryStrategy.class,key);
    }
}
