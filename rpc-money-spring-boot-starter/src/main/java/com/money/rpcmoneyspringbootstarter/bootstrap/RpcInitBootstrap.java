package com.money.rpcmoneyspringbootstarter.bootstrap;

import com.money.rpc.RpcApplication;
import com.money.rpc.config.RpcConfig;
import com.money.rpc.server.tcp.VertxTcpServer;
import com.money.rpcmoneyspringbootstarter.annotation.EnableRpc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Author:     money
 * Description:  RPC 框架启动
 * Date:    2024/6/13 15:33
 * Version:    1.0
 */

@Slf4j
public class RpcInitBootstrap implements ImportBeanDefinitionRegistrar {
    /**
     * String 初始化时执行，初始化 RPC 框架
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
                                        BeanDefinitionRegistry registry) {
        // 获取 EnableRpc 注解的属性值
        boolean needServer = (boolean) importingClassMetadata.getAnnotationAttributes(EnableRpc.class.getName()).get("needServer");
        
        // Rpc框架初始化 （配置和注册中心）
        RpcApplication.init();

        final RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        // 启动服务器
        if (needServer){
            VertxTcpServer vertxTcpServer = new VertxTcpServer();
            vertxTcpServer.doStart(rpcConfig.getServerPort());
        }else{
            log.info("不启动server");
        }
    }
}
