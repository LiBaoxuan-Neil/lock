package com.github.houbb.lock.spring.config;

import com.github.houbb.common.cache.api.service.ICommonCacheService;
import com.github.houbb.heaven.util.lang.StringUtil;
import com.github.houbb.id.api.Id;
import com.github.houbb.id.core.core.Ids;
import com.github.houbb.lock.core.bs.LockBs;
import com.github.houbb.lock.spring.annotation.EnableLock;
import com.github.houbb.redis.config.core.factory.JedisRedisServiceFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * aop 配置
 *
 * @author binbin.hou
 * @since 0.0.2
 */
@Configuration
@ComponentScan(basePackages = "com.github.houbb.lock.spring")
@Import(LockBeanConfig.class)
public class LockAopConfig implements ImportAware, BeanFactoryPostProcessor {

    @Bean("lockBs")
    public LockBs lockBs() {
        int lockExpireMills = (int) enableLockAttributes.get("lockExpireMills");
        ICommonCacheService commonCacheService = beanFactory.getBean(enableLockAttributes.getString("cache"), ICommonCacheService.class);
        Id id = beanFactory.getBean(enableLockAttributes.getString("id"), Id.class);

        return LockBs.newInstance()
                .cache(commonCacheService)
                .id(id)
                .lockExpireMills(lockExpireMills)
                .init();
    }

    /**
     * 属性信息
     */
    private AnnotationAttributes enableLockAttributes;

    /**
     * bean 工厂
     *
     * @since 0.0.5
     */
    private ConfigurableListableBeanFactory beanFactory;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setImportMetadata(AnnotationMetadata annotationMetadata) {
        enableLockAttributes = AnnotationAttributes.fromMap(
                annotationMetadata.getAnnotationAttributes(EnableLock.class.getName(), false));
        if (enableLockAttributes == null) {
            throw new IllegalArgumentException(
                    "@EnableLock is not present on importing class " + annotationMetadata.getClassName());
        }
    }

}
