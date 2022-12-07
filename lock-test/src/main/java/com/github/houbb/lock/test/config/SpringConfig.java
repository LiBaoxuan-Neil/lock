package com.github.houbb.lock.test.config;


import com.github.houbb.lock.spring.annotation.EnableLock;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author binbin.hou
 * @since 1.0.0
 */
@Configurable
@ComponentScan(basePackages = "com.github.houbb.lock.test.service")
@EnableLock
public class SpringConfig {
}
