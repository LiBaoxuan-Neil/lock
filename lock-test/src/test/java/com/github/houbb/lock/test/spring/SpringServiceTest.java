package com.github.houbb.lock.test.spring;


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
public class SpringServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void queryLogTest() {
        final String key = "name";
        final String value = userService.queryUserName(1L);
    }

    @Test
    public void queryUserNameTest() {
        User user = new User();
        user.setId("001");
        user.setName("u-001");
        userService.queryUserName2(user);
    }

}
