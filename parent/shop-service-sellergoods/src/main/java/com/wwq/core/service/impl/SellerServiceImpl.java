package com.wwq.core.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wwq.core.dao.seller.SellerDao;
import com.wwq.core.pojo.entity.PageResult;
import com.wwq.core.pojo.seller.Seller;
import com.wwq.core.pojo.seller.SellerQuery;
import com.wwq.core.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 *
 * @author Administrator
 */
@Service
@Transactional
public class SellerServiceImpl implements SellerService {

    @Autowired
    SellerDao sellerDao;

    @Override
    public void add(Seller seller) {

        //密码加密
        seller.setPassword(new BCryptPasswordEncoder().encode(seller.getPassword()));

        //为审核的商品
        seller.setStatus("0");
        //商品注册的时间
        seller.setCreateTime(new Date());

        sellerDao.insertSelective(seller);
    }

    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    @Override
    public Seller findOne(String username) {
        return sellerDao.selectByPrimaryKey(username);
    }

    @Override
    public PageResult search(Integer page, Integer rows, Seller seller) {
        PageHelper.startPage(page,rows);
        SellerQuery sellerQuery = new SellerQuery();
        sellerQuery.createCriteria().andStatusEqualTo(seller.getStatus());
        Page<Seller> pages = (Page<Seller>) sellerDao.selectByExample(sellerQuery);
        return new PageResult(pages.getTotal(),pages.getResult());
    }

    @Override
    public Seller findSeller(String sellerId) {
        Seller seller = sellerDao.selectByPrimaryKey(sellerId);
        return seller;
    }

    @Override
    public void updateStatus(String sellerId, String status) {
        Seller seller = new Seller();
        seller.setSellerId(sellerId);
        seller.setStatus(status);
        sellerDao.updateByPrimaryKeySelective(seller);
    }
}
