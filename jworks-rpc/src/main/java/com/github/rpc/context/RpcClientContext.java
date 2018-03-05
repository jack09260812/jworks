package com.github.rpc.context;

import com.github.rpc.cluster.LoadBalance;
import com.github.rpc.listener.RemoteServerChangeListener;
import com.github.rpc.proxy.ProxyHandler;
import com.github.rpc.register.ServiceRegistry;
import com.github.rpc.remote.RemoteServer;
import com.github.rpc.serialize.Serialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by jinwei.li on 2017/6/28 0028.
 */
public class RpcClientContext implements RpcContext {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private ServiceRegistry serviceRegistry;
    private ProxyHandler proxyHandler;
    private Serialization serialization;
    private RemoteServer remoteServer;
    private LoadBalance loadBalance;
    private ConcurrentMap<String, List<String>> remoteAddress;
    private RemoteServerChangeListener notifyChangeListener;

    public RpcClientContext() {
        remoteAddress = new ConcurrentHashMap<>();
    }

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

    public LoadBalance getLoadBalance() {
        return loadBalance;
    }

    public void setLoadBalance(LoadBalance loadBalance) {
        this.loadBalance = loadBalance;
    }

    /**
     * 新增远程服务地址
     * @param className
     * @param address
     */
    public void addRemoteAddress(String className, List<String> address) {
        this.remoteAddress.put(className,address);
    }

    public List<String> getRemoteAddress(String className) {
        return remoteAddress.get(className);
    }

    public void setNotifyChangeListener(RemoteServerChangeListener notifyChangeListener) {
        this.notifyChangeListener = notifyChangeListener;
    }

    public RemoteServerChangeListener getNotifyChangeListener() {
        return notifyChangeListener;
    }
}
