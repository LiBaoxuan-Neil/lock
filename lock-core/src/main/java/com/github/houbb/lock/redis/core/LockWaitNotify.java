package com.github.houbb.lock.redis.core;

import com.github.houbb.lock.redis.exception.LockRuntimeException;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 等待通知的锁实现
 * @author binbin.hou
 * @since 0.0.2
 */
public class LockWaitNotify extends AbstractLock {

    /**
     * volatile 引用，保证线程间的可见性+易变性
     *
     * @since 0.0.2
     */
    private AtomicReference<Thread> owner =new AtomicReference<>();

    @Override
    public synchronized void lock() {
        while (!tryLock()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                // 是否可以被打断
            }
        }
    }

    @Override
    public boolean tryLock(String key) {
        Thread current = Thread.currentThread();
        // CAS
        return owner.compareAndSet(null, current);
    }

    @Override
    public synchronized void unlock(String key) {
        Thread current = Thread.currentThread();
        boolean result = owner.compareAndSet(current, null);
        if(!result) {
            throw new LockRuntimeException("解锁失败");
        }

        // 唤醒等待中的线程
        notifyAll();
    }

}
