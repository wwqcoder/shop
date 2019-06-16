package com.wwq.core.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 显示用户名
 * @author Administrator
 */
@RestController
@RequestMapping("login")
public class LoginController {

    @RequestMapping("/showName")
    public Map<String, Object> showName(){
        HashMap<String, Object> map = new HashMap<>();
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        map.put("username",name);
        map.put("cur_time",new Date());
        return map;
    }
}
