package com.wwq.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wwq.core.pojo.address.Address;
import com.wwq.core.service.AddressService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: Mr.Wang
 * @create: 2019-05-18 21:05
 **/

@RestController
@RequestMapping("address")
public class AddressController {

    @Reference
    AddressService addressService;

    /**
     * 通过user_id查询地址列表
     * @return
     */
    @RequestMapping("/findListByLoginUser")
    public List<Address> findListByLoginUser(){
        //获取登录人
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return addressService.findListByLoginUser(name);
    }
}
