package com.money.rpc.config;

import com.money.rpc.registry.RegistryKeys;
import lombok.Data;

/**
 * Author:     money
 * Description:  RPC 框架注册中心配置
 * Date:    2024/6/4 12:16
 * Version:    1.0
 */

@Data
public class RegistryConfig {
    /**
     * 注册中心类别
     */
    private String registry = RegistryKeys.ETCD;

    /**
     * 注册中心地址
     */
    private String address;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 超时时间（单位毫秒）
     */
    private Long timeout = 10000L;
}
