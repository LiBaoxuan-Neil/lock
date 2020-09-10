# 项目简介

为 java 设计的锁。

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.houbb/lock/badge.svg)](http://mvnrepository.com/artifact/com.github.houbb/lock)
[![Build Status](https://www.travis-ci.org/houbb/lock.svg?branch=master)](https://www.travis-ci.org/houbb/lock?branch=master)
[![Coverage Status](https://coveralls.io/repos/github/houbb/lock/badge.svg?branch=master)](https://coveralls.io/github/houbb/lock?branch=master)

## 目的

- 基于 redis 的分布式锁

- 基于 oracle 的分布式锁

- 基于 mysql 的分布式锁

# 变更日志

> [变更日志](doc/CHANGELOG.md)

# 快速开始 

## 需要 

jdk1.7+

maven 3.x+

## maven 引入 

```xml
<dependency>
    <groupId>com.github.houbb</groupId>
    <artifactId>lock-core</artifactId>
    <version>${最新版本}</version>
</dependency>
```

## 入门例子

基于本地 redis 的测试案例。

```java
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
```

# 后期 Road-MAP

- [ ] 支持锁的可重入

持有锁的线程可以多次获取锁

- [ ] redis 实现的支持

cluster 支持

redis 支持

aliyun-redis 支持

各种各样的声明方式的默认支持

