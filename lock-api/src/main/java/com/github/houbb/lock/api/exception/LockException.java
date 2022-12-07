package com.github.houbb.lock.api.exception;

/**
 * 加锁运行时异常
 * @since 1.0.0
 * @author dh
 */
public class LockException extends RuntimeException {

    public LockException() {
    }

    public LockException(String message) {
        super(message);
    }

    public LockException(String message, Throwable cause) {
        super(message, cause);
    }

    public LockException(Throwable cause) {
        super(cause);
    }

    public LockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
