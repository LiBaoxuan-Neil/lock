package com.github.houbb.lock.redis.exception;

/**
 * @author binbin.hou
 * @since 0.0.1
 */
public class LockRedisException extends RuntimeException {

    public LockRedisException() {
    }

    public LockRedisException(String message) {
        super(message);
    }

    public LockRedisException(String message, Throwable cause) {
        super(message, cause);
    }

    public LockRedisException(Throwable cause) {
        super(cause);
    }

    public LockRedisException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
