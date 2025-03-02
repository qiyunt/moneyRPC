package com.money.rpc.registry;

import com.money.rpc.model.ServiceMetaInfo;

import java.util.List;

/**
 * Author:     money
 * Description:  注册中心服务本地缓存
 * Date:    2024/6/4 21:33
 * Version:    1.0
 */

public class RegistryServiceCache {
    /**
     * 服务缓存
     */
    List<ServiceMetaInfo> serviceCache;

    /**
     * 写缓存
     */
    void writeCache(List<ServiceMetaInfo> newServiceCache){
        this.serviceCache = newServiceCache;
    }

    /**
     * 读缓存
     */
    List<ServiceMetaInfo> readCache(){
        return this.serviceCache;
    }

    /**
     * 清空缓存
     */
    void clearCache(){
        this.serviceCache = null;
    }

}
