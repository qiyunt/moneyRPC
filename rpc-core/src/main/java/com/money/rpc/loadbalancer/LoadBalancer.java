package com.money.rpc.loadbalancer;

import com.money.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;

/**
 * Author:     money
 * Description:  负载均衡器 （消费端使用）
 * Date:    2024/6/6 16:38
 * Version:    1.0
 */

public interface LoadBalancer {
    /**
     * 选择服务调用
     */
    ServiceMetaInfo select(Map<String,Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList);
}
