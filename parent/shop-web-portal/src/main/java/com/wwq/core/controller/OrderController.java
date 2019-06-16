package com.wwq.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wwq.core.pojo.entity.Result;
import com.wwq.core.pojo.order.Order;
import com.wwq.core.service.OrderService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Mr.Wang
 * @create: 2019-05-19 18:57
 **/

@RestController
@RequestMapping("order")
public class OrderController {

    @Reference
    OrderService orderService;

    @RequestMapping("/add")
    public Result add(@RequestBody Order order){

        try {
            String user_id = SecurityContextHolder.getContext().getAuthentication().getName();
            order.setUserId(user_id);

            orderService.add(order);
            return new Result(true,"提交订单成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,"提交订单失败");
        }

    }


}
