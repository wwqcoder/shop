package com.wwq.core.service;

import com.wwq.core.pojo.item.Item;
import com.wwq.core.pojo.vo.Cart;

import java.util.List;

/**
 * @author: Mr.Wang
 * @create: 2019-05-16 20:28
 **/
public interface CartService {

    Item findItemById(Long itemId);

    List<Cart> findCartList(List<Cart> cartList);

    /**
     * //将合并的购物车存到redis中
     * @param cartList
     * @param name
     */
    void addCartListToRedis(List<Cart> cartList, String name);

    /**
     * 从缓存中获取购物车列表
     * @param name
     * @return
     */
    List<Cart> findCartListFromRedis(String name);
}
