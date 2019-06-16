package com.wwq.core.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Set;

/**
 * @author: Mr.Wang
 * @create: 2019-05-03 19:24
 **/

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-redis.xml")
public class TestSet {

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void setValue(){
        redisTemplate.boundSetOps("name").add("郭先生");
        redisTemplate.boundSetOps("name").add("那蓝");
        redisTemplate.boundSetOps("name").add("温迪");
    }

    @Test
    public void getValue(){
        Set names = redisTemplate.boundSetOps("name").members();
        System.out.println(names);
    }

    @Test
    public void deleteValue(){
//        redisTemplate.boundSetOps("name").remove("那蓝");
        redisTemplate.delete("name");
    }




}
