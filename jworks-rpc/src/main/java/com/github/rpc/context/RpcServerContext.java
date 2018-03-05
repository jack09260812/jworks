package com.github.rpc.context;

import com.github.rpc.proxy.ProxyHandler;
import com.github.rpc.register.ServiceRegistry;
import com.github.rpc.remote.RemoteServer;
import com.github.rpc.serialize.Serialization;

/**
 * Created by jinwei.li on 2017/6/28 0028.
 */
public class RpcServerContext implements RpcContext {
    private ServiceRegistry serviceRegistry;
    private ProxyHandler proxyHandler;
    private Serialization serialization;
    private RemoteServer remoteServer;

    @Override
    public ServiceRegistry getServiceRegistry() {
        return serviceRegistry;
    }

    public void setServiceRegistry(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    public ProxyHandler getProxyHandler() {
        return proxyHandler;
    }

    public void setProxyHandler(ProxyHandler proxyHandler) {
        this.proxyHandler = proxyHandler;
    }

    @Override
    public Serialization getSerialization() {
        return serialization;
    }

    public void setSerialization(Serialization serialization) {
        this.serialization = serialization;
    }

    @Override
    public RemoteServer getRemoteServer() {
        return remoteServer;
    }

    public void setRemoteServer(RemoteServer remoteServer) {
        this.remoteServer = remoteServer;
    }
}
