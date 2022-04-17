package com.github.houbb.lock.core.core;

import com.github.houbb.heaven.util.util.DateUtil;
import com.github.houbb.lock.core.exception.LockRuntimeException;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 自旋锁-可重入
 * @author binbin.hou
 * @since 0.0.2
 */
public class LockSpinRe extends AbstractLock {

    /**
     * volatile 引用，保证线程间的可见性+易变性
     *
     * @since 0.0.2
     */
    private AtomicReference<Thread> owner =new AtomicReference<>();

    /**
     * 计数统计类
     *
     * @since 0.0.2
     */
    private AtomicLong count = new AtomicLong(0);

    @Override
    public void lock() {
        // 循环等待，直到获取到锁
        while (!tryLock()) {
            // sleep
            DateUtil.sleep(1);
        }
    }

    @Override
    public boolean tryLock(String key) {
        Thread current = Thread.currentThread();
        // 判断是否已经拥有此锁
        if(current == owner.get()) {
            // 原子性自增 1
            count.incrementAndGet();

            return true;
        }

        // CAS
        return owner.compareAndSet(null, current);
    }

    @Override
    public void unlock(String key) {
        Thread current = Thread.currentThread();

        // 可重入实现
        if(owner.get() == current && count.get() != 0) {
            count.decrementAndGet();
            return;
        }

        boolean result = owner.compareAndSet(current, null);
        if(!result) {
            throw new LockRuntimeException("解锁失败");
        }
    }

}
