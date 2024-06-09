package com.money.rpc.loadbalancer;

import com.money.rpc.spi.SpiLoader;

/**
 * Author:     money
 * Description:  负载均衡器工厂 （工厂模式，用于获取负载均衡器对象）
 * Date:    2024/6/7 18:33
 * Version:    1.0
 */

public class LoadBalancerFactory {
    static {
        System.out.println("====================================================================");
        System.out.println("执行了负载均衡器代理工厂");
        System.out.println("====================================================================");
        SpiLoader.load(LoadBalancer.class);
    }

    /**
     * 默认负载均衡器
     */
    private static final LoadBalancer DEFAULT_LOAD_BALANCER = new RandomLoadBalancer();

    /**
     * 获取实例
     */
    public static LoadBalancer getInstance(String key) {
        return SpiLoader.getInstance(LoadBalancer.class,key);
    }
}
