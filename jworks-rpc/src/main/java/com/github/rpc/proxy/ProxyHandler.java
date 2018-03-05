package com.github.rpc.proxy;

import com.github.rpc.model.RpcRequest;

/**
 * Created by jinwei.li on 2017/6/27 0027.
 */
public interface ProxyHandler {
    /**
     * 代理接口或类
     * @param cls
     * @param <T>
     * @return
     * @throws Throwable
     */
    <T>T proxy(final String serverName,Class<?> cls) throws Throwable;

    /**
     * 执行代理类
     * @param request
     * @return
     * @throws Throwable
     */
    Object invoke(RpcRequest request) throws Throwable;
}
