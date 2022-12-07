package com.github.houbb.lock.spring.annotation;

import java.lang.annotation.*;

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
     * 尝试获取锁等待时间
     * @return 结果
     */
    long tryLockMills() default 10 * 1000;

}
