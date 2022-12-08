package com.github.houbb.lock.springboot.starter.config;

import com.github.houbb.lock.spring.annotation.EnableLock;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

/**
 * 分布式锁自动配置
 * @author binbin.hou
 * @since 1.0.0
 */
@Configuration
@ConditionalOnClass(LockAutoConfig.class)
@EnableLock
public class LockAutoConfig {
}
