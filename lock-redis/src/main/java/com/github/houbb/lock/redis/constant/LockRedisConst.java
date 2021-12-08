package com.github.houbb.lock.redis.constant;

/**
 * redis 锁常量
 *
 * @author binbin.hou
 * @since 0.0.1
 */
public final class LockRedisConst {

    private LockRedisConst() {
    }

    /**
     * 加锁成功
     * @since 0.0.1
     */
    public static final String LOCK_SUCCESS = "OK";

    /**
     * 如果不存在则设置值
     * @since 0.0.1
     */
    public static final String SET_IF_NOT_EXIST = "NX";

    /**
     * 设置过期时间
     *
     * 单位：milliseconds
     * @since 0.0.1
     */
    public static final String SET_WITH_EXPIRE_TIME = "PX";

    /**
     * 解锁成功
     *
     * @since 0.0.1
     */
    public static final Long RELEASE_SUCCESS = 1L;


    /**
     * 默认的失效时间
     *
     * 暂时定为 30min
     * @since 0.0.1
     */
    public static final int DEFAULT_EXPIRE_MILLS = 1000 * 60 * 30;

    /**
     * 默认锁为全局锁
     * @since 0.0.1
     */
    public static final String DEFAULT_KEY = "GLOBAL";

}
