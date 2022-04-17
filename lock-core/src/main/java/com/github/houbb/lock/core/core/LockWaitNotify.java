package com.github.houbb.lock.core.core;

import com.github.houbb.lock.core.exception.LockRuntimeException;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 等待通知的锁实现
 * @author binbin.hou
 * @since 0.0.2
 */
public class LockWaitNotify extends AbstractLock {

    private static final Log log = LogFactory.getLog(LockWaitNotify.class);

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
        // CAS
        boolean result = owner.compareAndSet(null, current);
        log.debug("尝试获取锁结果：{}", result);
        return result;
    }

    @Override
    public synchronized void unlock(String key) {
        Thread current = Thread.currentThread();
        boolean result = owner.compareAndSet(current, null);
        if(!result) {
            throw new LockRuntimeException("解锁失败");
        }

        // 唤醒等待中的线程
        log.debug("唤醒等待的进程");
        notify();
    }

}
