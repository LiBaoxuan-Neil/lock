# 项目简介

[lock](https://github.com/houbb/lock) 为 java 设计的分布式锁，开箱即用，纵享丝滑。

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.houbb/lock/badge.svg)](http://mvnrepository.com/artifact/com.github.houbb/lock)
[![Build Status](https://www.travis-ci.org/houbb/lock.svg?branch=master)](https://www.travis-ci.org/houbb/lock?branch=master)
[![Coverage Status](https://coveralls.io/repos/github/houbb/lock/badge.svg?branch=master)](https://coveralls.io/github/houbb/lock?branch=master)

开源地址：[https://github.com/houbb/lock](https://github.com/houbb/lock) 

## 目的

- 开箱即用，支持注解式和过程式调用 

- 基于 redis 的分布式锁

- 内置支持多种 redis 的整合方式

- 渐进式设计，可独立于 spring 使用

- 整合 spring

- 整合 spring-boot

# 变更日志

> [变更日志](CHANGELOG.md)

# 快速开始 

## 需要 

jdk1.7+

maven 3.x+

## maven 引入 

```xml
<dependency>
    <groupId>com.github.houbb</groupId>
    <artifactId>lock-core</artifactId>
    <version>1.4.0</version>
</dependency>
```

## 入门例子

基于本地 redis 的测试案例。

```java
public void helloTest() {
    ILock lock = LockBs.newInstance();
    String key = "ddd";
    try {
        // 加锁
        boolean lockFlag = lock.tryLock(key);
        System.out.println("业务处理");
    } catch (Exception e) {
        throw new RuntimeException(e);
    } finally {
        // 释放锁
        lock.unlock(key);
    }
}
```

### 配置化

为了便于拓展，LockBs 的配置支持自定义：

```java
LockBs.newInstance()
        .id(Ids.uuid32())   //id 生成策略
        .cache(JedisRedisServiceFactory.pooled("127.0.0.1", 6379)) //缓存策略
        .lockSupport(new RedisLockSupport())    // 锁实现策略
        .lockKeyFormat(new LockKeyFormat())     // 针对 key 的格式化处理策略
        .lockKeyNamespace(LockConst.DEFAULT_LOCK_KEY_NAMESPACE) // 加锁 key 的命名空间，避免不同应用冲突
        .lockReleaseFailHandler(new LockReleaseFailHandler())   //释放锁失败处理
        ;
```

# 整合 spring

## maven 引入

```xml
<dependency>
    <groupId>com.github.houbb</groupId>
    <artifactId>lock-spring</artifactId>
    <version>1.4.0</version>
</dependency>
```

## 指定 bean 使用

### 启用分布式锁

`@EnableLock` 启用分布式锁。

`@EnableRedisConfig` 启用 redis 的默认配置。

```xml
@Configurable
@ComponentScan(basePackages = "com.github.houbb.lock.test.service")
@EnableLock
@EnableRedisConfig
public class SpringConfig {
}
```

`EnableLock` 注解说明，和引导类对应：

```java
public @interface EnableLock {

    /**
     * 唯一标识生成策略
     * @return 结果
     */
    String id() default "lockId";

    /**
     * 缓存实现策略 bean 名称
     *
     * 默认引入 redis-config 中的配置
     *
     * @return 实现
     */
    String cache() default "springRedisService";

    /**
     * 加锁 key 格式化策略
     * @return 策略
     */
    String lockKeyFormat() default "lockKeyFormat";

    /**
     * 锁 key 的默认命名空间
     * @since 1.4.0
     * @return 命名空间
     */
    String lockKeyNamespace() default LockConst.DEFAULT_LOCK_KEY_NAMESPACE;

    /**
     * 锁释放失败处理类
     * @return 结果
     */
    String lockReleaseFailHandler() default "lockReleaseFailHandler";

}
```

其中 `springRedisService` 使用的是 [redis-config](https://github.com/houbb/redis-config) 中的实现。

对应注解 `@EnableRedisConfig`，redis 的配置信息如下：

| 配置 | 说明 | 默认值
|:---|:---|:----|
| redis.address | redis 地址 | 127.0.0.1 |
| redis.port | redis 端口 | 6379 |
| redis.password | redis 密码 | |

### 使用 LockBs

我们可以直接 `LockBs` 的引导类，这种适合一些更加灵活的场景。

```java
@ContextConfiguration(classes = SpringConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class SpringServiceRawTest {

    @Autowired
    private UserService userService;

    @Autowired
    private LockBs lockBs;

    @Test
    public void queryLogTest() {
        final String key = "name";
        try {
            boolean lockFlag = lockBs.tryLock(key);
            final String value = userService.rawUserName(1L);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        } finally {
            lockBs.unlock(key);
        }
    }

}
```

## aop 注解使用

### 指定方法注解

当然，我们可以在方法上直接指定注解 `@Lock`，使用更加方便。

直接使用，AOP 切面生效即可。

```java
@Service
public class UserService {

    @Lock
    public String queryUserName(Long userId) {
    }

    @Lock(value = "#user.name")
    public void queryUserName2(User user) {
    }
}
```

`@Lock` 属性说明，value 用于指定 key，支持 SPEL 表达式。

其他属性，和引导类的方法参数一一对应。

```java
public @interface Lock {

    /**
     * 缓存的 key 策略，支持 SpEL
     * @return 结果
     */
    String value() default "";

    /**
     * 时间单位
     * @return 单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 等待锁时间
     * @return 等待锁时间
     */
    long waitLockTime() default 10;

    /**
     * 业务加锁时间
     * @return 加锁时间
     */
    long lockTime() default 60;

}
```

# spring boot 整合

## maven 引入

```xml
<dependency>
    <groupId>com.github.houbb</groupId>
    <artifactId>lock-springboot-starter</artifactId>
    <version>1.4.0</version>
</dependency>
```

## 使用

同 spring

# 后期 Road-MAP

- [ ] 支持锁的可重入

持有锁的线程可以多次获取锁

- [x] 分布式锁注解支持

# 拓展阅读

[Redis 分布式锁](https://houbb.github.io/2018/09/08/redis-learn-42-distributed-lock-redis)

[java 从零实现 redis 分布式锁](https://houbb.github.io/2018/09/08/redis-learn-43-distributed-lock-redis-java-impl)

# 缓存相关工具

[cache: 手写渐进式 redis](https://github.com/houbb/cache)

[common-cache: 通用缓存标准定义](https://github.com/houbb/common-cache)

[redis-config: 兼容各种常见的 redis 配置模式](https://github.com/houbb/redis-config)

[lock: 开箱即用的分布式锁](https://github.com/houbb/lock)

[resubmit: 防重复提交](https://github.com/houbb/resubmit)

[rate-limit: 限流](https://github.com/houbb/rate-limit/)
