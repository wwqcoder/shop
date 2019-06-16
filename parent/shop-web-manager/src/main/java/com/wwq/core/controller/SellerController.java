package com.wwq.core.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.wwq.core.pojo.entity.PageResult;
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
    SellerService sellerService;

    @RequestMapping("/search")
    public PageResult search(Integer page, Integer rows, @RequestBody Seller seller){
        return sellerService.search(page, rows, seller);

    }

    @RequestMapping("/findOne")
    public Seller findOne(String id){
        return sellerService.findSeller(id);
    }

    /**
     * 审核
     * @param sellerId
     * @param status
     */
    @RequestMapping("/updateStatus")
    public void updateStatus(String sellerId,String status){
        sellerService.updateStatus(sellerId,status);
    }


}
