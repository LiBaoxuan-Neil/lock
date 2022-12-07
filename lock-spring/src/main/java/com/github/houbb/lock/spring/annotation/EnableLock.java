package com.github.houbb.lock.spring.annotation;

import com.github.houbb.lock.spring.config.LockAopConfig;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用注解
 * @author binbin.hou
 * @since 0.0.2
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(LockAopConfig.class)
@EnableAspectJAutoProxy
public @interface EnableLock {

    /**
     * 加锁过期时间
     * @return 时间
     * @since 1.1.0
     */
    int lockExpireMills() default 60 * 1000;

    /**
     * 唯一标识生成策略
     * @return 结果
     * @since 1.1.0
     */
    String id() default "lockId";

    /**
     * 缓存实现策略 bean 名称
     * @return 实现
     * @since 1.1.0
     */
    String cache() default "lockCache";


}
