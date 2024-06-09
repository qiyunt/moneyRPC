package com.money.rpc.protocol;


import lombok.Getter;


/**
 * 协议消息的类型枚举
 */

@Getter
public enum ProtocolMessageTypeEnum {
    REQUEST(0),
    RESPONSE(1),
    HEART_BEAT(2),
    OTHERS(3);

    private final int key;

    ProtocolMessageTypeEnum(int key){
        this.key = key;
    }

    /**
     * 根据 key 获取枚举
     */
    public static ProtocolMessageTypeEnum getEnumByKey(int key){
        for (ProtocolMessageTypeEnum protocolMessageTypeEnum : ProtocolMessageTypeEnum.values()){
            if (protocolMessageTypeEnum.key == key){
                return protocolMessageTypeEnum;
            }
        }
        return null;
    }
}
