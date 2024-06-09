package com.money.rpc.registry;

import com.money.rpc.config.RegistryConfig;
import com.money.rpc.model.ServiceMetaInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Author:     money
 * Description:  TODO
 * Date:    2024/6/4 18:09
 * Version:    1.0
 */

public class RegistryTest {
    final Registry registry = new EtcdRegistry();

    @Before
    public void init(){
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("http://localhost:2379");
        registry.init(registryConfig);
    }

    @Test
    public void registry() throws Exception{
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("myService");
        serviceMetaInfo.setServiceVersion("1.0");
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServicePort(1234);
        registry.registry(serviceMetaInfo);

        ServiceMetaInfo serviceMetaInfo1 = new ServiceMetaInfo();
        serviceMetaInfo1.setServiceName("myService");
        serviceMetaInfo1.setServiceVersion("1.0");
        serviceMetaInfo1.setServiceHost("localhost");
        serviceMetaInfo1.setServicePort(1235);
        registry.registry(serviceMetaInfo1);

        ServiceMetaInfo serviceMetaInfo2 = new ServiceMetaInfo();
        serviceMetaInfo2.setServiceName("myService");
        serviceMetaInfo2.setServiceVersion("2.0");
        serviceMetaInfo2.setServiceHost("localhost");
        serviceMetaInfo2.setServicePort(1234);
        registry.registry(serviceMetaInfo2);
    }

    @Test
    public void unRegistry(){
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("myService");
        serviceMetaInfo.setServiceVersion("1.0");
        serviceMetaInfo.setServiceHost("localhost");
        serviceMetaInfo.setServicePort(1234);
        registry.unRegistry(serviceMetaInfo);
    }

    @Test
    public void serviceDiscovery(){
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName("myService");
        serviceMetaInfo.setServiceVersion("1.0");
        String serviceKey = serviceMetaInfo.getServiceKey();
        List<ServiceMetaInfo> serviceMetaInfos = registry.serviceDiscovery(serviceKey);
        System.out.println(serviceMetaInfos);
        Assert.assertNotNull(serviceMetaInfos);
    }

    @Test
    public void heartBeat() throws Exception{
        registry();
        Thread.sleep(60 * 1000L);
    }
}
