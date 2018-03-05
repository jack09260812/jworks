package com.github.rpc.serialize.support;

import com.alibaba.fastjson.JSON;
import com.github.rpc.serialize.Serialization;

/**
 * TODO param转换
 * Created by jinwei.li on 2017/7/5 0005.
 */
public class FastJsonSerialization implements Serialization {


    @Override
    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> cls) {
        return JSON.parseObject(bytes, cls);
    }
}
