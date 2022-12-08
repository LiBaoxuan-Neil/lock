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
     * 1. 如果等待时间小于等于0，则直接执行加锁
     * 2. 如果等待时间大于等于0，则尝试循环等待。
     *
     * @param context 上下文
     * @return 返回
     * @since 0.0.1
     */
    boolean tryLock(final ILockSupportContext context);

    /**
     * 解锁
     * @param context 上下文
     * @since 0.0.1
     * @return 结果
     */
    boolean unlock(final ILockSupportContext context);

}
