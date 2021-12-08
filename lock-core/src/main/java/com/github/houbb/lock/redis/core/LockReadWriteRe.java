package com.github.houbb.lock.redis.core;

import com.github.houbb.lock.api.core.IReadWriteLock;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 读写锁实现-可重入锁
 *
 * @author binbin.hou
 * @since 0.0.2
 */
public class LockReadWriteRe implements IReadWriteLock {

    private static final Log log = LogFactory.getLog(LockReadWriteRe.class);

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
    private volatile int writeCount = 0;

    /**
     * 获取读锁,读锁在写锁不存在的时候才能获取
     *
     * @since 0.0.2
     */
    @Override
    public synchronized void lockRead() {
        try {
            // 写锁存在,需要wait
            while (!tryLockRead()) {
                log.debug("获取读锁失败，进入等待状态。");
                wait();
            }
        } catch (InterruptedException e) {
            Thread.interrupted();
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
        Integer count = readCountMap.get(currentThread);
        if(count == null) {
            count = 0;
        }
        count++;

        this.readCountMap.put(currentThread, count);
        return true;
    }

    /**
     * 释放读锁
     *
     * @since 0.0.2
     */
    @Override
    public synchronized void unlockRead() {
        Thread currentThread = Thread.currentThread();
        Integer readCount = readCountMap.get(currentThread);

        if (readCount == null) {
            throw new RuntimeException("当前线程未持有任何读锁，释放锁失败！");
        } else {
            readCount--;

            // 已经是最后一次
            if(readCount == 0) {
                readCountMap.remove(currentThread);
            } else {
                readCountMap.put(currentThread, readCount);
            }

            log.debug("释放读锁，唤醒所有等待线程。");
            notifyAll();
        }
    }

    /**
     * 获取写锁
     *
     * @since 0.0.2
     */
    @Override
    public synchronized void lockWrite() {
        try {
            // 写锁存在,需要wait
            while (!tryLockWrite()) {
                log.debug("获取写锁失败，进入等待状态。");
                wait();
            }

            // 此时已经不存在获取写锁的线程了,因此占坑,防止写锁饥饿
            writeCount++;
        } catch (InterruptedException e) {
            Thread.interrupted();
        }
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
        // 多次重入
        if(writeOwner.get() == currentThread) {
            log.debug("为当前写线程多次重入，直接返回 true。");
            return true;
        }

        boolean result = writeOwner.compareAndSet(null, currentThread);
        log.debug("尝试获取写锁结果：{}", result);
        return result;
    }

    /**
     * 释放写锁
     *
     * @since 0.0.2
     */
    @Override
    public synchronized void unlockWrite() {
        Thread currentThread = Thread.currentThread();
        // 多次重入释放（当次数多于1时直接返回，否则需要释放 owner 信息）
        if(writeCount > 1 && (currentThread == writeOwner.get())) {
            log.debug("当前为写锁释放多次重入，直接返回成功。");

            unlockWriteNotify();
            return;
        }

        boolean toNullResult = writeOwner.compareAndSet(currentThread, null);
        if (toNullResult) {
            unlockWriteNotify();
        } else {
            throw new RuntimeException("释放写锁失败");
        }
    }

    /**
     * 释放写锁并且通知
     */
    private synchronized void unlockWriteNotify() {
        writeCount--;
        log.debug("释放写锁成功，唤醒所有等待线程。");
        notifyAll();
    }

}
