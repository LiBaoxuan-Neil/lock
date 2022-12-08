package com.github.houbb.lock.spring.aop;

import com.github.houbb.aop.spring.util.SpringAopUtil;
import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.github.houbb.heaven.util.lang.StringUtil;
import com.github.houbb.heaven.util.util.ArrayUtil;
import com.github.houbb.lock.api.exception.LockException;
import com.github.houbb.lock.core.bs.LockBs;
import com.github.houbb.lock.core.support.handler.LockReleaseFailHandlerContext;
import com.github.houbb.lock.spring.annotation.Lock;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author binbin.hou
 * @since 1.1.0
 */
@Aspect
@Component
public class LockAspect {

    private final Log log = LogFactory.getLog(LockAspect.class);

    @Autowired
    @Qualifier("lockBs")
    private LockBs lockBs;

    @Pointcut("@annotation(com.github.houbb.lock.spring.annotation.Lock)")
    public void lockPointcut() {
    }

    /**
     * 执行核心方法
     *
     * 相当于 MethodInterceptor
     * @param point 切点
     * @return 结果
     * @throws Throwable 异常信息
     * @since 0.0.2
     */
    @Around("lockPointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Method method = SpringAopUtil.getCurrentMethod(point);

        boolean isLockAnnotation = method.isAnnotationPresent(Lock.class);
        if(isLockAnnotation) {
            Lock lockAnnotation = method.getAnnotation(Lock.class);

            // 如果构建 key？
            String lockKey = buildLockKey(lockAnnotation, point);

            boolean tryLockFlag = false;
            try {
                tryLockFlag = lockBs.tryLock(lockKey, lockAnnotation.timeUnit(), lockAnnotation.lockTime(), lockAnnotation.waitLockTime());
                if(!tryLockFlag) {
                    log.warn("[LOCK] TRY LOCK FAILED {}", lockKey);
                    throw new LockException("[LOCK] TRY LOCK FAILED " + lockKey);
                }

                // 执行业务
                return point.proceed();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            } finally {
                // 只有获取锁的情况下，才尝试释放锁
                if(tryLockFlag) {
                    boolean unLockFlag = lockBs.unlock(lockKey);

                    // 释放锁结果
                    this.lockReleaseFailHandle(unLockFlag, lockKey);
                }
            }
        } else {
            return point.proceed();
        }
    }

    /**
     * 锁释放失败
     * @param unLockFlag 释放结果
     * @param lockKey 加锁信息
     */
    private void lockReleaseFailHandle(boolean unLockFlag,
                                       String lockKey) {
        if(unLockFlag) {
            return;
        }

        // 触发通知，便于用户自定义处理。
        // 比如报警
        LockReleaseFailHandlerContext handlerContext = LockReleaseFailHandlerContext.newInstance()
                .key(lockKey);
        this.lockBs.lockReleaseFailHandler().handle(handlerContext);
    }

    /**
     * 构建加锁的 key
     *
     * https://www.cnblogs.com/fashflying/p/6908028.html  spring cache
     *
     * https://www.cnblogs.com/best/p/5748105.html   SpEL
     * @param lock 注解信息
     * @param joinPoint 参数
     * @return 结果
     */
    private String buildLockKey(Lock lock,
                                ProceedingJoinPoint joinPoint) {
        final Object[] args = joinPoint.getArgs();
        final String lockValue = lock.value();

        //创建SpEL表达式的解析器
        ExpressionParser parser = new SpelExpressionParser();
        //1. 如果没有入参怎么办？
        if(ArrayUtil.isEmpty(args)) {
            log.warn("[LOCK] method args is empty, return lock.value() {}", lockValue);
            return lockValue;
        }

        // 如果 lockValue 的值为空呢？
        if(StringUtil.isEmpty(lockValue)) {
            return Arrays.toString(args);
        }

        // 如何获取参数名称呢？

        //解析表达式需要的上下文，解析时有一个默认的上下文
        // jdk1.7 之前，直接使用 arg0, arg1...
        EvaluationContext ctx = new StandardEvaluationContext();

        // 利用 spring 的处理方式
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        String[] paramNameList = methodSignature.getParameterNames();

        for(int i = 0; i < paramNameList.length; i++) {
            String paramName = paramNameList[i];
            Object paramValue = args[i];

            //在上下文中设置变量，变量名为user，内容为user对象
            ctx.setVariable(paramName, paramValue);
        }

        // 直接 toString，比较简单。
        Object value = parser.parseExpression(lockValue).getValue(ctx);
        return ObjectUtil.objectToString(value, "null");
    }

}
