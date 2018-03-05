package com.github.utils.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * spring context获取bean
 */
@Component
@Lazy(false)
public class SpringContextHolder implements ApplicationContextAware, DisposableBean {
    private static ApplicationContext context;
    private static Logger logger = LoggerFactory.getLogger(SpringContextHolder.class);
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context=applicationContext;
    }

    public static ApplicationContext getContext(){
        return context;
    }

    @Override
    public void destroy() throws Exception {
        SpringContextHolder.clearHolder();
    }

    public static Object getBean(String beanName) {
        if(!getContext().containsBean(beanName)) {
            if(!beanName.substring(0, 1).toLowerCase().equals(beanName.substring(0, 1))) {
                beanName = beanName.substring(0, 1).toLowerCase() + beanName.substring(1);
            } else if(!beanName.substring(0, 1).toUpperCase().equals(beanName.substring(0, 1))) {
                beanName = beanName.substring(0, 1).toUpperCase() + beanName.substring(1);
            }
        }

        return getContext().getBean(beanName);
    }

    public static <T> T getBean(String beanName, Class<T> clazz) {
        return getContext().getBean(beanName,clazz);
    }

    public static <T> T getBean(Class<T> clazz) {
        String[] beans = getContext().getBeanNamesForType(clazz);
        return beans != null && beans.length > 0?getBean(beans[0], clazz):null;
    }

    public static Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> clazz) {
        return getContext().getBeansWithAnnotation(clazz);
    }

    /**
     * 清除SpringContextHolder中的ApplicationContext为Null.
     */
    public static void clearHolder() {
        if (logger.isDebugEnabled()){
            logger.debug("清除SpringContextHolder中的ApplicationContext:" + context);
        }
        context = null;
    }
}
