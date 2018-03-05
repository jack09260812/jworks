package com.github.rpc.remote;

import com.github.rpc.serialize.Serialization;
import com.github.rpc.serialize.SerializationFactory;
import com.github.rpc.serialize.support.FastJsonSerialization;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class RpcEncoder extends MessageToByteEncoder {

    private Class<?> genericClass;
    private Serialization serialization;

    public RpcEncoder(Class<?> genericClass) {
        this.genericClass = genericClass;
        this.serialization = SerializationFactory.getDefaultSerialization();
    }

    @Override
    public void encode(ChannelHandlerContext ctx, Object in, ByteBuf out) throws Exception {
        if (genericClass.isInstance(in)) {
            byte[] data = serialization.serialize(in);
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }
}