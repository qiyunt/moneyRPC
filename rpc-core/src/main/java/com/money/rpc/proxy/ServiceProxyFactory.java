package com.money.rpc.proxy;

import com.money.rpc.RpcApplication;

import java.lang.reflect.Proxy;

/**
 * @Author: money
 * @Description: 服务代理工厂 （用于创建代理对象）
 * @Date: 2024/6/3 15:57
 * @Version: 1.0
 */

public class ServiceProxyFactory {
    public static <T> T getProxy(Class<T> serviceClass){
        if (RpcApplication.getRpcConfig().isMock()){
            return getMockProxy(serviceClass);
        }

        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new ServiceProxy()
        );
    }

    /**
     * 根据服务类型获取 Mock 代理对象
     */
    private static <T> T getMockProxy(Class<T> serviceClass) {
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new MockServiceProxy()
        );
    }
}
