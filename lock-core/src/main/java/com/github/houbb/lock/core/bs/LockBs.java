package com.github.houbb.lock.core.bs;

import com.github.houbb.common.cache.api.service.ICommonCacheService;
import com.github.houbb.heaven.util.common.ArgUtil;
import com.github.houbb.id.api.Id;
import com.github.houbb.id.core.core.Ids;
import com.github.houbb.lock.api.core.ILock;
import com.github.houbb.lock.api.core.ILockSupport;
import com.github.houbb.lock.api.core.ILockSupportContext;
import com.github.houbb.lock.core.constant.LockConst;
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
public final class LockBs implements ILock{

    private LockBs(){}

    public static LockBs newInstance() {
        return new LockBs();
    }

    /**
     * 加锁锁定时间
     * @since 0.0.4
     */
    private int lockExpireMills = LockConst.DEFAULT_EXPIRE_MILLS;

    /**
     * 标识策略
     * @since 0.0.4
     */
    private Id id = Ids.uuid32();

    /**
     * 缓存策略
     * @since 0.0.4
     */
    private ICommonCacheService commonCacheService = JedisRedisServiceFactory.simple("127.0.0.1", 6379);

    /**
     * 锁支持策略
     * @since 1.0.0
     */
    private ILockSupport lockSupport = new RedisLockSupport();

    /**
     * 锁上下文
     * @since 1.0.0
     */
    private ILockSupportContext lockSupportContext = null;

    public LockBs lockExpireMills(int lockExpireMills) {
        this.lockExpireMills = lockExpireMills;
        return this;
    }

    public LockBs id(Id id) {
        ArgUtil.notNull(id, "id");

        this.id = id;
        return this;
    }

    public LockBs commonCacheService(ICommonCacheService commonCacheService) {
        ArgUtil.notNull(commonCacheService, "commonCacheService");

        this.commonCacheService = commonCacheService;
        return this;
    }

    public LockBs lockSupport(ILockSupport lockSupport) {
        ArgUtil.notNull(lockSupport, "lockSupport");

        this.lockSupport = lockSupport;
        return this;
    }


    /**
     * 初始化
     */
    public LockBs init() {
        this.lockSupportContext = LockSupportContext.newInstance()
                .id(id)
                .commonCacheService(commonCacheService)
                .lockExpireMills(lockExpireMills);

        return this;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit, String key) {
        ArgUtil.notEmpty(key, "key");
        this.checkInitStatus();


        return this.lockSupport.tryLock(time, unit, key, lockSupportContext);
    }

    @Override
    public boolean tryLock(String key) {
        ArgUtil.notEmpty(key, "key");
        this.checkInitStatus();

        return this.lockSupport.tryLock(key, lockSupportContext);
    }

    @Override
    public boolean unlock(String key) {
        ArgUtil.notEmpty(key, "key");
        this.checkInitStatus();

        return this.lockSupport.unlock(key, lockSupportContext);
    }


    private void checkInitStatus() {
        ArgUtil.notNull(lockSupportContext, "please init() first!");
    }


}
