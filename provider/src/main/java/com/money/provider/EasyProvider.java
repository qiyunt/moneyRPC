package com.money.provider;

import com.money.common.service.UserService;
import com.money.rpc.RpcApplication;
import com.money.rpc.config.RegistryConfig;
import com.money.rpc.config.RpcConfig;
import com.money.rpc.model.ServiceMetaInfo;
import com.money.rpc.registry.LocalRegistry;
import com.money.rpc.registry.Registry;
import com.money.rpc.registry.RegistryFactory;
import com.money.rpc.server.HttpServer;
import com.money.rpc.server.VertxHttpServer;
import com.money.rpc.server.tcp.VertxTcpServer;

/**
 * @Author: money
 * @Description: TODO
 * @Date: 2024/6/3 14:23
 * @Version: 1.0
 */

public class EasyProvider {
    public static void main(String[] args) {
        // RPC 框架初始化，加载配置文件，初始化注册中心等
        RpcApplication.init();

        // 本地注册服务
        LocalRegistry.register(UserService.class.getName(),UserServiceImpl.class);

        // 注册服务到注册中心
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(UserService.class.getName());
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());

        try {
            registry.registry(serviceMetaInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 启动 tcp 服务
        VertxTcpServer vertxTcpServer = new VertxTcpServer();
        vertxTcpServer.doStart(RpcApplication.getRpcConfig().getServerPort());

        // 启动web服务
//        HttpServer httpServer = new VertxHttpServer();
//        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}
