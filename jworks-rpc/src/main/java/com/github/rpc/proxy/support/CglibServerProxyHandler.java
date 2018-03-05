package com.github.rpc.proxy.support;

import com.github.rpc.model.RpcRequest;
import com.github.rpc.proxy.ProxyHandler;
import com.google.common.base.Preconditions;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by jinwei.li on 2017/6/27 0027.
 */
public class CglibServerProxyHandler implements ProxyHandler {


    private Logger logger = LoggerFactory.getLogger(getClass());

    //保存服务名称和实现类映射
    private Map<String, Object> handlerMap;

    public CglibServerProxyHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    public Object proxy(RpcRequest request) throws Throwable {
        String className = request.getClassName();
        logger.debug("服务名称为：{}", className);
        String serverName = request.getServerName();
        logger.debug("server name:{}", serverName);
        Object serviceBean = handlerMap.get(serverName == null ? className : serverName);
        if (serviceBean == null) {
            logger.error("服务：{}不在注册列表中", className);
            throw new IllegalArgumentException("服务不在注册列表中");
        }
        Class<?> serviceClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();
        FastClass serviceFastClass = FastClass.create(serviceClass);
        FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);
        return serviceFastMethod.invoke(serviceBean, parameters);
    }

    @Override
    public <T> T proxy(String serverName, Class<?> cls) throws Throwable {
        Preconditions.checkNotNull(handlerMap);
//        String className = cls.getName();
        return (T) handlerMap.get(serverName);
    }

    @Override
    public Object invoke(RpcRequest request) throws Throwable {
        Preconditions.checkNotNull(handlerMap);
        String className = request.getClassName();
        logger.debug("class name:{}", className);
        String serverName = request.getServerName();
        logger.debug("server name:{}", serverName);
        Object serviceBean = handlerMap.get(serverName == null ? className : serverName);
        Class<?> serviceClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        logger.debug("method name:{}", methodName);
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();
        FastClass serviceFastClass = FastClass.create(serviceClass);
        FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);
        return serviceFastMethod.invoke(serviceBean, parameters);
    }

}
