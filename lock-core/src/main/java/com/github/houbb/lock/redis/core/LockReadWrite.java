package com.github.houbb.lock.redis.core;

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

/**
 * 读写锁实现
 *
 * @author binbin.hou
 * @since 0.0.2
 */
public class LockReadWrite {

    private static final Log log = LogFactory.getLog(LockReadWrite.class);

    /**
     * 读次数统计
     */
    private int readCount = 0;

    /**
     * 写次数统计
     */
    private int writeCount = 0;

    /**
     * 获取读锁,读锁在写锁不存在的时候才能获取
     *
     * @since 0.0.2
     */
    public synchronized void lockRead() throws InterruptedException {
        // 写锁存在,需要wait
        while (!tryLockRead()) {
            wait();
        }

        readCount++;
    }

    /**
     * 尝试获取读锁
     *
     * @return 是否成功
     * @since 0.0.2
     */
    private boolean tryLockRead() {
        if (writeCount > 0) {
            log.debug("当前有写锁，获取读锁失败");
            return false;
        }

        return true;
    }

    /**
     * 释放读锁
     *
     * @since 0.0.2
     */
    public synchronized void unlockRead() {
        readCount--;
        notifyAll();
    }

    /**
     * 获取写锁
     *
     * @since 0.0.2
     */
    public synchronized void lockWrite() throws InterruptedException {
        // 写锁存在,需要wait
        while (!tryLockWrite()) {
            wait();
        }

        // 此时已经不存在获取写锁的线程了,因此占坑,防止写锁饥饿
        writeCount++;
    }

    /**
     * 尝试获取写锁
     *
     * @return 是否成功
     * @since 0.0.2
     */
    private boolean tryLockWrite() {
        if (writeCount > 0) {
            log.debug("当前有其他写锁，获取写锁失败");
            return false;
        }

        // 读锁
        if (readCount > 0) {
            log.debug("当前有其他读锁，获取写锁失败。");
            return false;
        }

        return true;
    }

    /**
     * 释放写锁
     *
     * @since 0.0.2
     */
    public synchronized void unlockWrite() {
        writeCount--;
        notifyAll();
    }

}