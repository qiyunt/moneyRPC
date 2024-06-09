package com.money.rpc.protocol;


import cn.hutool.core.util.ObjectUtil;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 协议消息的序列化器枚举
 */

@Getter
public enum ProtocolMessageSerializerEnum {

    JDK(0,"jdk"),
    JSON(1,"json"),
    KRYO(2,"kryo"),
    HESSIAN(3,"hessian");

    private final int key;

    private final String value;

    ProtocolMessageSerializerEnum(int key,String value){
        this.key = key;
        this.value = value;
    }

    /**
     * 获取值列表
     */
    public static List<String> getValues(){
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 key 获取枚举
     */
    public static ProtocolMessageSerializerEnum getEnumByKey(int key){
        for (ProtocolMessageSerializerEnum protocolMessageSerializerEnum : ProtocolMessageSerializerEnum.values()){
            if (protocolMessageSerializerEnum.key == key){
                return protocolMessageSerializerEnum;
            }
        }
        return null;
    }

    /**
     * 根据 value 获取枚举
     */
    public static ProtocolMessageSerializerEnum getEnumByValue(String value){
        if (ObjectUtil.isEmpty(value)){
            return null;
        }
        for (ProtocolMessageSerializerEnum protocolMessageSerializerEnum : ProtocolMessageSerializerEnum.values()){
            if (protocolMessageSerializerEnum.value.equals(value)){
                return protocolMessageSerializerEnum;
            }
        }
        return null;
    }
}
