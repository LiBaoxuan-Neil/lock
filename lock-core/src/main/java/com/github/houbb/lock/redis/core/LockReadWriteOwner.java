package com.github.houbb.lock.redis.core;

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 读写锁实现-保证释放锁时为锁的持有者
 *
 * @author binbin.hou
 * @since 0.0.2
 */
public class LockReadWriteOwner {

    private static final Log log = LogFactory.getLog(LockReadWriteOwner.class);

    /**
     * 如果使用类似 write 的方式，会导致读锁只能有一个。
     * 调整为使用 HashMap 存放读的信息
     *
     * @since 0.0.2
     */
    private final Map<Thread, Integer> readCountMap = new HashMap<>();

    /**
     * volatile 引用，保证线程间的可见性+易变性
     *
     * @since 0.0.2
     */
    private final AtomicReference<Thread> writeOwner = new AtomicReference<>();

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
            log.debug("获取读锁失败，进入等待状态。");
            wait();
        }
    }

    /**
     * 尝试获取读锁
     *
     * 读锁之间是不互斥的，这里后续需要优化。
     *
     * @return 是否成功
     * @since 0.0.2
     */
    private boolean tryLockRead() {
        if (writeCount > 0) {
            log.debug("当前有写锁，获取读锁失败");
            return false;
        }

        Thread currentThread = Thread.currentThread();
        // 次数暂时固定为1，后面如果实现可重入，这里可以改进。
        this.readCountMap.put(currentThread, 1);
        return true;
    }

    /**
     * 释放读锁
     *
     * @since 0.0.2
     */
    public synchronized void unlockRead() {
        Thread currentThread = Thread.currentThread();
        Integer readCount = readCountMap.get(currentThread);

        if (readCount == null) {
            throw new RuntimeException("当前线程未持有任何读锁，释放锁失败！");
        } else {
            log.debug("释放读锁，唤醒所有等待线程。");
            notifyAll();
        }
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
        if (!readCountMap.isEmpty()) {
            log.debug("当前有其他读锁，获取写锁失败。");
            return false;
        }

        Thread currentThread = Thread.currentThread();
        boolean result = writeOwner.compareAndSet(null, currentThread);
        log.debug("尝试获取写锁结果：{}", result);
        return result;
    }

    /**
     * 释放写锁
     *
     * @since 0.0.2
     */
    public synchronized void unlockWrite() {
        boolean toNullResult = writeOwner.compareAndSet(Thread.currentThread(), null);

        if (toNullResult) {
            writeCount--;
            log.debug("写锁释放，唤醒所有等待线程。");
            notifyAll();
        } else {
            throw new RuntimeException("释放写锁失败");
        }
    }

}
