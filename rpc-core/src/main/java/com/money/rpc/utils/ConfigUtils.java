package com.money.rpc.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;

/**
 * Author: money
 * Description: 配置工具类
 * Date: 2024/6/3 16:31
 * Version: 1.0
 */

public class ConfigUtils {
    /**
     * 加载配置对象
     *
     * @param tClass Class
     * @param prefix String
     * @return T
     */
    public static <T> T loadConfig(Class<T> tClass, String prefix) {
        return loadConfig(tClass, prefix, "");
    }

    /**
     * 加载配置对象，支持区分环境
     *
     * @param tClass Class
     * @param prefix String
     * @param environment String
     * @return T
     */
    public static <T> T loadConfig(Class<T> tClass, String prefix, String environment) {
        StringBuilder configFileBuilder = new StringBuilder("application");
        if (StrUtil.isNotBlank(environment)) {
            configFileBuilder.append("-").append(environment);
        }
        configFileBuilder.append(".properties");
        Props props = new Props(configFileBuilder.toString());
        return props.toBean(tClass, prefix);
    }
}
