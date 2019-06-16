package com.wwq.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wwq.core.pojo.entity.Result;
import com.wwq.core.service.PayService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author: Mr.Wang
 * @create: 2019-05-20 14:27
 **/

@RestController
@RequestMapping("pay")
public class PayController {

    @Reference
    PayService payService;


    /**
     * 创建支付本地
     * @return
     */
    @RequestMapping("/createNative")
    public Map<String,String> createNative(){

        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return payService.createNative(name);

    }

    /**
     * 查询支付状态
     * @param out_trade_no
     * @return
     */
    @RequestMapping("/queryPayStatus")
    public Result queryPayStatus(String out_trade_no){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        int[] a = {1,2,3};
        System.out.println(a.length);
        String s = "123";


        try {
            int x = 0;
            while (true){
                //调用查询接口
                Map<String,String> map = payService.queryPayStatus(out_trade_no,name);
                //判断支付状态
                if ("NOTPAY".equals(map.get("trade_state"))){
                    //未支付
                    //睡一会儿
                    Thread.sleep(3000);
                    x++;
                    if (x > 100){
                        return new Result(false,"支付超时");
                    }
                }else {
                    return new Result(true,"支付成功");
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return new Result(false,"支付失败");
        }
    }
}
