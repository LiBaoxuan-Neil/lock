package com.github.houbb.lock.api.core;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁接口定义
 * @author binbin.hou
 * @since 0.0.1
 */
public interface ILock  {

    /**
     * 尝试加锁，如果失败，会一直尝试。
     *
     * @param key key
     * @param timeUnit 时间单位
     * @param waitLockTime 等待锁时间
     * @param lockTime 加锁时间
     * @return 返回
     * @since 0.0.1
     */
    boolean tryLock(String key, TimeUnit timeUnit, long lockTime, long waitLockTime);

    /**
     * 尝试加锁，只加锁一次
     * @param key key
     * @param timeUnit 时间单位
     * @param lockTime 加锁时间
     * @return 返回
     * @since 0.0.1
     */
    boolean tryLock(String key, TimeUnit timeUnit, long lockTime);

    /**
     * 尝试加锁，只加锁一次
     * @param key key
     * @param lockTime 加锁时间
     * @return 返回
     * @since 0.0.1
     */
    boolean tryLock(String key, long lockTime);

    /**
     * 尝试加锁，只加锁一次
     * @param key key
     * @return 返回
     * @since 0.0.1
     */
    boolean tryLock(String key);

    /**
     * 解锁
     * @param key key
     * @since 0.0.1
     * @return 是否释放锁成功
     */
    boolean unlock(String key);

}
