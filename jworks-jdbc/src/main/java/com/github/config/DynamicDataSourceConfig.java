package com.github.config;

import com.github.dynamic.datasource.DynamicDataSourceRegister;
import com.github.dynamic.intercept.DynamicDataSourceAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by jinwei.li on 2017/5/12 0012.
 */
@ConditionalOnProperty(prefix = "customized.datasource", value = {"names"})
@Import(DynamicDataSourceRegister.class)
@Configuration
public class DynamicDataSourceConfig {

    @Bean("dynamicDataSourceAspect")
    public DynamicDataSourceAspect dynamicDataSourceAspect() {
        DynamicDataSourceAspect aspect = new DynamicDataSourceAspect();
        return aspect;
    }
}
