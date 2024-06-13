package com.money.rpcmoneyspringbootstarter.bootstrap;

import com.money.rpc.proxy.ServiceProxyFactory;
import com.money.rpcmoneyspringbootstarter.annotation.RpcReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

/**
 * Author:     money
 * Description:  Rpc 服务消费者启动
 * Date:    2024/6/13 16:06
 * Version:    1.0
 */

@Slf4j
public class RpcConsumerBootstrap implements BeanPostProcessor {
    /**
     * Bean 初始化后执行 ，注入事务
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        // 遍历对象的所有属性
        Field[] declareFields = beanClass.getDeclaredFields();

        for(Field field : declareFields){
            RpcReference rpcReference = field.getAnnotation(RpcReference.class);
            if (rpcReference != null){
                // 为属性生成代理对象
                Class<?> interfaceClass = rpcReference.interfaceClass();
                if (interfaceClass == void.class){
                    interfaceClass = field.getType();
                }
                field.setAccessible(true);
                Object proxyObject = ServiceProxyFactory.getProxy(interfaceClass);
                try{
                   field.set(bean,proxyObject);
                   field.setAccessible(false);
                }catch (IllegalAccessException e){
                    throw new RuntimeException("为字段注入代理对象失败",e);
                }
            }

        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean,beanName);
    }
}
