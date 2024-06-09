package com.money.common.service;

import com.money.common.domain.User;

/**
 * @Author: money
 * @Description: 用户服务
 * @Date: 2024/6/3 14:18
 * @Version: 1.0
 */

public interface UserService {
    /**
     *  获取用户
     */
    User getUser(User user);

    /**
     * 新方法 - 获取数字
     */
    default short getNumber(){
        return 1;
    }
}
