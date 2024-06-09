package com.money.rpc.loadbalancer;

import com.money.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Author:     money
 * Description:  TODO
 * Date:    2024/6/7 18:03
 * Version:    1.0
 */

public class ConsistentHashLoadBalancer implements LoadBalancer{

    /**
     * 一致性 Hash 环，存放虚拟节点
     */
    private final TreeMap<Integer,ServiceMetaInfo> integerServiceMetaInfoTreeMap = new TreeMap<>();

    /**
     * 虚拟节点数
     */
    private static final int VIRTUAL_NODE_NUM = 100;

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if (serviceMetaInfoList.isEmpty()){
            return null;
        }

        // 构建虚拟节点环
        for (ServiceMetaInfo serviceMetaInfo : serviceMetaInfoList){
            for (int i = 0;i < VIRTUAL_NODE_NUM;i++){
                int hash = getHash(serviceMetaInfo.getServiceAddress() + "#" + i);
                integerServiceMetaInfoTreeMap.put(hash,serviceMetaInfo);
            }
        }

        // 获取调用请求的hash值
        int hash = getHash(requestParams);

        // 选择最接近且大于等于调用请求 hash 值的虚拟节点
        Map.Entry<Integer, ServiceMetaInfo> entry = integerServiceMetaInfoTreeMap.ceilingEntry(hash);
        if (entry == null){
            // 如果没有大于等于调用请求 hash 值的虚拟节点，则返回环首部的节点
            entry = integerServiceMetaInfoTreeMap.firstEntry();
        }
        return entry.getValue();

    }
    /**
     * Hash 算法，可自行实现
     */
    private int getHash(Object s) {
        return s.hashCode();
    }
}
