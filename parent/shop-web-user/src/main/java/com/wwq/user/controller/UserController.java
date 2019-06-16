package com.wwq.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wwq.core.pojo.entity.Result;
import com.wwq.core.pojo.user.User;
import com.wwq.core.service.UserService;
import com.wwq.core.utils.PhoneFormatCheckUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Mr.Wang
 * @create: 2019-05-12 23:22
 **/
@RestController
@RequestMapping("user")
public class UserController {

    @Reference
    UserService userService;

    /**
     * 注册用户
     * @param user
     * @param smscode
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody User user, String smscode){

        Boolean checkSmsCode = userService.checkSmsCode(user.getPhone(),smscode);

        if (!checkSmsCode){
            return new Result(false,"验证码输入错误");
        }

        try {
            userService.add(user);
            return new Result(true,"注册成功");
        } catch (Exception e) {
            e.printStackTrace();

            return new Result(false,"注册失败");
        }

    }

    /**
     * 发送验证码
     * @param phone
     * @return
     */
    @RequestMapping("/sendCode")
    public Result sendCode(String phone){

        //判断手机号是否格式正确
        if (!PhoneFormatCheckUtils.isPhoneLegal(phone)){
            return new Result(false,"手机号格式不正确");
        }

        try {
            userService.sendCode(phone);
            return new Result(true,"发送验证码成功");

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"发送验证码失败");

        }


    }
}
