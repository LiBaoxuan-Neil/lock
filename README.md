# 项目简介

为 java 设计的锁。

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.houbb/lock/badge.svg)](http://mvnrepository.com/artifact/com.github.houbb/lock)
[![Build Status](https://www.travis-ci.org/houbb/lock.svg?branch=master)](https://www.travis-ci.org/houbb/lock?branch=master)
[![Coverage Status](https://coveralls.io/repos/github/houbb/lock/badge.svg?branch=master)](https://coveralls.io/github/houbb/lock?branch=master)

## 目的

- 基于 redis 的分布式锁

- 整合 spring

- 整合 spring-boot

- 开箱即用，支持注解。

- 支持多种 redis 的声明方式

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
    <version>1.1.0</version>
</dependency>
```

## 入门例子

基于本地 redis 的测试案例。

```java
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
```

# 整合 spring

## maven 引入

```xml
<dependency>
    <groupId>com.github.houbb</groupId>
    <artifactId>lock-spring</artifactId>
    <version>1.1.0</version>
</dependency>
```

## 指定 bean 使用

### 启用分布式锁

`@EnableLock` 启用分布式锁。

```xml
@Configurable
@ComponentScan(basePackages = "com.github.houbb.lock.test.service")
@EnableLock
public class SpringConfig {
}
```

### 使用 LockBs

我们可以直接 `LockBs` 的引导类。

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
            lockBs.tryLock(key);
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

支持 SPEL 表达式。

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

直接使用，AOP 切面生效即可。

# springboot 整合

## maven 引入

```xml
<dependency>
    <groupId>com.github.houbb</groupId>
    <artifactId>lock-springboot-starter</artifactId>
    <version>1.1.0</version>
</dependency>
```

## 使用

同 spring

# 后期 Road-MAP

- [ ] 支持锁的可重入

持有锁的线程可以多次获取锁

- [x] 分布式锁注解支持

