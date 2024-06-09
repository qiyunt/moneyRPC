package com.money.rpc.protocol;

/**
 * @Author: money
 * @Description: 协议常量
 * @Date: 2024/6/5 17:59
 * @Version: 1.0
 */

public interface ProtocolConstant {
    /**
     * 消息体长度
     */
    int MESSAGE_HEADER_LENGTH = 17;

    /**
     * 协议魔数
     */
    byte PROTOCOL_MAGIC = 0x1;

    /**
     * 协议版本号
     */
    byte PROTOCOL_VERSION = 0x1;

}
