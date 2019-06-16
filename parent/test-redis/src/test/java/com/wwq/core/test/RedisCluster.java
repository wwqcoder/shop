package com.wwq.core.test;

import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;

/**
 * @author: Mr.Wang
 * @create: 2019-05-26 00:19
 **/
public class RedisCluster {

    /**
     * 测试redis集群
     */
    @Test
    public void testCluster(){

        //连接池配置类
        JedisPoolConfig config = new JedisPoolConfig();
        //设置最大连接数
        config.setMaxTotal(5);
        //设置最大间隔数
        config.setMaxIdle(3);

        //节点集
        HashSet<HostAndPort> hostAndPorts = new HashSet<>();
        //配置多个IP及端口号
        hostAndPorts.add(new HostAndPort("192.168.200.128",7001));
        hostAndPorts.add(new HostAndPort("192.168.200.128",7002));
        hostAndPorts.add(new HostAndPort("192.168.200.128",7003));
        hostAndPorts.add(new HostAndPort("192.168.200.128",7004));
        hostAndPorts.add(new HostAndPort("192.168.200.128",7005));
        hostAndPorts.add(new HostAndPort("192.168.200.128",7006));

        //创建客户端集群版
        JedisCluster jedisCluster = new JedisCluster(hostAndPorts, config);

        //保存数据
        jedisCluster.set("name","王玮琦");

        //获取数据
        System.out.println(jedisCluster.get("name"));


    }

}
