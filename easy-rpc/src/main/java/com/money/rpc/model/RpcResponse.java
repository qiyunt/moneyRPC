package com.money.rpc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: money
 * @Description: 响应
 * @Date: 2024/6/3 14:59
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RpcResponse implements Serializable {
    /**
     * 响应数据
     */
    private Object data;

    /**
     * 响应数据类型（预留）
     */
    private Class<?> dataType;

    /**
     * 响应数据
     */
    private String message;

    /**
     * 异常信息
     */
    private Exception exception;
}
