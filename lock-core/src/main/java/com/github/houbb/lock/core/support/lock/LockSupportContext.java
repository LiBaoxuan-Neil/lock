package com.github.houbb.lock.core.support.lock;

import com.github.houbb.common.cache.api.service.ICommonCacheService;
import com.github.houbb.id.api.Id;
import com.github.houbb.lock.api.core.ILockSupportContext;

/**
 * 分布式锁接口定义
 * @author binbin.hou
 * @since 0.0.1
 */
public class LockSupportContext implements ILockSupportContext {

    public static LockSupportContext newInstance() {
        return new LockSupportContext();
    }

    /**
     * 标识策略
     * @since 0.0.4
     */
    private Id id;

    /**
     * 缓存策略
     * @since 0.0.4
     */
    private ICommonCacheService commonCacheService;

    /**
     * 锁的过期时间
     * @since 1.0.0
     */
    private int lockExpireMills;

    @Override
    public Id id() {
        return id;
    }

    public LockSupportContext id(Id id) {
        this.id = id;
        return this;
    }

    @Override
    public ICommonCacheService commonCacheService() {
        return commonCacheService;
    }

    public LockSupportContext commonCacheService(ICommonCacheService commonCacheService) {
        this.commonCacheService = commonCacheService;
        return this;
    }

    @Override
    public int lockExpireMills() {
        return lockExpireMills;
    }

    public LockSupportContext lockExpireMills(int lockExpireMills) {
        this.lockExpireMills = lockExpireMills;
        return this;
    }
}
