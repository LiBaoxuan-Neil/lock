package com.github.houbb.lock.spring.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 分布式加锁注解
 * @author binbin.hou
 * @since 0.0.2
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Lock {

    /**
     * 缓存的 key 策略，支持 SpEL
     * @return 结果
     */
    String value() default "";

    /**
     * 时间单位
     * @return 单位
     * @since 1.2.0
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 等待锁时间
     * @return 等待锁时间
     * @since 1.2.0
     */
    long waitLockTime() default 10;

    /**
     * 业务加锁时间
     * @return 加锁时间
     * @since 1.2.0
     */
    long lockTime() default 60;

}
