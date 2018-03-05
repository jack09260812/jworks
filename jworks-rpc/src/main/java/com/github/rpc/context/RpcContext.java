package com.github.rpc.context;

import com.github.rpc.model.RpcFuture;
import com.github.rpc.model.RpcResponse;
import com.github.rpc.proxy.ProxyHandler;
import com.github.rpc.register.ServiceRegistry;
import com.github.rpc.remote.RemoteServer;
import com.github.rpc.serialize.Serialization;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by jinwei.li on 2017/6/28 0028.
 */
public interface RpcContext {

    ConcurrentMap<String,RpcFuture> rpcFutures = new ConcurrentHashMap<>();

    /**
     * 获取注册
     * @return
     */
    ServiceRegistry getServiceRegistry();

    /**
     * 获取代理
     * @return
     */
    ProxyHandler getProxyHandler();

    /**
     * 获取序列化
     * @return
     */
    Serialization getSerialization();

    /**
     * 获取远程服务
     * @return
     */
    RemoteServer getRemoteServer();

}
