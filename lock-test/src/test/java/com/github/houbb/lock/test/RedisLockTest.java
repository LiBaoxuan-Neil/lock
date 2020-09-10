package com.github.houbb.lock.test;

import com.github.houbb.lock.api.core.ILock;
import com.github.houbb.lock.redis.bs.LockRedisBs;
import com.github.houbb.lock.redis.support.operator.IOperator;
import com.github.houbb.lock.redis.support.operator.impl.JedisOperator;
import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * @author binbin.hou
 * @since 0.0.1
 */
public class RedisLockTest {

    @Test
    public void helloTest() {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        IOperator operator = new JedisOperator(jedis);

        // 获取锁
        ILock lock = LockRedisBs.newInstance().operator(operator).lock();

        try {
            boolean lockResult = lock.tryLock();
            System.out.println(lockResult);
            // 业务处理
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

}
