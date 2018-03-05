package com.github.config;

import com.alibaba.fastjson.JSON;
import com.github.filter.UrlMappingFilter;
import com.github.filter.XssFilter;
import com.github.interceptor.RequestHolderInterceptor;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.support.RequestContext;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 读取application.properties中的配置值方式：
 * <p>
 *
 * @Autowired private Environment env;
 * env.getProperty(key);
 * @value("${key:default") 查找value，如果没找到，则使用default作为默认值<p>
 * 读取配置文件方式：
 * <p>
 * @PropertySources({@PropertySource("classpath:config.properties"),@PropertySource("classpath:db.properties") })/@PropertySource("classpath:missing.properties")定义到class作用域，提供扩展配置文件（application.properties扩展）
 * <p>
 * <bean class="org.springframework.context.support.PropertySourcesPlaceholderConfigurer">
 * <property name="ignoreUnresolvablePlaceholders" value="true"/>
 * <property name="locations">
 * <list>
 * <value>classpath:spring/config.properties</value>
 * </list>
 * </property>
 * </bean>
 * <p>
 * @ConfigurationProperties(locations = "classpath:config.properties", prefix = "config")注解到class，class属性为配置属性值。
 * Created by jinwei.li on 2017/1/2.
 */
@Configuration
public class SpringMvcConfig extends WebMvcConfigurerAdapter {
    @Resource
    private Environment environment;

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        Iterator<HttpMessageConverter<?>> i = converters.iterator();
        converters.add(0, new UTF8StringHttpMessageConverter());
        super.extendMessageConverters(converters);
    }

    @Bean
    public FilterRegistrationBean urlMappingFilter() {
        Set<String> patternUrls = new HashSet<>();
        String excludePro = "jworks.filter.mapping.excludePath";
        String excludePaths = "";
        //是否生效，默认生效
        boolean invalid = false;
        if (environment.containsProperty("jworks.filter.mapping.invalid"))
            invalid = environment.getProperty("jworks.filter.mapping.invalid", Boolean.class);
        Set<PatternsRequestCondition> requestConditions = new HashSet<>();
        Map<RequestMappingInfo, HandlerMethod> map = this.handlerMapping.getHandlerMethods();
        for (RequestMappingInfo info : map.keySet()) {
            requestConditions.add(info.getPatternsCondition());
            patternUrls.addAll(info.getPatternsCondition().getPatterns());
        }
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        UrlMappingFilter mappingFilter = new UrlMappingFilter(invalid,requestConditions);
        Map<String, String> initParam = Maps.newHashMap();
        if (environment.containsProperty(excludePro))
            excludePaths = environment.getProperty(excludePro);
        initParam.put("patternUrls", JSON.toJSONString(patternUrls));
        initParam.put("excludePaths", excludePaths);
        registrationBean.setOrder(0);
        registrationBean.setFilter(mappingFilter);
        registrationBean.setInitParameters(initParam);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }

//    @Bean
//    public FilterRegistrationBean xssFilter() {
//        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
//        XssFilter xssFilter = new XssFilter();
//        Map<String, String> initParam = Maps.newHashMap();
//        initParam.put("whiteList", xssWhiteList);
//        registrationBean.setFilter(xssFilter);
//        registrationBean.setInitParameters(initParam);
//        registrationBean.addUrlPatterns("/*");
//        return registrationBean;
//    }
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        super.addInterceptors(registry);
//        registry.addInterceptor(new RequestHolderInterceptor());
//    }

}
