package com.github.houbb.lock.test.core;

import com.github.houbb.lock.api.core.ILock;
import com.github.houbb.lock.core.core.LockSpinRe;

/**
 * @author binbin.hou
 * @since 1.0.0
 */
public class LockSpinReThread implements Runnable {

    private final ILock lock = new LockSpinRe();

    @Override
    public void run() {
        System.out.println("first-lock: " + Thread.currentThread().getId());
        lock.lock();

        System.out.println("second-lock: " + Thread.currentThread().getId());
        lock.lock();
        lock.unlock();
        System.out.println("second-unlock: " + Thread.currentThread().getId());

        lock.unlock();
        System.out.println("first-unlock: " + Thread.currentThread().getId());
    }

    public static void main(String[] args) {
        final Runnable runnable = new LockSpinReThread();
        new Thread(runnable).start();
        new Thread(runnable).start();
        new Thread(runnable).start();
    }

}
