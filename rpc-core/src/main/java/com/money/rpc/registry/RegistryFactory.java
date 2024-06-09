package com.money.rpc.registry;

import com.money.rpc.spi.SpiLoader;

/**
 * Author:     money
 * Description:  注册中心工厂 （用于获取注册中心对象）
 * Date:    2024/6/4 17:37
 * Version:    1.0
 */

public class RegistryFactory {
    static {
        SpiLoader.load(Registry.class);
    }

    /**
     * 默认注册中心
     */
    private static final Registry DEFAULT_REGISTRY = new EtcdRegistry();

    /**
     * 获取实例
     */
    public static Registry getInstance(String key){
        return SpiLoader.getInstance(Registry.class,key);
    }
}
