package com.wwq.core.service;

/**
 * @author: Mr.Wang
 * @create: 2019-05-09 00:08
 **/
public interface ItemPageService {

    /**
        通过商品ID 生成商品详情页面
     */
    void genItemHtml(Long goodsId);
}
