package com.github.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by jinwei.li on 2017/5/8 0008.
 */
@Configuration
@ConditionalOnResource(resources = {"classpath:dubbo/spring-dubbo.xml"})
@ImportResource(locations = {"classpath:dubbo/spring-dubbo.xml"})
public class DubboXMLConfiguration {
}
