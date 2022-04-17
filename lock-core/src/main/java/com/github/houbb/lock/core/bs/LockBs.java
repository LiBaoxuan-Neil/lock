package com.github.houbb.lock.core.bs;

import com.github.houbb.heaven.util.common.ArgUtil;
import com.github.houbb.id.api.Id;
import com.github.houbb.id.core.core.Ids;
import com.github.houbb.lock.api.core.ILock;
import com.github.houbb.lock.api.support.IOperator;
import com.github.houbb.lock.core.support.simple.SimpleLock;
import com.github.houbb.wait.api.IWait;
import com.github.houbb.wait.core.Waits;

/**
 * 锁引导类
 *
 * @author binbin.hou
 * @since 0.0.4
 */
public final class LockBs {

    private LockBs(){}

    /**
     * 清空初始化延迟时间
     * @since 0.0.4
     */
    private long clearInitDelaySeconds = 60;

    /**
     * 清空初始化周期
     * @since 0.0.4
     */
    private long clearPeriodSeconds = 60;

    /**
     * 是否启用清空任务
     * @since 0.0.4
     */
    private boolean enableClearTask = true;

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

    public static LockBs newInstance(final IOperator operator) {
        return new LockBs().operator(operator);
    }

    public LockBs clearInitDelaySeconds(long clearInitDelaySeconds) {
        this.clearInitDelaySeconds = clearInitDelaySeconds;
        return this;
    }

    public LockBs clearPeriodSeconds(long clearPeriodSeconds) {
        this.clearPeriodSeconds = clearPeriodSeconds;
        return this;
    }

    public LockBs enableClearTask(boolean enableClearTask) {
        this.enableClearTask = enableClearTask;
        return this;
    }

    public LockBs waits(IWait waits) {
        ArgUtil.notNull(waits, "waits");

        this.waits = waits;
        return this;
    }

    public LockBs id(Id id) {
        ArgUtil.notNull(id, "id");

        this.id = id;
        return this;
    }

    public LockBs operator(IOperator operator) {
        ArgUtil.notNull(operator, "operator");

        this.operator = operator;
        return this;
    }

    public ILock lock() {
        ArgUtil.notNull(operator, "operator");

        return SimpleLock.newInstance()
                .waits(waits)
                .id(id)
                .operator(operator)
                .enableClearTask(enableClearTask)
                .clearInitDelaySeconds(clearInitDelaySeconds)
                .clearPeriodSeconds(clearPeriodSeconds)
                .init();
    }

}
