package com.money.rpc.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Author: money
 * @Description: Mock 服务代理 （JDK 动态代理）
 * @Date: 2024/6/3 18:27
 * @Version: 1.0
 */

@Slf4j
public class MockServiceProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 根据方法的返回值类型，生成特定的默认值对象
        Class<?> returnType = method.getReturnType();
        log.info("mock invoke {}",method.getName());
        return getDefaultObject(returnType);
    }

    /**
     * 生成指定类型的默认值对象 （可自行完善默认值逻辑）
     *
     * @param
     * @return
     */
    private Object getDefaultObject(Class<?> returnType) {
        // 基本类型
        if (returnType.isPrimitive()){
            if (returnType == boolean.class){
                return false;
            } else if (returnType == short.class) {
                return (short) 0;
            } else if (returnType == int.class) {
                return 0;
            } else if (returnType == long.class) {
                return 0L;
            }
        }
        // 对象类型
        return null;
    }
}
