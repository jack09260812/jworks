package com.github.rpc.remote.support;

import com.alibaba.fastjson.JSON;
import com.github.rpc.model.RpcRequest;
import com.github.rpc.model.RpcResponse;
import com.github.rpc.proxy.ProxyHandler;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyResponseHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ProxyHandler handler;


    public NettyResponseHandler(ProxyHandler handler) {
        this.handler = handler;
    }

    @Override
    public void channelRead0(final ChannelHandlerContext ctx, RpcRequest request) throws Exception {
        RpcResponse response = new RpcResponse();
        response.setRequestId(request.getRequestId());
        try {
            //代理实现返回结果
            Object result = handler.invoke(request);
            if (logger.isDebugEnabled())
                logger.debug("响应结果:{}", JSON.toJSONString(result));
            logger.info("result:{}", result);
            response.setResult(result);
        } catch (Throwable t) {
            response.setError(t);
            logger.error("远程调用出错",t);
        }
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("server caught exception", cause);
        ctx.close();
    }
}