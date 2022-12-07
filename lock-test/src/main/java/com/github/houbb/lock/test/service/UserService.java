package com.github.houbb.lock.test.service;

import com.github.houbb.lock.spring.annotation.Lock;
import com.github.houbb.lock.test.model.User;
import org.springframework.stereotype.Service;

/**
 * @author binbin.hou
 * @since 1.0.0
 */
@Service
public class UserService {

    @Lock
    public String queryUserName(Long userId) {
        return userId+"-name";
    }

    @Lock(value = "#arg0.name")
    public void queryUserName2(User user) {
        System.out.println("user: " + user.toString());
    }
}
