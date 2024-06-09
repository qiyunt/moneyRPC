package com.money.rpc.registry;

import com.money.rpc.config.RegistryConfig;
import com.money.rpc.model.ServiceMetaInfo;

import java.util.List;

/**
 * Author:     money
 * Description:  注册中心接口
 * Date:    2024/6/4 12:18
 * Version:    1.0
 */

public interface Registry {
    /**
     * 初始化
     *
     * @param registryConfig 注册配置
     */
    void init(RegistryConfig registryConfig);

    /**
     * 注册服务 （服务端）
     *
     * @param serviceMetaInfo 注册信息
     */
    void registry(ServiceMetaInfo serviceMetaInfo) throws Exception;

    /**
     * 注销服务 （服务端）
     *
     * @param serviceMetaInfo 注册信息
     */
    void unRegistry(ServiceMetaInfo serviceMetaInfo);

    /**
     * 服务发现 （获取某服务的所有节点，消费端）
     *
     * @param serviceKey 服务key
     * @return List
     */
    List<ServiceMetaInfo> serviceDiscovery(String serviceKey);

    /**
     * 心跳检测 (服务端)
     */
    void heartBeat();

    /**
     * 监听 （消费端）
     */
    void watch(String serviceNodeKey);

    /**
     * 服务销毁
     */
    void destroy();
}
