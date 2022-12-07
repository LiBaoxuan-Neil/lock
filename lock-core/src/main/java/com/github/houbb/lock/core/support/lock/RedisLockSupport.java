package com.github.houbb.lock.core.support.lock;

import com.github.houbb.common.cache.api.service.ICommonCacheService;
import com.github.houbb.id.api.Id;
import com.github.houbb.id.core.util.IdThreadLocalHelper;
import com.github.houbb.lock.api.core.ILockSupport;
import com.github.houbb.lock.api.core.ILockSupportContext;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.redis.config.core.constant.JedisConst;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁接口定义
 * @author binbin.hou
 * @since 0.0.1
 */
public class RedisLockSupport implements ILockSupport {

    private final Log log = LogFactory.getLog(RedisLockSupport.class);

    @Override
    public boolean tryLock(long time, TimeUnit unit, String key, ILockSupportContext context) {
        long startTimeMills = System.currentTimeMillis();

        // 一次获取，直接成功
        boolean result = this.tryLock(key, context);
        if(result) {
            return true;
        }

        // 时间判断
        if(time <= 0) {
            return false;
        }
        long durationMills = unit.toMillis(time);
        long endMills = startTimeMills + durationMills;

        // 循环等待
        while (System.currentTimeMillis() < endMills) {
            result = tryLock(key, context);
            if(result) {
                return true;
            }

            // 等待 1ms
            try {
                TimeUnit.MILLISECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    @Override
    public boolean tryLock(String key, ILockSupportContext context) {
        log.info("开始尝试获取锁 {}", key);

        // 生成当前线程的唯一标识
        Id id = context.id();
        final String requestId = id.id();
        IdThreadLocalHelper.put(requestId);
        log.info("开始尝试获取锁 requestId: {}", requestId);

        final ICommonCacheService commonCacheService = context.cache();

        final int lockExpireMills = context.lockExpireMills();

        String result = commonCacheService.set(key, requestId, JedisConst.SET_IF_NOT_EXIST, JedisConst.SET_WITH_EXPIRE_TIME, lockExpireMills);
        return JedisConst.OK.equalsIgnoreCase(result);
    }

    @Override
    public boolean unlock(String key, ILockSupportContext context) {
        log.info("开始尝试释放锁 {}", key);
        String requestId = IdThreadLocalHelper.get();
        log.info("开始尝试释放锁 requestId: {}", requestId);

        final ICommonCacheService commonCacheService = context.cache();
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = commonCacheService.eval(script, Collections.singletonList(key), Collections.singletonList(requestId));
        return JedisConst.RELEASE_SUCCESS.equals(result);
    }

}
