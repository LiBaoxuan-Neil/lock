package com.github.houbb.lock.redis.support.operator.impl;

import com.github.houbb.lock.redis.constant.LockRedisConst;
import com.github.houbb.lock.redis.support.operator.IOperator;
import redis.clients.jedis.Jedis;

import java.util.Collections;

/**
 * Redis 客户端
 * @author binbin.hou
 * @since 0.0.1
 */
public class JedisOperator implements IOperator {

    /**
     * jedis 客户端
     * @since 0.0.1
     */
    private final Jedis jedis;

    public JedisOperator(Jedis jedis) {
        this.jedis = jedis;
    }

    /**
     * 尝试获取分布式锁
     *
     * expireTimeMills 保证当前进程挂掉，也能释放锁
     *
     * requestId 保证解锁的是当前进程（锁的持有者）
     *
     * @param lockKey         锁
     * @param requestId       请求标识
     * @param expireTimeMills 超期时间
     * @return 是否获取成功
     * @since 0.0.1
     */
    @Override
    public boolean lock(String lockKey, String requestId, int expireTimeMills) {
        String result = jedis.set(lockKey, requestId, LockRedisConst.SET_IF_NOT_EXIST, LockRedisConst.SET_WITH_EXPIRE_TIME, expireTimeMills);
        return LockRedisConst.LOCK_SUCCESS.equals(result);
    }

    /**
     * 解锁
     *
     * （1）使用 requestId，保证为当前锁的持有者
     * （2）使用 lua 脚本，保证执行的原子性。
     *
     * @param lockKey   锁 key
     * @param requestId 请求标识
     * @return 结果
     * @since 0.0.1
     */
    @Override
    public boolean unlock(String lockKey, String requestId) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
        return LockRedisConst.RELEASE_SUCCESS.equals(result);
    }

}
