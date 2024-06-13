package com.money.rpc.bootstrap;

import com.money.rpc.RpcApplication;
import com.money.rpc.config.RegistryConfig;
import com.money.rpc.config.RpcConfig;
import com.money.rpc.model.ServiceMetaInfo;
import com.money.rpc.model.ServiceRegisterInfo;
import com.money.rpc.registry.LocalRegistry;
import com.money.rpc.registry.Registry;
import com.money.rpc.registry.RegistryFactory;
import com.money.rpc.server.tcp.VertxTcpServer;

import java.util.List;

/**
 * Author:     money
 * Description:  服务提供者初始化
 * Date:    2024/6/13 14:42
 * Version:    1.0
 */

public class ProviderBootstrap {
    /**
     * 初始化
     */
    public static void init(List<ServiceRegisterInfo<?>> serviceRegisterInfoList){
        // RPC 框架初始化 （配置和注册中心）
        RpcApplication.init();
        // 全局配置
        final RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        // 注册服务
        for (ServiceRegisterInfo<?> serviceRegisterInfo : serviceRegisterInfoList){
            String serviceName = serviceRegisterInfo.getServiceName();
            // 本地注册
            LocalRegistry.register(serviceName,serviceRegisterInfo.getImplClass());

            // 注册服务到注册中心
            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
            serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
            try {
                registry.registry(serviceMetaInfo);
            }catch (Exception e){
                throw new RuntimeException(serviceName + " 服务注册失败 ",e);
            }
        }

        // 启动服务器
        VertxTcpServer vertxTcpServer = new VertxTcpServer();
        vertxTcpServer.doStart(rpcConfig.getServerPort());
    }
}
