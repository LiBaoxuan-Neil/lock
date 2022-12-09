package com.github.houbb.lock.spring.annotation;

import com.github.houbb.lock.core.constant.LockConst;

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
    long waitLockTime() default LockConst.DEFAULT_WAIT_LOCK_TIME;

    /**
     * 业务加锁时间
     * @return 加锁时间
     * @since 1.2.0
     */
    long lockTime() default LockConst.DEFAULT_LOCK_TIME;

    /**
     * 是否可以重入获取
     * @return 结果
     * @since 1.5.0
     */
    boolean reentrant() default LockConst.DEFAULT_REENTRANT;

}
