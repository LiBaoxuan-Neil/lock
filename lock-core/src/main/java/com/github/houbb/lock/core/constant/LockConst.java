package com.github.houbb.lock.core.constant;

/**
 * 通用锁常量
 *
 * @author binbin.hou
 * @since 0.0.3
 */
public final class LockConst {

    private LockConst() {
    }

    /**
     * 默认的失效时间
     *
     * 暂时定为 1min
     * @since 0.0.1
     */
    public static final int DEFAULT_EXPIRE_MILLS = 1000 * 60;

    /**
     * 默认锁为全局锁
     * @since 0.0.1
     */
    public static final String DEFAULT_KEY = "GLOBAL";

    /**
     * 加锁 key 对应的命名空间
     * @since 1.4.0
     */
    public static final String DEFAULT_LOCK_KEY_NAMESPACE = "DISTRIBUTED_LOCK";

}
