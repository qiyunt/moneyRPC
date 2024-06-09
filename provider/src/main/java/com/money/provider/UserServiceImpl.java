package com.money.provider;

import com.money.common.domain.User;
import com.money.common.service.UserService;

/**
 * @Author: money
 * @Description: TODO
 * @Date: 2024/6/3 14:23
 * @Version: 1.0
 */

public class UserServiceImpl implements UserService {
    @Override
    public User getUser(User user) {
        System.out.println("用户名:" + user.getName());
        return user;
    }
}
