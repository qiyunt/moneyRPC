package com.money.rpc.model;

import cn.hutool.core.util.StrUtil;
import com.money.rpc.constant.RpcConstant;
import lombok.Data;

/**
 * Author:     money
 * Description:  服务元信息 （注册信息）
 * Date:    2024/6/4 12:03
 * Version:    1.0
 */

@Data
public class ServiceMetaInfo {
    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 服务版本号
     */
    private String serviceVersion = RpcConstant.DEFAULT_SERVICE_VERSION;

    /**
     * 服务地址
     */
    private String serviceAddress;

    /**
     * 服务域名
     */
    private String serviceHost;

    /**
     * 服务端口号
     */
    private Integer servicePort;

    /**
     * 服务分组 (暂未实现)
     */
    private String serviceGroup = "default";

    /**
     * 获取服务键名
     *
     * @return String
     */
    public String getServiceKey(){
        //  后续可扩展服务分组
//        return String.format("%s:%s:%s",serviceName,serviceVersion,serviceGroup);
        return String.format("%s:%s",serviceName,serviceVersion);
    }
    /**
     * 获取服务注册节点键名
     *
     * @return String
     */
    public String getServiceNodeKey(){
//        return String.format("%s/%s:",getServiceKey(),serviceAddress);
        return String.format("%s/%s:%s", getServiceKey(), serviceHost, servicePort);
    }

    /**
     * 获取完整服务地址
     *
     * @param
     * @return
     */
    public String getServiceAddress(){
        if (!StrUtil.contains(serviceHost,"http")){
            return String.format("http://%s:%s",serviceHost,servicePort);
        }
        return String.format("%s:%s",serviceHost,servicePort);
    }

}
