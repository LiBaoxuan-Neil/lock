package com.github.houbb.lock.core.bs;

import com.github.houbb.common.cache.api.service.ICommonCacheService;
import com.github.houbb.heaven.util.common.ArgUtil;
import com.github.houbb.id.api.Id;
import com.github.houbb.id.core.core.Ids;
import com.github.houbb.lock.api.core.*;
import com.github.houbb.lock.core.support.format.LockKeyFormat;
import com.github.houbb.lock.core.support.handler.LockReleaseFailHandler;
import com.github.houbb.lock.core.support.lock.LockSupportContext;
import com.github.houbb.lock.core.support.lock.RedisLockSupport;
import com.github.houbb.redis.config.core.factory.JedisRedisServiceFactory;

import java.util.concurrent.TimeUnit;

/**
 * 锁引导类
 *
 * @author binbin.hou
 * @since 0.0.4
 */
public final class LockBs implements ILock {

    private LockBs() {
    }

    public static LockBs newInstance() {
        return new LockBs();
    }

    /**
     * 标识策略
     *
     * @since 0.0.4
     */
    private Id id = Ids.uuid32();

    /**
     * 缓存策略
     *
     * @since 0.0.4
     */
    private ICommonCacheService cache = JedisRedisServiceFactory.pooled("127.0.0.1", 6379);

    /**
     * 锁支持策略
     *
     * @since 1.0.0
     */
    private ILockSupport lockSupport = new RedisLockSupport();

    /**
     * 锁 key 格式化
     *
     * @since 1.2.0
     */
    private ILockKeyFormat lockKeyFormat = new LockKeyFormat();

    /**
     * 锁释放失败处理类
     *
     * @since 1.2.0
     */
    private ILockReleaseFailHandler lockReleaseFailHandler = new LockReleaseFailHandler();

    public LockBs id(Id id) {
        ArgUtil.notNull(id, "id");

        this.id = id;
        return this;
    }

    public LockBs cache(ICommonCacheService cache) {
        ArgUtil.notNull(cache, "cache");

        this.cache = cache;
        return this;
    }

    public LockBs lockSupport(ILockSupport lockSupport) {
        ArgUtil.notNull(lockSupport, "lockSupport");

        this.lockSupport = lockSupport;
        return this;
    }

    public LockBs lockKeyFormat(ILockKeyFormat lockKeyFormat) {
        ArgUtil.notNull(lockKeyFormat, "lockKeyFormat");

        this.lockKeyFormat = lockKeyFormat;
        return this;
    }

    public LockBs lockReleaseFailHandler(ILockReleaseFailHandler lockReleaseFailHandler) {
        ArgUtil.notNull(lockReleaseFailHandler, "lockReleaseFailHandler");

        this.lockReleaseFailHandler = lockReleaseFailHandler;
        return this;
    }

    public ILockReleaseFailHandler lockReleaseFailHandler() {
        return lockReleaseFailHandler;
    }

    @Override
    public boolean tryLock(String key, TimeUnit timeUnit, long lockTime, long waitLockTime) {
        ILockSupportContext supportContext = buildLockSupportContext(key, timeUnit, lockTime, waitLockTime);
        return this.lockSupport.tryLock(supportContext);
    }

    @Override
    public boolean tryLock(String key, TimeUnit timeUnit, long lockTime) {
        return this.tryLock(key, timeUnit, lockTime, 0);
    }

    @Override
    public boolean tryLock(String key, long lockTime) {
        return this.tryLock(key, TimeUnit.SECONDS, lockTime);
    }

    @Override
    public boolean tryLock(String key) {
        return this.tryLock(key, 60);
    }

    @Override
    public boolean unlock(String key) {
        ILockSupportContext supportContext = buildLockSupportContext(key, TimeUnit.SECONDS, 0, 0);
        return this.lockSupport.unlock(supportContext);
    }

    /**
     * 构建上下文
     *
     * @param key          key
     * @param timeUnit     时间
     * @param lockTime     加锁时间
     * @param waitLockTime 等待加锁时间
     * @return 结果
     * @since 1.0.0
     */
    private ILockSupportContext buildLockSupportContext(String key, TimeUnit timeUnit, long lockTime, long waitLockTime) {
        ArgUtil.notEmpty(key, "key");
        ArgUtil.notNull(timeUnit, "timeUnit");

        ILockSupportContext context = LockSupportContext.newInstance().id(id).cache(cache).lockKeyFormat(lockKeyFormat).lockReleaseFailHandler(lockReleaseFailHandler).key(key).timeUnit(timeUnit).lockTime(lockTime).waitLockTime(waitLockTime);
        return context;
    }

}
