package com.wwq.user.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Mr.Wang
 * @create: 2019-05-14 20:29
 **/
@RestController
@RequestMapping("login")
public class LoginController {

    @RequestMapping("/name")
    public Map showName(){

        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        HashMap<String, String> map = new HashMap<>();
        map.put("loginName",name);
        return map;
    }

}
