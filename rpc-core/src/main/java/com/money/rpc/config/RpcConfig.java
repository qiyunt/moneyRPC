package com.money.rpc.config;

import com.money.rpc.fault.retry.RetryStrategyKeys;
import com.money.rpc.fault.tolerant.TolerantStrategyKeys;
import com.money.rpc.loadbalancer.LoadBalancerKeys;
import com.money.rpc.serializer.Serializer;
import com.money.rpc.serializer.SerializerKeys;
import lombok.Data;

/**
 * @Author: money
 * @Description: RPC 框架配置
 * @Date: 2024/6/3 16:26
 * @Version: 1.0
 */

@Data
public class RpcConfig {
    /**
     * 序列化器
     */
    private String serializer = SerializerKeys.JDK;

    /**
     * 模拟调用
     */
    private boolean mock = false;

    /**
     * 名称
     */
    private String name = "moneyRPC";

    /**
     * 版本号
     */
    private String version = "1.0";

    /**
     * 服务器主机名
     */
    private String serverHost = "localhost";

    /**
     * 服务器端口号
     */
    private Integer serverPort = 8080;

    /**
     * 注册中心配置
     */
    private RegistryConfig registryConfig = new RegistryConfig();

    /**
     * 负载均衡器
     */
    private String loadBalancer = LoadBalancerKeys.ROUND_ROBIN;

    /**
     * 重试策略
     */
    private String retryStrategy = RetryStrategyKeys.NO;

    /**
     * 容错策略
     */
    private String tolerantStrategy = TolerantStrategyKeys.FAIL_FAST;

}
