package com.github.houbb.lock.core.support.simple;

import com.github.houbb.heaven.util.lang.StringUtil;
import com.github.houbb.id.core.util.IdThreadLocalHelper;
import com.github.houbb.lock.core.constant.LockConst;
import com.github.houbb.lock.core.core.AbstractLock;
import com.github.houbb.lock.core.exception.LockRuntimeException;

/**
 * 简单锁实现策略
 *
 * @author binbin.hou
 * @since 0.0.4
 */
public class SimpleLock extends AbstractLock {

    public static SimpleLock newInstance() {
        return new SimpleLock();
    }

    @Override
    public boolean tryLock(String key) {
        final String requestId = id.id();
        IdThreadLocalHelper.put(requestId);

        return operator.lock(key, requestId, LockConst.DEFAULT_EXPIRE_MILLS);
    }

    @Override
    public void unlock(String key) {
        final String requestId = IdThreadLocalHelper.get();
        if(StringUtil.isEmpty(requestId)) {
            String threadName = Thread.currentThread().getName();
            throw new LockRuntimeException("Thread " + threadName +" not contains requestId");
        }

        boolean unlock = operator.unlock(key, requestId);
        if(!unlock) {
            throw new LockRuntimeException("Unlock key " + key + " result is failed!");
        }
    }

}
