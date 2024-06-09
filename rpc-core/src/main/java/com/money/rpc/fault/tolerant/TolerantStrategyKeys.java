package com.money.rpc.fault.tolerant;

/**
 * @Author: money
 * @Description: 容错策略键名常量
 * @Date: 2024/6/8 10:42
 * @Version: 1.0
 */

public interface TolerantStrategyKeys {
    /**
     * 故障恢复
     */
    String FAIL_BACK = "failBack";

    /**
     * 快速恢复
     */
    String FAIL_FAST = "failFast";

    /**
     * 故障转移
     */
    String FAIL_OVER = "failOver";

    /**
     * 静默处理
     */
    String FAIL_SAVE = "failSafe";
}
