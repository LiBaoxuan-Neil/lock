package com.github.houbb.lock.test2.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author binbin.hou
 * @since 0.0.2
 */
@ContextConfiguration(classes = MyApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest {

    @Autowired
    private UserService service;

    @Test
    public void queryTest() {
        service.queryInfo("1");
    }

}
