package com.wwq.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.wwq.core.pojo.entity.Result;
import com.wwq.core.pojo.item.Item;
import com.wwq.core.pojo.order.OrderItem;
import com.wwq.core.pojo.vo.Cart;
import com.wwq.core.service.CartService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Mr.Wang
 * @create: 2019-05-14 21:46
 **/

@RestController
@RequestMapping("cart")
public class CartController {

    @Reference
    CartService cartService;

    //加入购物车
    @RequestMapping("/addGoodsToCartList")
    @CrossOrigin(origins = {"http://localhost:9003","http://localhost:9007"},allowCredentials = "true")
    public Result addGoodsToCartList(Long itemId, Integer num, HttpServletResponse response
            ,HttpServletRequest request){

        try {
//            未登陆
            List<Cart> cartList = null;

            //标记
            boolean flag = false;
//            1:获取Cookie
            Cookie[] cookies = request.getCookies();
            if(null != cookies && cookies.length > 0){
                for (Cookie cookie : cookies) {
//            2:获取Cookie中购物车集合
                    if("CART".equals(cookie.getName())){
                        cartList = JSON.parseArray(cookie.getValue(), Cart.class);
                        flag = true;
                    }
                }
            }
//            3:没有 创建购物车
            if(null == cartList){
                cartList = new ArrayList<>();
            }
//            4:追加当前款
            Cart newCart = new Cart();

            //创建新的订单详情对象
            OrderItem newOrderItem = new OrderItem();
            //库存ID
            newOrderItem.setItemId(itemId);
            //数量
            newOrderItem.setNum(num);
            //创建订单详情集合
            List<OrderItem> newOrderItemList = new ArrayList<>();
            newOrderItemList.add(newOrderItem);
            newCart.setOrderItemList(newOrderItemList);

            //根据库存ID查询 商家ID
            Item item = cartService.findItemById(itemId);

            newCart.setSellerId(item.getSellerId());


            //1:判断新购物车中商家是否在老购物车集合中已经存在
            int indexOf = cartList.indexOf(newCart);   // indexOf 不存在是-1  存在返回角标
            if(indexOf != -1){
                //-- 存在
                //2:判断新购物车中的新订单详情在
                //从老购物车集合中找出跟新购物车相同商家的老购物车的老订单详情集合中是否存在
                Cart oldCart = cartList.get(indexOf); //此老车就和新车是同商家
                List<OrderItem> oldOrderItemList = oldCart.getOrderItemList();
                int i = oldOrderItemList.indexOf(newOrderItem);
                if(i != -1){
                    //-- 存在   追加数量
                    OrderItem oldOrderItem = oldOrderItemList.get(i);
                    oldOrderItem.setNum(oldOrderItem.getNum() + newOrderItem.getNum());
                }else{
                    //-- 不存在  直接添加
                    oldOrderItemList.add(newOrderItem);
                }

            }else{
                //-- 不存在 直接添加
                cartList.add(newCart);
            }

            //判断用户是否登录   是否获取登录名
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            if (!"anonymousUser".equals(name)){
                //已登录

                //将合并的购物车存到redis中  采用hash
                cartService.addCartListToRedis(cartList,name);

                if (flag){
                    //清空购物车
                    Cookie cookie = new Cookie("CART", null);
                    cookie.setPath("/");
                    cookie.setMaxAge(0);  // 0 立即销毁  -1： 关闭浏览器销毁，  >0: 到时间销毁
                    response.addCookie(cookie);
                }

            }else {
                //未登录
    //            5:创建Cookie添加购物车集合
                Cookie cookie = new Cookie("CART",JSON.toJSONString(cartList));
                cookie.setMaxAge(60*60*24*365);
                cookie.setPath("/");
    //            6:回写浏览器
                response.addCookie(cookie);

            }


            return new Result(true,"加入购物车成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"加入购物车失败");
        }

    }

    //查询购物车集合或列表
    @RequestMapping("/findCartList")
    public List<Cart> findCartList(HttpServletRequest request,HttpServletResponse response){

//        未登陆
//        1:获取Cookie
        List<Cart> cartList = null;
//        2:获取Cookie中购物车集合
        Cookie[] cookies = request.getCookies();
        if (null != cookies && cookies.length > 0){
            for (Cookie cookie : cookies) {
                if ("CART".equals(cookie.getName())){
                    cartList = JSON.parseArray(cookie.getValue(), Cart.class);
                    break;
                }
            }
        }
        //判断是否登陆 是否获取当前登陆人的用户名        空指针异常
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!"anonymousUser".equals(name)){
            //已登录  将cookie中的购物车合并到redis中
            if (null != cartList){
                cartService.addCartListToRedis(cartList,name);
                //然后清空购物车
                Cookie cookie = new Cookie("CART", null);
                cookie.setPath("/");
                cookie.setMaxAge(0);   // 0 立即销毁  -1： 关闭浏览器销毁，  >0: 到时间销毁
                response.addCookie(cookie);
            }

            //cookie中没有，从缓存中获取购物车列表
            cartList = cartService.findCartListFromRedis(name);
        }
//        3:有  将购物车装满
        if (null != cartList && cartList.size() > 0){
            cartList = cartService.findCartList(cartList);
        }
//        4:回显

        return cartList;
    }


}
