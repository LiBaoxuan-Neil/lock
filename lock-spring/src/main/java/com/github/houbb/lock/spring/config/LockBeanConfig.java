package com.github.houbb.lock.spring.config;

import com.github.houbb.common.cache.api.service.ICommonCacheService;
import com.github.houbb.heaven.util.lang.StringUtil;
import com.github.houbb.id.api.Id;
import com.github.houbb.id.core.core.Ids;
import com.github.houbb.redis.config.core.factory.JedisRedisServiceFactory;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${redis.address:127.0.0.1}")
    private String redisAddress;

    @Value("${redis.port:6379}")
    private int redisPort;

    @Value("${redis.password:}")
    private String redisPassword;

    @Bean("lockCache")
    public ICommonCacheService lockCache() {
        if(StringUtil.isNotEmpty(redisPassword)) {
            return JedisRedisServiceFactory.pooled(redisAddress, redisPort, redisPassword);
        }

        return JedisRedisServiceFactory.simple(redisAddress, redisPort);
    }

    @Bean("lockId")
    public Id lockId() {
        return Ids.uuid32();
    }

}
