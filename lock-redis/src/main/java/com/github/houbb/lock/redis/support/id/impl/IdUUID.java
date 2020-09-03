package com.github.houbb.lock.redis.support.id.impl;

import com.github.houbb.lock.redis.support.id.IId;

import java.util.UUID;

/**
 * @author binbin.hou
 * @since 1.0.0
 */
public class IdUUID implements IId {

    /**
     * 返回唯一标识
     *
     * @return 唯一标识
     */
    @Override
    public String id() {
        return UUID.randomUUID().toString().replace("-","");
    }

}
