package com.github.houbb.lock.test2.service;

import com.github.houbb.lock.spring.annotation.Lock;
import org.springframework.stereotype.Service;

/**
 * @author binbin.hou
 * @since 0.0.1
 */
@Service
public class UserService {

    @Lock
    public void queryInfo(final String id) {
        System.out.println("query info: " + id);
    }

}
