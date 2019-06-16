package com.wwq.core.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Set;

/**
 * @author: Mr.Wang
 * @create: 2019-05-03 19:24
 **/

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-redis.xml")
public class TestList {

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void setValue(){
       redisTemplate.boundListOps("nameList").leftPush("1");
       redisTemplate.boundListOps("nameList").leftPush("2");
       redisTemplate.boundListOps("nameList").leftPush("3");
    }

    @Test
    public void getValue(){
        List nameList = redisTemplate.boundListOps("nameList").range(0, -1);
        System.out.println(nameList);
    }

    @Test
    public void testSearchByIndex(){
        Object nameList = redisTemplate.boundListOps("nameList").index(0);
        System.out.println(nameList);
    }

    @Test
    public void deleteValue(){
        redisTemplate.boundListOps("nameList").remove(1,2);
    }




}
