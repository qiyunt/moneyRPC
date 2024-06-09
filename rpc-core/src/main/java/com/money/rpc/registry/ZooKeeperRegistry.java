package com.money.rpc.registry;

import cn.hutool.core.collection.ConcurrentHashSet;
import com.money.rpc.config.RegistryConfig;
import com.money.rpc.model.ServiceMetaInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Author:     money
 * Description:  ZooKeeper 注册中心
 * 操作文档：<a href="https://curator.apache.org/docs/getting-started">Apache Curator</a>
 * 代码示例：<a href="https://github.com/apache/curator/blob/master/curator-examples/src/main/java/discovery/DiscoveryExample.java">DiscoveryExample.java</a>
 * 监听 key 示例：<a href="https://github.com/apache/curator/blob/master/curator-examples/src/main/java/cache/CuratorCacheExample.java">CuratorCacheExample.java</a>
 * Date:    2024/6/5 12:04
 * Version:    1.0
 */

@Slf4j
public class ZooKeeperRegistry implements Registry{

    private CuratorFramework client;

    private ServiceDiscovery<ServiceMetaInfo> serviceDiscovery;

    /**
     * 本机注册的节点 key 集合 （用于维护续期）
     */
    private final Set<String> localRegisterNodeKeySet = new HashSet<>();

    /**
     * 注册中心服务缓存
     */
    private final RegistryServiceCache registryServiceCache = new RegistryServiceCache();

    /**
     * 正在监听的key集合
     */
    private final Set<String> watchingKeySet = new ConcurrentHashSet<>();

    /**
     * 根节点
     */
    private static final String ZK_ROOT_PATH = "/rpc/zk";

    @Override
    public void init(RegistryConfig registryConfig) {
        // 构建 client  实例
        client = CuratorFrameworkFactory.builder()
                .connectString(registryConfig.getAddress())
                .retryPolicy(new ExponentialBackoffRetry(Math.toIntExact(registryConfig.getTimeout()),3))
                .build();

        // 构建 serviceDiscovery 实例
        serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceMetaInfo.class)
                .client(client)
                .basePath(ZK_ROOT_PATH)
                .serializer(new JsonInstanceSerializer<>(ServiceMetaInfo.class))
                .build();

        // 启动
        client.start();
        try {
            serviceDiscovery.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void registry(ServiceMetaInfo serviceMetaInfo) throws Exception {
        // 注册到 zk 里
        serviceDiscovery.registerService(buildServiceInstance(serviceMetaInfo));

        // 添加节点信息到本地缓存
        String registryKey = ZK_ROOT_PATH + "/" + serviceMetaInfo.getServiceNodeKey();
        localRegisterNodeKeySet.add(registryKey);

    }

    @Override
    public void unRegistry(ServiceMetaInfo serviceMetaInfo) {
        try {
            serviceDiscovery.unregisterService(buildServiceInstance(serviceMetaInfo));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 从本地缓存移除
        String registerKey = ZK_ROOT_PATH + "/" + serviceMetaInfo.getServiceNodeKey();
        localRegisterNodeKeySet.remove(registerKey);
    }

    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {
        // 优先从缓存里面获取服务
        List<ServiceMetaInfo> readCache = registryServiceCache.readCache();
        if (readCache != null) {
            return readCache;
        }

        // 查询服务信息
        try {
            Collection<ServiceInstance<ServiceMetaInfo>> serviceInstances = serviceDiscovery.queryForInstances(serviceKey);

            // 解析服务信息
            List<ServiceMetaInfo> serviceMetaInfos = serviceInstances.stream().map(ServiceInstance::getPayload).collect(Collectors.toList());
            // 写入缓存
            registryServiceCache.writeCache(serviceMetaInfos);
            return serviceMetaInfos;
        } catch (Exception e) {
            throw new RuntimeException("获取服务列表失败",e);
        }
    }

    @Override
    public void heartBeat() {
        // 不需要心跳机制，建立了临时节点，如果服务器故障，则临时节点直接丢失
    }

    /**
     * 监听 （消费端）
     */
    @Override
    public void watch(String serviceNodeKey) {
        String watchKey = ZK_ROOT_PATH + "/" + serviceNodeKey;
        boolean add = watchingKeySet.add(watchKey);
        if (add) {
            CuratorCache curatorCache = CuratorCache.build(client, watchKey);
            curatorCache.start();
            curatorCache.listenable().addListener(
                    CuratorCacheListener.builder()
                            .forDeletes(childData -> registryServiceCache.clearCache())
                            .forChanges((oldNode,node) -> registryServiceCache.clearCache())
                            .build()
            );
        }
    }

    @Override
    public void destroy() {
        log.info("当前节点下线");
        // 下线节点 （这一步可以不做，因为都是临时节点，服务下线，自然就被删掉了）
        for (String key : localRegisterNodeKeySet){
            try {
                client.delete().guaranteed().forPath(key);
            } catch (Exception e) {
                throw new RuntimeException(key + "节点下线失败",e);
            }
        }

        // 释放资源
        if (client != null){
            client.close();
        }
    }

    private ServiceInstance<ServiceMetaInfo> buildServiceInstance(ServiceMetaInfo serviceMetaInfo){
        String serviceAddress = serviceMetaInfo.getServiceHost() + ":" + serviceMetaInfo.getServicePort();

        try {
             return ServiceInstance.<ServiceMetaInfo>builder()
                     .id(serviceAddress)
                     .name(serviceMetaInfo.getServiceKey())
                     .address(serviceAddress)
                     .payload(serviceMetaInfo)
                     .build();

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
