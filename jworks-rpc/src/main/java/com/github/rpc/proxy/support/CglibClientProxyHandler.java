package com.github.rpc.proxy.support;

import com.github.rpc.context.RpcClientContext;
import com.github.rpc.context.RpcContext;
import com.github.rpc.model.RpcFuture;
import com.github.rpc.model.RpcRequest;
import com.github.rpc.proxy.ProxyHandler;
import com.github.rpc.remote.RemoteServer;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

/**
 * Created by jinwei.li on 2017/6/27 0027.
 */
public class CglibClientProxyHandler implements ProxyHandler {


    private Logger logger = LoggerFactory.getLogger(getClass());
    private RpcClientContext context;

    public CglibClientProxyHandler(RpcClientContext context) {
        this.context = context;
        context.setProxyHandler(this);
    }

    @Override
    public <T> T proxy(final String serverName, Class<?> cls) throws Throwable {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(cls);
        // 设置回调方法
        enhancer.setCallback(new MethodInterceptor() {

            @Override
            public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                RpcRequest request = new RpcRequest(); // 创建并初始化 RPC 请求
                request.setRequestId(UUID.randomUUID().toString());
                String className = method.getDeclaringClass().getName();
                request.setServerName(serverName);
                request.setClassName(className);
                request.setMethodName(method.getName());
                request.setParameterTypes(method.getParameterTypes());
                request.setParameters(args);
                List<String> remoteAddresses = context.getRemoteAddress(serverName);
                String remoteAddress = context.getLoadBalance().select(remoteAddresses, className);
                logger.info("remote address:{}", remoteAddress);
                String[] array = remoteAddress.split(":");
                String host = array[0];
                int port = Integer.parseInt(array[1]);
                request.setHost(host);
                request.setPort(port);
                //TODO  解耦
                RemoteServer client = context.getRemoteServer(); // 初始化 RPC 客户端
                RpcFuture future = client.send(request); // 通过 RPC 客户端发送 RPC 请求并获取 RPC 响应
                while (!future.isDone()) {
                    Thread.sleep(100);
                }
                RpcContext.rpcFutures.remove(request.getRequestId());
                return future.get();
            }
        });
        // 创建代理对象
        return (T) enhancer.create();
    }

    @Override
    public Object invoke(RpcRequest request) throws Throwable {
        return null;
    }
}
