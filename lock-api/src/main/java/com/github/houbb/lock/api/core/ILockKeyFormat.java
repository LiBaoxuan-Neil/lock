package com.github.houbb.lock.api.core;

/**
 * 分布式锁 KEY 格式化处理
 *
 * @author binbin.hou
 * @since 0.0.1
 */
public interface ILockKeyFormat {

    /**
     * 对 key 进行格式化处理，避免缓存命名空间处理等问题
     *
     * @param formatContext 上下文
     * @return 返回
     * @since 1.2.0
     */
    String format(ILockKeyFormatContext formatContext);

}
