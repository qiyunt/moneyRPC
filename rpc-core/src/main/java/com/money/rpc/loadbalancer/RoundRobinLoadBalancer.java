package com.money.rpc.loadbalancer;

import com.money.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author:     money
 * Description:  轮询
 * Date:    2024/6/6 16:42
 * Version:    1.0
 */

public class RoundRobinLoadBalancer implements LoadBalancer{

    /**
     * 当前轮询的下标
     */
    private final AtomicInteger atomicInteger = new AtomicInteger(0);

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if (serviceMetaInfoList.isEmpty()){
            return null;
        }

        // 只有一个服务,无需轮询
        int size = serviceMetaInfoList.size();
        if (size == 1){
            return serviceMetaInfoList.get(0);
        }

        int index = atomicInteger.getAndIncrement() % size;
        return serviceMetaInfoList.get(index);
    }
}
