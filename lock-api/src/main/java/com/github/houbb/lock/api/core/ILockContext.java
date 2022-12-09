package com.github.houbb.lock.api.core;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁上下文接口
 * @author binbin.hou
 * @since 1.5.0
 */
public interface ILockContext {

    /**
     * 时间单位
     * @return 时间单位
     */
    TimeUnit timeUnit();

    /**
     * 加锁时间
     * @return 时间
     * @since 1.2.0
     */
    long lockTime();

    /**
     * 等待加锁时间
     * @return 等待时间
     * @since 1.2.0
     */
    long waitLockTime();

    /**
     * 缓存对应的 key
     * @return 结果
     * @since 1.2.0
     */
    String key();

    /**
     * 是否可以重入获取
     * @return 结果
     * @since 1.5.0
     */
    boolean reentrant();

}
