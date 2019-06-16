package com.wwq.core.service;

import com.wwq.core.pojo.entity.PageResult;
import com.wwq.core.pojo.seller.Seller;

/**
 * @author Administrator
 */
public interface SellerService {

    /**
     * 添加商家
     * @param seller
     */
    void add(Seller seller);

    Seller findOne(String username);

    /**
     * 分页查询商家列表
     * @param page
     * @param rows
     * @param seller
     * @return
     */
    PageResult search(Integer page, Integer rows, Seller seller);

    /**
     * 根据Id查询商家
     * @param sellerId
     * @return
     */
    Seller findSeller(String sellerId);

    /**
     * 审核状态修改
     * @param sellerId
     * @param status
     */
    void updateStatus(String sellerId, String status);
}
