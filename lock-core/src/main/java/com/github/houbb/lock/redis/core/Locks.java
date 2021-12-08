package com.github.houbb.lock.redis.core;

import com.github.houbb.lock.api.core.ILock;
import com.github.houbb.lock.api.core.IReadWriteLock;

/**
 * 锁工具
 *
 * @author binbin.hou
 * @since 0.0.3
 */
public final class Locks {

    private Locks(){}

    /**
     * 无锁
     * @return 锁
     * @since 0.0.3
     */
    public static ILock none() {
        return new LockNone();
    }

    /**
     * 读写锁
     * @return 锁
     * @since 0.0.3
     */
    public static IReadWriteLock readWrite() {
        return new LockReadWrite();
    }

    /**
     * 读写锁
     * @return 锁
     * @since 0.0.3
     */
    public static IReadWriteLock readWriteOwner() {
        return new LockReadWriteOwner();
    }

    /**
     * 可重入读写锁
     * @return 锁
     * @since 0.0.3
     */
    public static IReadWriteLock readWriteRe() {
        return new LockReadWriteRe();
    }

    /**
     * 自旋锁
     * @return 锁
     * @since 0.0.3
     */
    public static ILock spin() {
        return new LockSpin();
    }

    /**
     * 可重入自旋锁
     * @return 锁
     * @since 0.0.3
     */
    public static ILock spinRe() {
        return new LockSpinRe();
    }

    /**
     * 等待通知锁
     * @return 锁
     * @since 0.0.3
     */
    public static ILock waitNotify() {
        return new LockWaitNotify();
    }

    /**
     * 可重入等待通知锁
     * @return 锁
     * @since 0.0.3
     */
    public static ILock waitNotifyRe() {
        return new LockWaitNotifyRe();
    }

}
