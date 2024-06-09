package com.money.rpc.protocol;

import cn.hutool.core.util.IdUtil;
import com.money.rpc.constant.RpcConstant;
import com.money.rpc.model.RpcRequest;
import io.vertx.core.buffer.Buffer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * Author:     money
 * Description:  TODO
 * Date:    2024/6/5 20:29
 * Version:    1.0
 */

public class ProtocolMessageTest {

    @Test
    public void testEncodeAndDecode() throws IOException{
        // 构造消息
        ProtocolMessage<RpcRequest> protocolMessage = new ProtocolMessage<>();
        ProtocolMessage.Header header = new ProtocolMessage.Header();
        header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
        header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
        header.setSerializer((byte) ProtocolMessageSerializerEnum.JDK.getKey());
        header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
        header.setStatus((byte) ProtocolMessageStatusEnum.OK.getValue());
        header.setRequestId(IdUtil.getSnowflakeNextId());
        header.setBodyLength(0);

        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setServiceName("myService");
        rpcRequest.setMethodName("myMethod");
        rpcRequest.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
        rpcRequest.setParameterTypes(new Class[]{String.class});
        rpcRequest.setArgs(new Object[]{"aaa","bbb"});
        protocolMessage.setHeader(header);
        protocolMessage.setBody(rpcRequest);

        Buffer encode = ProtocolMessageEncoder.encode(protocolMessage);
        System.out.println(encode.toString());

        System.out.println("+++++++++++");

        ProtocolMessage<?> decode = ProtocolMessageDecoder.decode(encode);
        System.out.println(decode);
        Assert.assertNotNull(decode);
    }
}
