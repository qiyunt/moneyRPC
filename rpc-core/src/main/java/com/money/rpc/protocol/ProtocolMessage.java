package com.money.rpc.protocol;

import cn.hutool.http.Header;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author:     money
 * Description:  协议消息结构
 * Date:    2024/6/5 16:30
 * Version:    1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProtocolMessage<T> {
    /**
     * 消息头
     */
    private Header header;

    /**
     * 消息体 (请求或响应对象)
     */
    private T body;

    /**
     * 协议消息头
     */
    @Data
    public static class Header{
        /**
         * 魔数，保证安全性
         */
        private byte magic;

        /**
         * 版本号
         */
        private byte version;

        /**
         * 序列化器
         */
        private byte serializer;

        /**
         * 消息类型 （请求 / 响应）
         */
        private byte type;

        /**
         * 状态
         */
        private byte status;

        /**
         * 请求id
         */
        private long requestId;

        /**
         * 消息体长度
         */
        private int bodyLength;
    }

}
