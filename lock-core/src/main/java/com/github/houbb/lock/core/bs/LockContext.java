package com.github.houbb.lock.core.bs;

import com.github.houbb.lock.api.core.ILockContext;
import com.github.houbb.lock.core.constant.LockConst;

import java.util.concurrent.TimeUnit;

/**
 * 加锁上下文
 * @author dh
 * @since 1.5.0
 */
public class LockContext implements ILockContext {

    /**
     * 缓存对应的 key
     */
    private String key;

    /**
     * 时间单位
     */
    private TimeUnit timeUnit = LockConst.DEFAULT_TIME_UNIT;

    /**
     * 加锁时间
     */
    private long lockTime = LockConst.DEFAULT_LOCK_TIME;

    /**
     * 等待加锁时间
     */
    private long waitLockTime = LockConst.DEFAULT_WAIT_LOCK_TIME;



    /**
     * 是否可以重入获取
     */
    private boolean reentrant = LockConst.DEFAULT_REENTRANT;


    public static LockContext newInstance() {
        return new LockContext();
    }

    @Override
    public String key() {
        return key;
    }

    public LockContext key(String key) {
        this.key = key;
        return this;
    }

    @Override
    public TimeUnit timeUnit() {
        return timeUnit;
    }

    public LockContext timeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
        return this;
    }

    @Override
    public long lockTime() {
        return lockTime;
    }

    public LockContext lockTime(long lockTime) {
        this.lockTime = lockTime;
        return this;
    }

    @Override
    public long waitLockTime() {
        return waitLockTime;
    }

    public LockContext waitLockTime(long waitLockTime) {
        this.waitLockTime = waitLockTime;
        return this;
    }

    @Override
    public boolean reentrant() {
        return reentrant;
    }

    public LockContext reentrant(boolean reentrant) {
        this.reentrant = reentrant;
        return this;
    }

}
