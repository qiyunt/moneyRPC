package com.money.rpc.fault.tolerant;

import com.money.rpc.spi.SpiLoader;

/**
 * Author:     money
 * Description:  容错策略工厂（工厂模式，用于获取容器策略对象）
 * Date:    2024/6/8 16:27
 * Version:    1.0
 */

public class TolerantStrategyFactory {
    static {
        SpiLoader.load(TolerantStrategy.class);
    }

    /**
     * 默认容错策略
     */
    private static final TolerantStrategy DEFAULT_RETRY_STRATEGY = new FailFastTolerantStrategy();

    /**
     * 获取实例
     */
    public static TolerantStrategy getInstance(String key){
        return SpiLoader.getInstance(TolerantStrategy.class,key);
    }
}
