package com.github.houbb.lock.test;

import org.junit.Assert;
import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * @author binbin.hou
 * @since 0.0.1
 */
public class JedisTest {

    @Test
    public void helloTest() {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        jedis.set("key", "001");
        String value = jedis.get("key");

        Assert.assertEquals("001", value);
    }

}
