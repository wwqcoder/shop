package com.wwq.core.service;

import com.wwq.core.pojo.user.User;

/**
 * @author: Mr.Wang
 * @create: 2019-05-12 23:27
 **/
public interface UserService {
    /**
     * 用户注册
     * @param user
     */
    void add(User user);

    /**
     * 发送验证码
     * @param phone
     */
    void sendCode(String phone);

    /**
     * 检查验证码
     * @param phone
     * @param smscode
     * @return
     */
    Boolean checkSmsCode(String phone, String smscode);
}
