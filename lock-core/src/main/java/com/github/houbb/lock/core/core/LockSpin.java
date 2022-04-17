package com.github.houbb.lock.core.core;

import com.github.houbb.lock.core.exception.LockRuntimeException;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 自旋锁
 * @author binbin.hou
 * @since 0.0.2
 */
public class LockSpin extends AbstractLock {

    /**
     * volatile 引用，保证线程间的可见性+易变性
     *
     * @since 0.0.2
     */
    private AtomicReference<Thread> owner =new AtomicReference<>();

    @Override
    public void lock() {
        // 循环等待，直到获取到锁
        while (!tryLock()) {
        }
    }

    @Override
    public boolean tryLock(String key) {
        Thread current = Thread.currentThread();
        // CAS
        return owner.compareAndSet(null, current);
    }

    @Override
    public void unlock(String key) {
        Thread current = Thread.currentThread();
        boolean result = owner.compareAndSet(current, null);
        if(!result) {
            throw new LockRuntimeException("解锁失败");
        }
    }

}
