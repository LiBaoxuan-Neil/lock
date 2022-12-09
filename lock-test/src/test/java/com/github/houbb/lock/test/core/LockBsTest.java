package com.github.houbb.lock.test.core;


import com.github.houbb.id.core.core.Ids;
import com.github.houbb.lock.api.core.ILock;
import com.github.houbb.lock.core.bs.LockBs;
import com.github.houbb.lock.core.constant.LockConst;
import com.github.houbb.lock.core.support.format.LockKeyFormat;
import com.github.houbb.lock.core.support.handler.LockReleaseFailHandler;
import com.github.houbb.lock.core.support.lock.RedisLockSupport;
import com.github.houbb.redis.config.core.factory.JedisRedisServiceFactory;
import org.junit.Test;

public class LockBsTest {

    @Test
    public void helloTest() {
        ILock lock = LockBs.newInstance();
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

    @Test
    public void configTest() {
        LockBs.newInstance()
                .id(Ids.uuid32())   //id 生成策略
                .cache(JedisRedisServiceFactory.pooled("127.0.0.1", 6379)) //缓存策略
                .lockSupport(new RedisLockSupport())    // 锁实现策略
                .lockKeyFormat(new LockKeyFormat())     // 针对 key 的格式化处理策略
                .lockKeyNamespace(LockConst.DEFAULT_LOCK_KEY_NAMESPACE) // 加锁 key 的命名空间，避免不同应用冲突
                .lockReleaseFailHandler(new LockReleaseFailHandler())   //释放锁失败处理
                ;
    }

}
