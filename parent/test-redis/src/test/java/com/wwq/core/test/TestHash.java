package com.wwq.core.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author: Mr.Wang
 * @create: 2019-05-03 19:24
 **/

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-redis.xml")
public class TestHash {

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void setValue(){
        redisTemplate.boundHashOps("nameHash").put("1","我");
        redisTemplate.boundHashOps("nameHash").put("2","爱");
        redisTemplate.boundHashOps("nameHash").put("3","你");
    }

    @Test
    public void getValue(){
        //Object nameHash = redisTemplate.boundHashOps("nameHash").get("1");
        Set<String> nameHash = redisTemplate.boundHashOps("nameHash").keys();
        List nameHash1 = redisTemplate.boundHashOps("nameHash").values();
        System.out.println(nameHash+"---"+nameHash1);

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
