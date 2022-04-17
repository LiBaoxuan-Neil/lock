package com.github.houbb.lock.test.core;

import com.github.houbb.lock.api.core.ILock;
import com.github.houbb.lock.core.core.LockWaitNotifyRe;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

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
