package com.github.houbb.lock.spring.aop;

import com.github.houbb.aop.spring.util.SpringAopUtil;
import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.github.houbb.heaven.util.lang.StringUtil;
import com.github.houbb.heaven.util.lang.reflect.ReflectMethodUtil;
import com.github.houbb.heaven.util.util.ArrayUtil;
import com.github.houbb.lock.api.exception.LockException;
import com.github.houbb.lock.core.bs.LockBs;
import com.github.houbb.lock.spring.annotation.Lock;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
            Lock lock = method.getAnnotation(Lock.class);

            // 如果构建 key？
            Object[] args = point.getArgs();
            String lockKey = buildLockKey(lock, method, args);
            try {
                long tryLockMills = lock.tryLockMills();

                boolean tryLockFlag = lockBs.tryLock(tryLockMills, TimeUnit.MILLISECONDS, lockKey);
                if(!tryLockFlag) {
                    log.warn("尝试获取锁失败 {}", lockKey);
                    throw new LockException("尝试获取锁失败 " + lockKey);
                }

                // 执行业务
                return point.proceed();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            } finally {
                boolean unLockFlag = lockBs.unlock(lockKey);
                if(!unLockFlag) {
                    log.warn("尝试释放锁失败 {}", lockKey);
                    // 这里释放异常，没有意义。
                }
            }
        } else {
            return point.proceed();
        }
    }

    /**
     * 构建加锁的 key
     *
     * https://www.cnblogs.com/fashflying/p/6908028.html  spring cache
     *
     * https://www.cnblogs.com/best/p/5748105.html   SpEL
     * @param lock 注解信息
     * @param args 参数
     * @return 结果
     */
    private String buildLockKey(Lock lock,
                                Method method,
                                Object[] args) {
        final String lockValue = lock.value();

        //创建SpEL表达式的解析器
        ExpressionParser parser = new SpelExpressionParser();
        //1. 如果没有入参怎么办？
        if(ArrayUtil.isEmpty(args)) {
            log.warn("对应的数组信息为空，直接返回 key 的值 {}", lockValue);
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
        List<String> paramNameList = ReflectMethodUtil.getParamNames(method);

        for(int i = 0; i < paramNameList.size(); i++) {
            String paramName = paramNameList.get(i);
            Object paramValue = args[i];

            //在上下文中设置变量，变量名为user，内容为user对象
            ctx.setVariable(paramName, paramValue);
        }

        // 直接 toString，比较简单。
        Object value = parser.parseExpression(lockValue).getValue(ctx);
        return ObjectUtil.objectToString(value, "null");
    }

}
