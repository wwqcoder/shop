package com.wwq.core.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.wwq.core.dao.item.ItemDao;
import com.wwq.core.pojo.item.Item;
import com.wwq.core.pojo.order.OrderItem;
import com.wwq.core.pojo.vo.Cart;
import com.wwq.core.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Mr.Wang
 * @create: 2019-05-16 20:29
 **/

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    ItemDao itemDao;
    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public Item findItemById(Long itemId) {
        return itemDao.selectByPrimaryKey(itemId);
    }

    /**
     * 将购物车装满
     * @param cartList
     * @return
     */
    @Override
    public List<Cart> findCartList(List<Cart> cartList) {

        for (Cart cart : cartList) {

            List<OrderItem> orderItemList = cart.getOrderItemList();
            for (OrderItem orderItem : orderItemList) {

                Item item = findItemById(orderItem.getItemId());
                //图片
                orderItem.setPicPath(item.getImage());
                //标题
                orderItem.setTitle(item.getTitle());
                //价格
                orderItem.setPrice(item.getPrice());
                //小计
                orderItem.setTotalFee(new BigDecimal(orderItem.getPrice().doubleValue()*orderItem.getNum()));

                cart.setSellerName(item.getSeller());
            }
        }
        return cartList;
    }

    /**
     * //将合并的购物车存到redis中
     * @param newCartList
     * @param name
     */
    @Override
    public void addCartListToRedis(List<Cart> newCartList, String name) {

        //先获取redis中的购物车
        List<Cart> oldCartList = (List<Cart>) redisTemplate.boundHashOps("CART").get(name);
        //新老车合并到老车
        oldCartList = mergeCartList(newCartList,oldCartList);
        //将合并后的老车保存到redis,覆盖到之前的购物车
        redisTemplate.boundHashOps("CART").put(name,oldCartList);


    }

    /**
     * 从缓存中获取购物车列表
     * @param name
     * @return
     */
    @Override
    public List<Cart> findCartListFromRedis(String name) {
        return (List<Cart>) redisTemplate.boundHashOps("CART").get(name);
    }

    /**
     * 合并新老购物车
     * @param newCartList
     * @param oldCartList
     * @return
     */
    private List<Cart> mergeCartList(List<Cart> newCartList, List<Cart> oldCartList) {
        //判断新车集合是否为空
        if (null != newCartList && newCartList.size() > 0){
            //判断老车集合是否为空
            if (null != oldCartList && oldCartList.size() > 0){
                //新老车大合并
                for (Cart newCart : newCartList) {
                    //1,判断新购物车的商家是否在老购物车中存在
                    int j = oldCartList.indexOf(newCart);
                    if (j != -1){
                        //存在  新旧是一家
                        Cart oldCart = oldCartList.get(j);
                        //获取老订单详情列表
                        List<OrderItem> oldOrderItemList = oldCart.getOrderItemList();
                        //获取新订单详情列表
                        List<OrderItem> newOrderItemList = newCart.getOrderItemList();

                        for (OrderItem newOrderItem : newOrderItemList) {
                            //判断新的订单详情是否在老的订单详情中存在
                            int z = oldOrderItemList.indexOf(newOrderItem);
                            if (z != -1){
                                //存在  数量相加
                                OrderItem oldOrderItem = oldOrderItemList.get(z);
                                oldOrderItem.setNum(oldOrderItem.getNum() + newOrderItem.getNum());
                            }else {
                                //不存在
                                oldOrderItemList.add(newOrderItem);
                            }
                        }

                    }else {
                        //不存在  直接添加
                        oldCartList.add(newCart);

                    }

                }
            }else {
                return newCartList;
            }
        }
        return oldCartList;
    }
}
