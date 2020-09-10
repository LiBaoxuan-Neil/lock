package com.github.houbb.lock.api.core;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * 锁定义
 * @author binbin.hou
 * @since 0.0.1
 */
public interface ILock extends Lock {

    /**
     * 尝试加锁
     * @param time 时间
     * @param unit 当为
     * @param key key
     * @return 返回
     * @throws InterruptedException 异常
     * @since 0.0.1
     */
    boolean tryLock(long time, TimeUnit unit,
                    String key) throws InterruptedException;

    /**
     * 尝试加锁
     * @param key key
     * @return 返回
     * @since 0.0.1
     */
    boolean tryLock(String key);

    /**
     * 解锁
     * @param key key
     * @since 0.0.1
     */
    void unlock(String key);

}
