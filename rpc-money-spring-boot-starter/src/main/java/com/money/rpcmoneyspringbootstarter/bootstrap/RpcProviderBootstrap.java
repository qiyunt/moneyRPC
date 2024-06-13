package com.money.rpcmoneyspringbootstarter.bootstrap;

import com.money.rpc.RpcApplication;
import com.money.rpc.config.RegistryConfig;
import com.money.rpc.config.RpcConfig;
import com.money.rpc.model.ServiceMetaInfo;
import com.money.rpc.registry.LocalRegistry;
import com.money.rpc.registry.Registry;
import com.money.rpc.registry.RegistryFactory;
import com.money.rpcmoneyspringbootstarter.annotation.RpcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * Author:     money
 * Description:  RPC 服务提供者启动
 * Date:    2024/6/13 15:45
 * Version:    1.0
 */

@Slf4j
public class RpcProviderBootstrap implements BeanPostProcessor {

    /**
     * Bean 初始化后执行，注册事务
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        RpcService rpcService = beanClass.getAnnotation(RpcService.class);
        if (rpcService != null){
            // 需要注册服务
            // 1.获取服务基本信息
            Class<?> interfaceClass = rpcService.interfaceClass();

            // 默认值处理
            if (interfaceClass == void.class){
                interfaceClass = beanClass.getInterfaces()[0];
            }
            String serviceName = interfaceClass.getName();
            String serviceVersion = rpcService.serviceVersion();

            // 2. 注册服务
            // 本地注册
            LocalRegistry.register(serviceName,beanClass);

            // 全局配置
            final RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(serviceVersion);
            serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
            serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
            try {
                registry.registry(serviceMetaInfo);
            }catch (Exception e){
                throw new RuntimeException(serviceName + "服务注册失败",e);
            }
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean,beanName);
    }
}
