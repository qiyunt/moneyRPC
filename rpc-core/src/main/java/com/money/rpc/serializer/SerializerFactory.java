package com.money.rpc.serializer;

import com.money.rpc.spi.SpiLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: money
 * Description: 序列化器工程 （用于获取序列化对象）
 * Date: 2024/6/3 23:44
 * Version: 1.0
 */

public class SerializerFactory {

    static {
        System.out.println("====================================================================");
        System.out.println("执行了序列化器代理工厂");
        System.out.println("====================================================================");

        SpiLoader.load(Serializer.class);
    }

//    /**
//     * 序列化映射 （用于实现单例）
//     */
//    private static final Map<String,Serializer> KEY_SERIALIZER_MAP = new HashMap<String,Serializer>(){{
//        put(SerializerKeys.JDK,new JDKSerializer());
//        put(SerializerKeys.JSON,new JsonSerializer());
//        put(SerializerKeys.KRYO,new KryoSerializer());
//        put(SerializerKeys.HESSIAN,new HessianSerializer());
//    }};

    /**
     * 默认序列化器
     */
    private static final Serializer DEFAULT_SERIALIZER = new JDKSerializer();
//    private static final Serializer DEFAULT_SERIALIZER = KEY_SERIALIZER_MAP.get("jdk");

    /**
     * 获取实例
     *
     * @param key String
     * @return Serializer
     */
    public static Serializer getInstance(String key){
//        return KEY_SERIALIZER_MAP.getOrDefault(key,DEFAULT_SERIALIZER);
        return SpiLoader.getInstance(Serializer.class,key);
    }
}
