package com.github.houbb.lock.test.core;


import com.github.houbb.lock.api.core.ILock;
import com.github.houbb.lock.core.bs.LockBs;
import org.junit.Test;

public class LockBsTest {

    @Test
    public void helloTest() {
        ILock lock = LockBs.newInstance()
                .init();

        String key = "ddd";
        try {
            // 加锁
            lock.tryLock(key);
            System.out.println("业务处理");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // 释放锁
            lock.unlock(key);
        }
    }

}
