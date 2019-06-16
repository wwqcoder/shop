package com.wwq.core.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.JedisCluster;

/**
 * @author: Mr.Wang
 * @create: 2019-05-26 00:31
 **/

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring/applicationContext-redis.xml")
public class SpringRedisCluster {

    @Autowired
    JedisCluster jedisCluster;

    @Test
    public void StringClusterTest(){

        jedisCluster.set("num","1234");

        System.out.println(jedisCluster.get("num"));
    }



}
