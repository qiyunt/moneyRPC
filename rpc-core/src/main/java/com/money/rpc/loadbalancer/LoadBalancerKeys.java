package com.money.rpc.loadbalancer;

/**
 * Author:     money
 * Description:  负载均衡器键名常量
 * Date:    2024/6/7 18:29
 * Version:    1.0
 */

public interface LoadBalancerKeys {
    /**
     * 轮询
     */
    String ROUND_ROBIN = "roundRobin";

    String RANDOM = "random";

    String CONSISTENT_HASH = "consistentHash";

}
