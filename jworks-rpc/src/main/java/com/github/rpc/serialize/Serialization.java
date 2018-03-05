package com.github.rpc.serialize;

/**
 * Created by jinwei.li on 2017/6/27 0027.
 */
public interface Serialization {
    //序列化
    byte[] serialize(Object object);

    //反序列化
    <T> T deserialize(byte[] bytes, Class<T> cls);
}
