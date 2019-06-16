package com.wwq.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wwq.core.pojo.entity.PageResult;
import com.wwq.core.pojo.entity.Result;
import com.wwq.core.pojo.seller.Seller;
import com.wwq.core.service.SellerService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Administrator
 */
@RestController
@RequestMapping("seller")
public class SellerController {


    @Reference
    private SellerService sellerService;

    @RequestMapping("/add")
    public Result add(@RequestBody Seller seller){

        try {
            sellerService.add(seller);
            return new Result(true,"成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,"失败");
        }

    }

    @RequestMapping("/search")
    public PageResult search(Integer page,Integer rows,@RequestBody Seller seller){
        return sellerService.search(page,rows,seller);
    }


}
