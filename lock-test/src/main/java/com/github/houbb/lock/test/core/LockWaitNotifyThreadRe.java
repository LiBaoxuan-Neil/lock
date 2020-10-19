package com.github.houbb.lock.test.core;

import com.github.houbb.heaven.util.util.DateUtil;
import com.github.houbb.lock.api.core.ILock;
import com.github.houbb.lock.redis.core.LockWaitNotify;
import com.github.houbb.lock.redis.core.LockWaitNotifyRe;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author binbin.hou
 * @since 1.0.0
 */
public class LockWaitNotifyThreadRe implements Runnable {

    private static final Log log = LogFactory.getLog(LockWaitNotifyThreadRe.class);

    private final ILock lock = new LockWaitNotifyRe();

    @Override
    public void run() {
        log.debug("first lock");
        lock.lock();

        log.debug("second lock");
        lock.lock();
        log.debug("second unlock");
        lock.unlock();

        log.debug("first unlock");
        lock.unlock();
    }

    public static void main(String[] args) {
        final Runnable runnable = new LockWaitNotifyThreadRe();
        new Thread(runnable).start();
        new Thread(runnable).start();
        new Thread(runnable).start();
    }

}
