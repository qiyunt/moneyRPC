package com.money.rpc.fault.retry;

/**
 * Author:     money
 * Description:  重试策略键名常量
 * Date:    2024/6/8 9:46
 * Version:    1.0
 */

public interface RetryStrategyKeys {
    /**
     * 不重试
     */
    String NO = "no";

    /**
     * 固定时间间隔
     */
    String FIXED_INTERVAL = "fixedInterval";

}
