package com.github.houbb.lock.api.core;

/**
 * 读写锁定义接口
 * @author binbin.hou
 * @since 0.0.2
 */
public interface IReadWriteLock {

    /**
     * 获取读锁
     * @since 0.0.2
     */
    void lockRead();

    /**
     * 释放读锁
     */
    void unlockRead();

    /**
     * 获取写锁
     * @since 0.0.2
     */
    void lockWrite();

    /**
     * 释放写锁
     */
    void unlockWrite();

}
