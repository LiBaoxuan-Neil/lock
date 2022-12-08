package com.github.houbb.lock.core.support.handler;

import com.github.houbb.lock.api.core.ILockReleaseFailHandlerContext;

/**
 * 锁释放失败上下文
 * @since 1.2.0
 */
public class LockReleaseFailHandlerContext implements ILockReleaseFailHandlerContext {

    /**
     * 释放锁对应的 key
     * @since 1.2.0
     */
    private String key;

    public static LockReleaseFailHandlerContext newInstance() {
        return new LockReleaseFailHandlerContext();
    }

    @Override
    public String key() {
        return key;
    }

    public LockReleaseFailHandlerContext key(String key) {
        this.key = key;
        return this;
    }

}
