package com.github.houbb.lock.test.spring;


import com.github.houbb.lock.core.bs.LockBs;
import com.github.houbb.lock.test.config.SpringConfig;
import com.github.houbb.lock.test.model.User;
import com.github.houbb.lock.test.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author binbin.hou
 * @since 1.0.0
 */
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
