package com.github.houbb.lock.api.core;

/**
 * 释放锁失败处理类
 *
 * @author binbin.hou
 * @since 1.2.0
 */
public interface ILockReleaseFailHandler {

    /**
     * 处理
     * @param context 上下文
     * @since 1.2.0
     */
    void handle(ILockReleaseFailHandlerContext context);

}
