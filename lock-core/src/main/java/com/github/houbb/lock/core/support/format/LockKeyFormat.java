package com.github.houbb.lock.core.support.format;

import com.github.houbb.lock.api.core.ILockKeyFormat;
import com.github.houbb.lock.api.core.ILockKeyFormatContext;

/**
 * 简单的格式化处理
 * @since 1.2.0
 * @author dh
 */
public class LockKeyFormat implements ILockKeyFormat {

    @Override
    public String format(ILockKeyFormatContext formatContext) {
        String rawKey = formatContext.rawKey();

        String format = "DISTRIBUTED-LOCK:%s";
        return String.format(format, rawKey);
    }

}
