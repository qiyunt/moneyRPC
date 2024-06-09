package com.money.rpc.loadbalancer;

import com.money.rpc.model.ServiceMetaInfo;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author:     money
 * Description:  负载均衡器测试
 * Date:    2024/6/8 0:05
 * Version:    1.0
 */

public class LoadBalancerTest {
    final LoadBalancer loadBalancer = new ConsistentHashLoadBalancer();

    @Test
    public void select(){
        // 请求参数
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("methodName","apple");
        // 服务列表
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("myService");
        serviceMetaInfo.setServiceVersion("1.0");
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServicePort(1234);

        ServiceMetaInfo serviceMetaInfo1 = new ServiceMetaInfo();
        serviceMetaInfo1.setServiceName("myService");
        serviceMetaInfo1.setServiceVersion("1.0");
        serviceMetaInfo1.setServiceHost("money.top");
        serviceMetaInfo1.setServicePort(80);

        ServiceMetaInfo serviceMetaInfo2 = new ServiceMetaInfo();
        serviceMetaInfo2.setServiceName("myService");
        serviceMetaInfo2.setServiceVersion("1.0");
        serviceMetaInfo2.setServiceHost("localhost");
        serviceMetaInfo2.setServicePort(12345);

        List<ServiceMetaInfo> list = Arrays.asList(serviceMetaInfo, serviceMetaInfo1);

        // 连续调用三次
        ServiceMetaInfo select = loadBalancer.select(requestParams, list);
        System.out.println(select);


        select = loadBalancer.select(requestParams, list);
        System.out.println(select);

        select = loadBalancer.select(requestParams, list);
        System.out.println(select);
    }
}
