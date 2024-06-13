package com.money.rpc.bootstrap;

import com.money.rpc.RpcApplication;

/**
 * Author:     money
 * Description:  服务消费者启动类 （初始化）
 * Date:    2024/6/13 15:06
 * Version:    1.0
 */

public class ConsumerBootstrap {
    /**
     * 初始化
     */
    public static void init() {
        // RPC 框架初始化 （配置和注册中心）
        RpcApplication.init();
    }
}
