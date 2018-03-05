package com.github.rpc.serialize.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rpc.serialize.Serialization;

import java.io.IOException;

/**
 * TODO param转换
 * Created by jinwei on 17-10-13.
 */
public class JacksonSerialization implements Serialization {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public byte[] serialize(Object object) {
        try {
            return mapper.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> cls) {
        try {
            return mapper.readValue(bytes, cls);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
