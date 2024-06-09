package com.money.rpc;

import com.money.rpc.config.RegistryConfig;
import com.money.rpc.config.RpcConfig;
import com.money.rpc.registry.Registry;
import com.money.rpc.registry.RegistryFactory;
import com.money.rpc.utils.ConfigUtils;
import com.money.rpc.constant.RpcConstant;
import lombok.extern.slf4j.Slf4j;

/**
 * Author: money
 * Description: TODO
 * Date: 2024/6/3 16:41
 * Version: 1.0
 */

@Slf4j
public class RpcApplication {

    private static volatile RpcConfig rpcConfig;

    /**
     * 框架初始化,支持传入自定义配置
     *
     * @param newRpcConfig RpcConfig
     */
    public static void init(RpcConfig newRpcConfig){
        rpcConfig = newRpcConfig;
        log.info("rpc init ,config = {}",newRpcConfig.toString());
        // 注册中心初始化
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        // 获取注册中心类型进行 工厂实例化
        Registry instance = RegistryFactory.getInstance(registryConfig.getRegistry());
        instance.init(registryConfig);
        log.info("registry init ,config = {}",registryConfig);

        // 创建并注册 Shutdown Hook,JVM 退出时执行操作
        Runtime.getRuntime().addShutdownHook(new Thread(instance::destroy));
    }

    /**
     * 初始化
     */
    public static void init(){
        RpcConfig newRpcConfig;
        try{
            newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        }catch (Exception e){
            newRpcConfig = new RpcConfig();
        }
        init(newRpcConfig);
    }

    /**
     * 获取配置
     *
     * @return RpcConfig
     */
    public static RpcConfig getRpcConfig(){
        if (rpcConfig == null){
            synchronized (RpcApplication.class){
                if (rpcConfig == null){
                    init();
                }
            }
        }
        return rpcConfig;
    }
}
