package com.github.houbb.lock.core.support.format;

import com.github.houbb.lock.api.core.ILockKeyFormatContext;

/**
 * 上下文
 * @since 1.2.0
 */
public class LockKeyFormatContext implements ILockKeyFormatContext {


    /**
     * 原始的 key
     *
     * @since 1.2.0
     */
    private String rawKey;

    /**
     * 锁 key 的默认命名空间
     * @since 1.4.0
     */
    private String lockKeyNamespace;

    public static LockKeyFormatContext newInstance() {
        return new LockKeyFormatContext();
    }

    @Override
    public String rawKey() {
        return rawKey;
    }

    public LockKeyFormatContext rawKey(String rawKey) {
        this.rawKey = rawKey;
        return this;
    }

    @Override
    public String lockKeyNamespace() {
        return lockKeyNamespace;
    }

    public LockKeyFormatContext lockKeyNamespace(String lockKeyNamespace) {
        this.lockKeyNamespace = lockKeyNamespace;
        return this;
    }
}
