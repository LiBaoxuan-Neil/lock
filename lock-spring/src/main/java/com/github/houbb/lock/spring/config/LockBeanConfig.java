package com.github.houbb.lock.spring.config;

import com.github.houbb.id.api.Id;
import com.github.houbb.id.core.core.Ids;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * bean 配置
 *
 * @author binbin.hou
 * @since 0.0.2
 */
@Configuration
@ComponentScan(basePackages = "com.github.houbb.lock.spring")
public class LockBeanConfig {

    @Bean("lockId")
    public Id lockId() {
        return Ids.uuid32();
    }

}
