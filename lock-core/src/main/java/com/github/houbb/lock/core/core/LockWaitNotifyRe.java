package com.github.houbb.lock.core.core;

import com.github.houbb.lock.core.exception.LockRuntimeException;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 等待通知的锁实现-可重入
 * @author binbin.hou
 * @since 0.0.2
 */
public class LockWaitNotifyRe extends AbstractLock {

    private static final Log log = LogFactory.getLog(LockWaitNotifyRe.class);

    /**
     * volatile 引用，保证线程间的可见性+易变性
     *
     * @since 0.0.2
     */
    private AtomicReference<Thread> owner =new AtomicReference<>();

    /**
     * 次数统计
     * @since 0.0.2
     */
    private AtomicInteger count = new AtomicInteger(0);

    @Override
    public synchronized void lock() {
        while (!tryLock()) {
            try {
                log.debug("等待被唤醒");
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

        //可重入实现
        if(current == owner.get()) {
            count.incrementAndGet();
            log.debug("当前线程已经拥有锁，直接返回 true");
            return true;
        }

        // CAS
        boolean result = owner.compareAndSet(null, current);
        log.debug("尝试获取锁结果：{}", result);
        return result;
    }

    @Override
    public synchronized void unlock(String key) {
        Thread current = Thread.currentThread();

        // 可重入实现
        if(owner.get() == current && count.get() != 0) {
            count.decrementAndGet();
            notifyAndLog();
            return;
        }

        boolean result = owner.compareAndSet(current, null);
        if(!result) {
            throw new LockRuntimeException("解锁失败");
        }

        notifyAndLog();
    }

    private void notifyAndLog() {
        // 唤醒等待中的线程
        log.debug("唤醒等待的进程");
        notify();
    }

}
