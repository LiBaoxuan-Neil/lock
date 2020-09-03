package com.github.houbb.lock.redis.support.redis;

/**
 * Redis 客户端
 * @author binbin.hou
 * @since 1.0.0
 */
public interface IRedisOperator {

    /**
     * 尝试获取分布式锁
     *
     * @param lockKey    锁
     * @param requestId  请求标识
     * @param expireTimeMills 超期时间
     * @return 是否获取成功
     */
    boolean tryLock(String lockKey, String requestId, int expireTimeMills);

    /**
     * 解锁
     * @param lockKey 锁 key
     * @param requestId 请求标识
     * @return 结果
     * @since 0.0.1
     */
    boolean unlock(String lockKey, String requestId);

}
