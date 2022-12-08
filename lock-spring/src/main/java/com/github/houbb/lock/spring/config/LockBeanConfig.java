package com.github.houbb.lock.spring.config;

import com.github.houbb.id.api.Id;
import com.github.houbb.id.core.core.Ids;
import com.github.houbb.lock.api.core.ILockKeyFormat;
import com.github.houbb.lock.api.core.ILockReleaseFailHandler;
import com.github.houbb.lock.core.support.format.LockKeyFormat;
import com.github.houbb.lock.core.support.handler.LockReleaseFailHandler;
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

    @Bean("lockKeyFormat")
    public ILockKeyFormat lockKeyFormat() {
        return new LockKeyFormat();
    }

    @Bean("lockReleaseFailHandler")
    public ILockReleaseFailHandler lockReleaseFailHandler() {
        return new LockReleaseFailHandler();
    }

}
