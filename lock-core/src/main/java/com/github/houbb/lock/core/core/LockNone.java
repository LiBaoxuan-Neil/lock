package com.github.houbb.lock.core.core;

import com.github.houbb.heaven.annotation.ThreadSafe;

/**
 * 无任何锁的操作
 *
 * @author binbin.hou
 * @since 0.0.3
 */
@ThreadSafe
public class LockNone extends AbstractLock {

    @Override
    public boolean tryLock(String key) {
        return true;
    }

    @Override
    public void unlock(String key) {

    }
}
