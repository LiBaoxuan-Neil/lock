package com.github.houbb.lock.test;

/**
 * @author binbin.hou
 * @since 0.0.1
 */
public class SyncTest {

    public synchronized void syncOne() {
        System.out.println("方法1执行");
        syncTwo();
    }

    public synchronized void syncTwo() {
        System.out.println("方法2执行");
    }

}
