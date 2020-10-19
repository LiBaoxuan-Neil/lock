package com.github.houbb.lock.redis.exception;

/**
 * @author binbin.hou
 * @since 0.0.2
 */
public class LockRuntimeException extends RuntimeException {

    public LockRuntimeException() {
    }

    public LockRuntimeException(String message) {
        super(message);
    }

    public LockRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public LockRuntimeException(Throwable cause) {
        super(cause);
    }

    public LockRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
