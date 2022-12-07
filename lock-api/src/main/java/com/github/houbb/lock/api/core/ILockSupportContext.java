package com.github.houbb.lock.api.core;

import com.github.houbb.common.cache.api.service.ICommonCacheService;
import com.github.houbb.id.api.Id;

/**
 * 分布式锁接口定义
 * @author binbin.hou
 * @since 0.0.1
 */
public interface ILockSupportContext {

    /**
     * @return 标识策略
     * @since 0.0.4
     */
    Id id();

    /**
     * @return 缓存策略
     * @since 0.0.4
     */
    ICommonCacheService cache();

    /**
     * 锁的过期时间
     * @return 结果
     */
    int lockExpireMills();

}
