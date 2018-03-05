package com.github.rpc;


import com.github.rpc.cluster.RoundLoadBalance;
import com.github.rpc.context.RpcClientContext;
import com.github.rpc.listener.RemoteServerChangeListener;
import com.github.rpc.proxy.support.CglibClientProxyHandler;
import com.github.rpc.register.RegistryFactory;
import com.github.rpc.remote.support.NettyRemoteServer;
import com.github.rpc.utils.PropertiesHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcClient {

    private static final Logger logger = LoggerFactory.getLogger(RpcServer.class);

    private RpcClientContext context;


    public RpcClient() {
        context = new RpcClientContext();
        context.setServiceRegistry(RegistryFactory.getRegistry(PropertiesHelper.getValue("jworks-rpc.registry.address")));
        context.setNotifyChangeListener(new RemoteServerChangeListener(context));
        context.setRemoteServer(new NettyRemoteServer(context));
        context.setLoadBalance(new RoundLoadBalance());
        context.setProxyHandler(new CglibClientProxyHandler(context));
    }

    public <T> T proxy(String serverName, Class<?> cls) throws Throwable {
        context.getServiceRegistry().subscribe(serverName, context.getNotifyChangeListener());
        return context.getProxyHandler().proxy(serverName, cls);
    }

    public <T> T proxy(Class<?> cls) throws Throwable {
        context.getServiceRegistry().subscribe(cls.getName(), context.getNotifyChangeListener());
        return context.getProxyHandler().proxy(cls.getName(), cls);
    }

    private static class SingletonHolder {
        private static RpcClient instance = new RpcClient();
    }

    public static RpcClient getInstance() {
        return RpcClient.SingletonHolder.instance;
    }
}