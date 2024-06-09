package com.money.consumer;

import com.money.common.domain.User;
import com.money.common.service.UserService;
import com.money.rpc.config.RpcConfig;
import com.money.rpc.proxy.ServiceProxyFactory;
import com.money.rpc.utils.ConfigUtils;

/**
 * @Author: money
 * @Description: TODO
 * @Date: 2024/6/3 15:07
 * @Version: 1.0
 */

public class EasyConsumer {
    public static void main(String[] args) {

        // 静态代理
//        UserService userService = new UserServiceProxy();
//        RpcConfig rpc = ConfigUtils.loadConfig(RpcConfig.class, "rpc");
//        System.out.println(rpc);

        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("money hello!money hello!money hello!money hello!money hello!money hello!money hello!money hello!money hello!money hello!money hello!money hello!money hello!money hello!money hello!money hello!money hello!money hello!money hello!money hello!money hello!money hello!");
        User user1 = userService.getUser(user);
        if (user1 == null){
            System.out.println("user == null");
        }else {
            System.out.println(user1.getName());
        }

        short number = userService.getNumber();
        System.out.println(number);

    }
}
