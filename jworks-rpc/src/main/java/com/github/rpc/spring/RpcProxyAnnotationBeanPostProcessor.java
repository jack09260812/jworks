package com.github.rpc.spring;

import com.github.rpc.RpcClient;
import com.github.rpc.spring.annotation.RpcProxy;
import org.springframework.beans.BeansException;
import org.springframework.beans.SimpleTypeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@Component
public class RpcProxyAnnotationBeanPostProcessor extends
        InstantiationAwareBeanPostProcessorAdapter {

    //创建简单类型转换器
    private SimpleTypeConverter typeConverter = new SimpleTypeConverter();

    @Override
    public boolean postProcessAfterInstantiation(final Object bean, String beanName)
            throws BeansException {
        ReflectionUtils.doWithFields(bean.getClass(), new ReflectionUtils.FieldCallback() {
            public void doWith(Field field) throws IllegalArgumentException,
                    IllegalAccessException {
                RpcProxy proxy = field.getAnnotation(RpcProxy.class);
                if (proxy != null) {
                    if (Modifier.isStatic(field.getModifiers())) {
                        throw new IllegalStateException("annotation is not supported on static fields ");
                    }

                    //如果开发者没有设置value，则使用变量域的名称作为键查找配置资源
                    String key = proxy.value().length() <= 0 ? field.getName() : proxy.value();
                    Object value = null;
                    try {
                        value = RpcClient.getInstance().proxy(field.getType());
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }

                    if (value != null) {
                        //转换配置值成其它非String类型
                        Object _value = typeConverter.convertIfNecessary(value, field.getType());
                        //使变量域可用，并且转换后的配置值注入其中
                        ReflectionUtils.makeAccessible(field);
                        field.set(bean, _value);
                    }
                }
            }
        });

        //通常情况下返回true即可
        return true;
    }
}