package com.github.rpc.serialize;

import com.github.rpc.register.ServiceRegistry;
import com.github.rpc.register.support.ZookeeperRegistry;
import com.github.rpc.serialize.support.FastJsonSerialization;
import com.github.rpc.serialize.support.JacksonSerialization;
import com.github.rpc.serialize.support.KryoSerialization;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by jinwei on 17-10-13.
 */
public class SerializationFactory {

    public static Serialization getSerialization(String serialization) {

        if (StringUtils.isEmpty(serialization)) {
            throw new IllegalArgumentException("serialization can`t be null！");
        }
        switch (serialization) {
            case "jackson":
                return new JacksonSerialization();
            case "fastjson":
                return new FastJsonSerialization();
            case "kryo":
                return new KryoSerialization();
            default:
                throw new IllegalArgumentException("illegal serialization");
        }
    }

    public static Serialization getDefaultSerialization() {
        return getSerialization("kryo");
    }
}
