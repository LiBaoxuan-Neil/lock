package com.github.houbb.lock.api.core;

/**
 * 释放锁失败处理类
 *
 * @author binbin.hou
 * @since 1.2.0
 */
public interface ILockReleaseFailHandlerContext {

    /**
     * 释放锁对应的 key
     * @return 结果
     * @since 1.2.0
     */
    String key();

}
