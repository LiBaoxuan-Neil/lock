package com.github.houbb.lock.api.core;

/**
 * 分布式锁 KEY 格式化处理上下文
 *
 * @author binbin.hou
 * @since 0.0.1
 */
public interface ILockKeyFormatContext {

    /**
     * 原始的 key
     *
     * @return 返回
     * @since 1.2.0
     */
    String rawKey();

    /**
     * 锁 key 的默认命名空间
     * @return 锁 key 的默认命名空间
     * @since 1.4.0
     */
    String lockKeyNamespace();

}
