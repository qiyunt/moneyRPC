//package com.money.consumer;
//
//import cn.hutool.http.HttpRequest;
//import cn.hutool.http.HttpResponse;
//import com.money.common.domain.User;
//import com.money.common.service.UserService;
//import com.money.rpc.model.RpcRequest;
//import com.money.rpc.model.RpcResponse;
//import com.money.rpc.serializer.JDKSerializer;
//import com.money.rpc.serializer.Serializer;
//
//import java.io.IOException;
//
///**
// * @Author: money
// * @Description: 用户服务静态代理
// * @Date: 2024/6/3 15:34
// * @Version: 1.0
// */
//
//public class UserServiceProxy implements UserService{
//    public User getUser(User user) {
//        final Serializer serializer = new JDKSerializer();
//
//        RpcRequest rpcRequest = RpcRequest.builder()
//                .serviceName(UserService.class.getName())
//                .methodName("getUser")
//                .parameterTypes(new Class[]{
//                        User.class
//                })
//                .args(new Object[]{user})
//                .build();
//
//        try {
//            byte[] serialize = serializer.serialize(rpcRequest);
//            try (HttpResponse httpResponse = HttpRequest.post("http://localhost:8080")
//                    .body(serialize)
//                    .execute()) {
//                byte[] result = httpResponse.bodyBytes();
//                // 反序列化（字节数组 => Java 对象）
//                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
//                return (User) rpcResponse.getData();
//            }
//        } catch (IOException e) {
////            throw new RuntimeException(e);
//            e.printStackTrace();
//        }
//        return null;
//    }
//}
