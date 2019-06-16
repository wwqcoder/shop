package com.wwq.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.wwq.core.dao.user.UserDao;
import com.wwq.core.pojo.user.User;
import com.wwq.core.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.*;
import java.util.Date;
import java.util.HashMap;

/**
 * @author: Mr.Wang
 * @create: 2019-05-12 23:29
 **/

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    JmsTemplate jmsTemplate;
    @Autowired
    Destination smsDestination;

    @Value("${template_code}")
    private String template_code;

    @Value("${sign_name}")
    private String sign_name;



    /**
     * 注册用户
     * @param user
     */
    @Override
    public void add(User user) {
        //创建时间
        user.setCreated(new Date());
        //更新时间
        user.setUpdated(new Date());
        //注册来源
        user.setSourceType("1");
        //对密码加密
        String password = DigestUtils.md5Hex(user.getPassword());
        user.setPassword(password);

        userDao.insertSelective(user);


    }

    /**
     * 发送验证码
     * @param phone
     */
    @Override
    public void sendCode(String phone) {

        //生成6为随机验证码
        String smscode = (long) (Math.random() * 1000000) + "";
        System.out.println("验证码为："+smscode);


        //将验证码放入redis
        redisTemplate.boundHashOps("smscode").put(phone,smscode);

        //将消息内容发送给mq
        jmsTemplate.send(smsDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage message = session.createMapMessage();
                message.setString("mobile",phone);
                message.setString("template_code",template_code);
                message.setString("sign_name",sign_name);
                HashMap<Object, Object> map = new HashMap<>();
                map.put("code",smscode);
                message.setString("param", JSON.toJSONString(map));
                return message;
            }
        });
    }

    /**
     * 检查验证码
     * @param phone
     * @param smscode
     * @return
     */
    @Override
    public Boolean checkSmsCode(String phone, String smscode) {
       return redisTemplate.boundHashOps("smscode").get(phone).equals(smscode);
    }
}
