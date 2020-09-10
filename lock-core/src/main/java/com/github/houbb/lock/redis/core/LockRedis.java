package com.github.houbb.lock.redis.core;

import com.github.houbb.heaven.util.lang.StringUtil;
import com.github.houbb.id.api.Id;
import com.github.houbb.id.core.util.IdThreadLocalHelper;
import com.github.houbb.lock.redis.constant.LockRedisConst;
import com.github.houbb.lock.redis.exception.LockRedisException;
import com.github.houbb.lock.redis.support.operator.IOperator;
import com.github.houbb.wait.api.IWait;

/**
 * 这里是基于 redis 实现
 *
 * 实际上也可以基于 zk/数据库等实现。
 *
 * @author binbin.hou
 * @since 0.0.1
 */
public class LockRedis extends AbstractLockRedis {

    /**
     * redis 操作实现
     * @since 0.0.1
     */
    private final IOperator redisOperator;

    /**
     * 主键标识
     * @since 0.0.1
     */
    private final Id id;

    public LockRedis(IWait wait, IOperator redisOperator, Id id) {
        super(wait);
        this.redisOperator = redisOperator;
        this.id = id;
    }

    @Override
    public boolean tryLock(String key) {
        final String requestId = id.id();
        IdThreadLocalHelper.put(requestId);

        return redisOperator.lock(key, requestId, LockRedisConst.DEFAULT_EXPIRE_MILLS);
    }

    @Override
    public void unlock(String key) {
        final String requestId = IdThreadLocalHelper.get();
        if(StringUtil.isEmpty(requestId)) {
            String threadName = Thread.currentThread().getName();
            throw new LockRedisException("Thread " + threadName +" not contains requestId");
        }

        boolean unlock = redisOperator.unlock(key, requestId);
        if(!unlock) {
            throw new LockRedisException("Unlock key " + key + " result is failed!");
        }
    }
}