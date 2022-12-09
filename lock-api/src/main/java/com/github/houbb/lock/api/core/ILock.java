package com.github.houbb.lock.api.core;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁接口定义
 * @author binbin.hou
 * @since 0.0.1
 */
public interface ILock  {

    /**
     * 尝试获取锁，避免参数过多
     * @param context 上下文
     * @return 结果
     * @since 1.5.0
     */
    boolean tryLock(final ILockContext context);

    /**
     * 尝试加锁，如果失败，在 waitLockTime 到达之前，会一直尝试。
     *
     * @param key key
     * @param timeUnit 时间单位
     * @param waitLockTime 等待锁时间
     * @param lockTime 加锁时间
     * @param reentrant 是否可以重入获取
     * @return 返回
     * @since 1.5.0
     */
    boolean tryLock(String key, TimeUnit timeUnit, long lockTime, long waitLockTime, boolean reentrant);

    /**
     * 尝试加锁。reentrant=true，默认可重入
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
     * 尝试加锁。waitLockTime=0，只进行一次尝试。
     * @param key key
     * @param timeUnit 时间单位
     * @param lockTime 加锁时间
     * @return 返回
     * @since 0.0.1
     */
    boolean tryLock(String key, TimeUnit timeUnit, long lockTime);

    /**
     * 尝试加锁。timeUnit = TimeUnit.SECONDS，时间单位默认为秒。
     * @param key key
     * @param lockTime 加锁时间
     * @return 返回
     * @since 0.0.1
     */
    boolean tryLock(String key, long lockTime);

    /**
     * 尝试加锁。lockTime=10，默认等待10S
     * @param key key
     * @return 返回
     * @since 0.0.1
     */
    boolean tryLock(String key);

    /**
     * 解锁
     *
     * ps: 目前释放锁不会进行重试。所有的 key 有过期时间。
     * @param key key
     * @since 0.0.1
     * @return 是否释放锁成功
     */
    boolean unlock(String key);

}
