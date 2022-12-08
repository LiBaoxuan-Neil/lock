package com.github.houbb.lock.core.support.handler;

import com.github.houbb.lock.api.core.ILockReleaseFailHandler;
import com.github.houbb.lock.api.core.ILockReleaseFailHandlerContext;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

/**
 * 释放锁失败处理类
 *
 * @author binbin.hou
 * @since 1.2.0
 */
public class LockReleaseFailHandler implements ILockReleaseFailHandler {

    private static final Log LOG = LogFactory.getLog(LockReleaseFailHandler.class);

    @Override
    public void handle(ILockReleaseFailHandlerContext context) {
        LOG.error("[LOCK] release lock failed {}", context.key());
    }

}
