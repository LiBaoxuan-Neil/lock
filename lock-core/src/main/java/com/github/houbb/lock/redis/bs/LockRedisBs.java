package com.github.houbb.lock.redis.bs;

import com.github.houbb.id.api.Id;
import com.github.houbb.id.core.core.Ids;
import com.github.houbb.lock.api.core.ILock;
import com.github.houbb.lock.redis.core.LockRedis;
import com.github.houbb.lock.redis.support.operator.IOperator;
import com.github.houbb.wait.api.IWait;
import com.github.houbb.wait.core.Waits;

/**
 * 引导类
 * @author binbin.hou
 * @since 0.0.1
 */
public final class LockRedisBs {

    private LockRedisBs(){}

    /**
     * 等待实现
     * @since 0.0.1
     */
    private IWait wait = Waits.threadSleep();

    /**
     * 等待实现
     * @since 0.0.1
     */
    private Id id = Ids.uuid32();

    /**
     * 操作类
     * @since 0.0.1
     */
    private IOperator operator;

    /**
     * 新建对象实例
     * @return 对象实例
     * @since 0.0.1
     */
    public static LockRedisBs newInstance() {
        return new LockRedisBs();
    }

    public LockRedisBs wait(IWait wait) {
        this.wait = wait;
        return this;
    }

    public LockRedisBs id(Id id) {
        this.id = id;
        return this;
    }

    public LockRedisBs operator(IOperator operator) {
        this.operator = operator;
        return this;
    }

    /**
     * 创建锁
     * @return 锁
     * @since 0.0.1
     */
    public ILock lock() {
        return new LockRedis(wait, operator, id);
    }

}
