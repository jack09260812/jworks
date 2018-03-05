package com.github.rpc.serialize.support;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.DefaultSerializers;
import com.github.rpc.serialize.Serialization;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by jinwei on 17-10-13.
 */
public class KryoSerialization implements Serialization {
    @Override
    public byte[] serialize(Object object) {
        Kryo kryo = new Kryo();
        kryo.setReferences(false);
        kryo.register(object.getClass());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Output output = new Output(baos);
        kryo.writeClassAndObject(output, object);
        output.flush();
        output.close();

        byte[] b = baos.toByteArray();
        try {
            baos.flush();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return b;
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> cls) {
        Kryo kryo = new Kryo();
        kryo.setReferences(false);
        kryo.register(cls);
        Input input = new Input(bytes);
        return (T) kryo.readClassAndObject(input);
    }
}
