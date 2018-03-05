package com.github.rpc;

import com.github.rpc.context.RpcServerContext;
import com.github.rpc.proxy.support.CglibServerProxyHandler;
import com.github.rpc.register.RegistryFactory;
import com.github.rpc.remote.support.NettyRemoteServer;
import com.github.rpc.utils.HostUtils;
import com.github.rpc.utils.PropertiesHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class RpcServer {

    private static final Logger logger = LoggerFactory.getLogger(RpcServer.class);

    private RpcServerContext context;
    private String host;
    private int port;
    private Map<String, Object> handlerMap = new HashMap<>(); // 存放接口名与服务对象之间的映射关系

    public RpcServer() {
        context = new RpcServerContext();
        this.host = PropertiesHelper.getValue("jworks-rpc.host") == null ? HostUtils.IP : PropertiesHelper.getValue("jworks-rpc.host");
        this.port = PropertiesHelper.getIntValue("jworks-rpc.port") == -1 ? 9999 : PropertiesHelper.getIntValue("jworks-rpc.port");
        context.setServiceRegistry(RegistryFactory.getRegistry(PropertiesHelper.getValue("jworks-rpc.registry.address")));
        context.setProxyHandler(new CglibServerProxyHandler(handlerMap));
        context.setRemoteServer(new NettyRemoteServer(context, host, port));
        try {
            context.getRemoteServer().start();
            logger.info("服务启动成功,host:{},port:{}", host, port);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("服务启动出错：{}", e);
        }
    }

    /**
     * 注册服务
     *
     * @param cls
     */
    public void register(Class<?> cls) {
        try {
            String serviceName = cls.getName();
            handlerMap.put(cls.getName(), cls.newInstance());
            //注册服务，格式为/serviceName/providers/serverAddress,/serviceName/consumers/serverAddress
            context.getServiceRegistry().register("/" + serviceName + "/providers/" + host + ":" + port); // 注册服务地址
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 注册服务
     *
     * @param serviceName
     * @param bean
     */
    public void register(String serviceName, Object bean) {
        if (bean instanceof Class) {
            try {
                handlerMap.put(serviceName, ((Class) bean).newInstance());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else
            handlerMap.put(serviceName, bean);
        //注册服务，格式为/serviceName/providers/serverAddress,/serviceName/consumers/serverAddress
        context.getServiceRegistry().register("/" + serviceName + "/providers/" + host + ":" + port); // 注册服务地址
    }


    private static class SingletonHolder {
        private static RpcServer instance = new RpcServer();
    }

    public static RpcServer getInstance() {
        return RpcServer.SingletonHolder.instance;
    }
}