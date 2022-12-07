package com.github.houbb.lock.api.core;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁接口定义
 * @author binbin.hou
 * @since 0.0.1
 */
public interface ILockSupport {

    /**
     * 尝试加锁，如果失败，会一直尝试。
     *
     * @param time 时间
     * @param unit 单位
     * @param key key
     * @param context 上下文
     * @return 返回
     * @since 0.0.1
     */
    boolean tryLock(long time, TimeUnit unit, String key, final ILockSupportContext context);

    /**
     * 尝试加锁，只加锁一次
     * @param key key
     * @param context 上下文
     * @return 返回
     * @since 0.0.1
     */
    boolean tryLock(String key, final ILockSupportContext context);

    /**
     * 解锁
     * @param key key
     * @param context 上下文
     * @since 0.0.1
     */
    boolean unlock(String key, final ILockSupportContext context);

}
