package com.github.rpc.remote.support;

import com.github.rpc.context.RpcContext;
import com.github.rpc.model.RpcFuture;
import com.github.rpc.model.RpcResponse;
import com.github.rpc.proxy.ProxyHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyRequestHandler extends SimpleChannelInboundHandler<RpcResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyRequestHandler.class);

    private RpcResponse response;
    private ProxyHandler proxy;

    private final Object obj = new Object();

    public NettyRequestHandler(ProxyHandler proxy) {
        this.proxy = proxy;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, RpcResponse response) throws Exception {
        RpcFuture future = RpcContext.rpcFutures.get(response.getRequestId());
        future.done(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("client caught exception", cause);
        ctx.close();
    }
}