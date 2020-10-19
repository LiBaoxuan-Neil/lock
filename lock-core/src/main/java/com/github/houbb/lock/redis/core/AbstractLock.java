package com.github.houbb.lock.redis.core;

import com.github.houbb.lock.api.core.ILock;
import com.github.houbb.lock.redis.constant.LockRedisConst;
import com.github.houbb.wait.api.IWait;
import com.github.houbb.wait.core.Waits;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

/**
 * 抽象实现
 * @author binbin.hou
 * @since 0.0.1
 */
public abstract class AbstractLock implements ILock {

    /**
     * 锁等待
     * @since 0.0.1
     */
    private final IWait wait;

    public AbstractLock() {
        this.wait = Waits.threadSleep();
    }

    protected AbstractLock(IWait wait) {
        this.wait = wait;
    }

    @Override
    public void lock() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean tryLock() {
        return tryLock(LockRedisConst.DEFAULT_KEY);
    }

    @Override
    public void unlock() {
        unlock(LockRedisConst.DEFAULT_KEY);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit, String key) throws InterruptedException {
        long startTimeMills = System.currentTimeMillis();

        // 一次获取，直接成功
        boolean result = this.tryLock(key);
        if(result) {
            return true;
        }

        // 时间判断
        if(time <= 0) {
            return false;
        }
        long durationMills = unit.toMillis(time);
        long endMills = startTimeMills + durationMills;

        // 循环等待
        while (System.currentTimeMillis() < endMills) {
            result = tryLock(key);
            if(result) {
                return true;
            }

            // 等待 1ms
            wait.wait(TimeUnit.MILLISECONDS, 1);
        }
        return false;
    }

    @Override
    public synchronized boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return tryLock(time, unit, LockRedisConst.DEFAULT_KEY);
    }

    @Override
    public Condition newCondition() {
        throw new UnsupportedOperationException();
    }

}
