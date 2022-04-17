package com.github.houbb.lock.test.core;

import com.github.houbb.heaven.util.util.DateUtil;
import com.github.houbb.lock.api.core.ILock;
import com.github.houbb.lock.core.core.LockWaitNotify;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author binbin.hou
 * @since 1.0.0
 */
public class LockWaitNotifyThread implements Runnable {

    private static final Log log = LogFactory.getLog(LockWaitNotifyThread.class);

    private final ILock lock = new LockWaitNotify();

    @Override
    public void run() {
        log.debug("first lock");

        lock.lock();
        log.info("执行业务逻辑。");
        DateUtil.sleep(TimeUnit.SECONDS, 5);
        lock.unlock();
        log.debug("first unlock");
    }

    public static void main(String[] args) {
        final Runnable runnable = new LockWaitNotifyThread();
        new Thread(runnable).start();
        new Thread(runnable).start();
        new Thread(runnable).start();
    }

}
