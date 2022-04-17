package com.github.houbb.lock.core.core;

import com.github.houbb.heaven.util.common.ArgUtil;
import com.github.houbb.id.api.Id;
import com.github.houbb.id.core.core.Ids;
import com.github.houbb.lock.api.core.ILock;
import com.github.houbb.lock.api.support.IOperator;
import com.github.houbb.lock.core.constant.LockConst;
import com.github.houbb.wait.api.IWait;
import com.github.houbb.wait.core.Waits;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
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
    protected IWait waits = Waits.threadSleep();

    /**
     * 标识策略
     * @since 0.0.4
     */
    protected Id id = Ids.uuid32();

    /**
     * 操作策略
     * @since 0.0.4
     */
    protected IOperator operator;

    /**
     * 清空初始化延迟时间
     * @since 0.0.4
     */
    private long clearInitDelaySeconds = 5;

    /**
     * 清空初始化周期
     * @since 0.0.4
     */
    private long clearPeriodSeconds = 5;

    /**
     * 是否启用清空任务
     * @since 0.0.4
     */
    private boolean enableClearTask = true;

    public AbstractLock waits(IWait waits) {
        this.waits = waits;
        return this;
    }

    public AbstractLock id(Id id) {
        this.id = id;
        return this;
    }

    public AbstractLock operator(IOperator operator) {
        this.operator = operator;
        return this;
    }

    public AbstractLock clearInitDelaySeconds(long clearInitDelaySeconds) {
        this.clearInitDelaySeconds = clearInitDelaySeconds;
        return this;
    }

    public AbstractLock clearPeriodSeconds(long clearPeriodSeconds) {
        this.clearPeriodSeconds = clearPeriodSeconds;
        return this;
    }

    public AbstractLock enableClearTask(boolean enableClearTask) {
        this.enableClearTask = enableClearTask;
        return this;
    }

    /**
     * 初始化
     * @since 0.0.4
     */
    public AbstractLock init() {
        // 参数校验
        ArgUtil.notNull(operator, "operator");

        // 初始化任务
        initClearExpireKey();

        return this;
    }

    /**
     * 初始化清空任务
     * @since 0.0.6
     */
    private void initClearExpireKey() {
        if(!enableClearTask) {
            return;
        }

        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        //5S 清理一次
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                operator.clearExpireLock();
            }
        }, clearInitDelaySeconds, clearPeriodSeconds, TimeUnit.SECONDS);
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
        return tryLock(LockConst.DEFAULT_KEY);
    }

    @Override
    public void unlock() {
        unlock(LockConst.DEFAULT_KEY);
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
            waits.wait(TimeUnit.MILLISECONDS, 1);
        }
        return false;
    }

    @Override
    public synchronized boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return tryLock(time, unit, LockConst.DEFAULT_KEY);
    }

    @Override
    public Condition newCondition() {
        throw new UnsupportedOperationException();
    }

}
