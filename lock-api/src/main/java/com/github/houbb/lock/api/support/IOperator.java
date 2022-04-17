package com.github.houbb.lock.api.support;

/**
 * 操作接口定义
 *
 * ps: 可以基于集中式数据库做操作
 *
 * @author binbin.hou
 * @since 0.0.3
 */
public interface IOperator {

    /**
     * 尝试获取分布式锁
     *
     * @param lockKey    锁
     * @param requestId  请求标识
     * @param expireTimeMills 超期时间
     * @return 是否获取成功
     * @since 0.0.3
     */
    boolean lock(String lockKey, String requestId, int expireTimeMills);

    /**
     * 解锁
     * @param lockKey 锁 key
     * @param requestId 请求标识
     * @return 结果
     * @since 0.0.3
     */
    boolean unlock(String lockKey, String requestId);

    /**
     * 清空过期的锁
     *
     * 避免单个线程 unlock 失败，定时移除过期的锁。
     * @since 0.0.4
     */
    void clearExpireLock();

}
