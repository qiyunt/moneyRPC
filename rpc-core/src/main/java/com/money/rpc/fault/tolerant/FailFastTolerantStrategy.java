package com.money.rpc.fault.tolerant;

import com.money.rpc.model.RpcResponse;

import java.util.Map;

/**
 * Author:     money
 * Description:  快速失败 （立刻通知外层调用方）
 * Date:    2024/6/8 10:39
 * Version:    1.0
 */

public class FailFastTolerantStrategy implements TolerantStrategy{
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        throw new RuntimeException("服务报错",e);
    }
}
