package com.github.rpc.remote;

import com.github.rpc.serialize.Serialization;
import com.github.rpc.serialize.SerializationFactory;
import com.github.rpc.serialize.support.FastJsonSerialization;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class RpcDecoder extends ByteToMessageDecoder {

    private Class<?> genericClass;
    private Serialization serialization;

    public RpcDecoder(Class<?> genericClass) {
        this.genericClass = genericClass;
        this.serialization = SerializationFactory.getDefaultSerialization();
    }

    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4) {
            return;
        }
        in.markReaderIndex();
        int dataLength = in.readInt();
        if (dataLength < 0) {
            ctx.close();
        }
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;
        }
        byte[] data = new byte[dataLength];
        in.readBytes(data);

        Object obj = serialization.deserialize(data, genericClass);
        out.add(obj);
    }
}