package com.money.rpc.loadbalancer;

import com.money.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Author:     money
 * Description:  TODO
 * Date:    2024/6/7 17:54
 * Version:    1.0
 */

public class RandomLoadBalancer implements LoadBalancer{

    private final Random random = new Random();

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        int size = serviceMetaInfoList.size();
        if (size == 0){
            return null;
        }
        if (size == 1){
            return serviceMetaInfoList.get(0);
        }
        return serviceMetaInfoList.get(random.nextInt(size));
    }
}
