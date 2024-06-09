package com.money.rpc.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: money
 * @Description: 本地注册中心
 * @Date: 2024/6/3 14:33
 * @Version: 1.0
 */

public class LocalRegistry {
    /**
     * 注册信息
     */
    private static final Map<String, Class<?>> map = new ConcurrentHashMap<>();

    /**
     * 注册服务
     *
     * @param serviceName
     * @param implClass
     * @return null
     */
    public static void register(String serviceName,Class<?> implClass){
        map.put(serviceName,implClass);
    }

    /**
     * 获取服务
     *
     * @param serviceName
     * @return Class
     */
    public static Class<?> get(String serviceName) {
        return map.get(serviceName);
   }

    /**
     * 删除服务
     *
     * @param serviceName
     * @return null
     */
    public static void remove(String serviceName){
        map.remove(serviceName);
   }

}
