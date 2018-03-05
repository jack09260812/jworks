package com.github.rpc.spring;

import com.github.rpc.RpcServer;
import com.github.rpc.spring.annotation.RpcService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by jinwei.li on 2017/7/17 0017.
 */
@Component
public class RpcServerAnnotationBeanProcessor implements ApplicationContextAware, InitializingBean {
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(RpcService.class);
        for (Map.Entry<String, Object> entry : serviceBeanMap.entrySet()) {
            RpcServer.getInstance().register(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
