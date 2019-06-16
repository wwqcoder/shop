package com.wwq.core.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.wwq.core.dao.item.ItemDao;
import com.wwq.core.dao.log.PayLogDao;
import com.wwq.core.dao.order.OrderDao;
import com.wwq.core.dao.order.OrderItemDao;
import com.wwq.core.pojo.item.Item;
import com.wwq.core.pojo.log.PayLog;
import com.wwq.core.pojo.order.Order;
import com.wwq.core.pojo.order.OrderItem;
import com.wwq.core.pojo.vo.Cart;
import com.wwq.core.service.OrderService;
import com.wwq.core.utils.IdWorker;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author: Mr.Wang
 * @create: 2019-05-19 19:04
 *
 * 保存订单主表和订单详情表
 **/

@Service
@Transactional
public class OrderServiceImpl implements OrderService {


    @Autowired
    IdWorker idWorker;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    OrderDao orderDao;
    @Autowired
    ItemDao itemDao;
    @Autowired
    OrderItemDao orderItemDao;
    @Autowired
    PayLogDao payLogDao;



    /**
     * 保存订单
     * @param order
     */
    @Override
    public void add(Order order) {



        //真正的总金额
        long tp = 0;
        ArrayList<String> ids = new ArrayList<>();


        List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("CART").get(order.getUserId());
        for (Cart cart : cartList) {

            long id = idWorker.nextId();
            ids.add(String.valueOf(id));
            //订单id
            order.setOrderId(id);
            //实付金额
            double totalPrice = 0;

            List<OrderItem> orderItemList = cart.getOrderItemList();
            for (OrderItem orderItem : orderItemList) {
                //ID
                orderItem.setId(idWorker.nextId());
                //
                Item item = itemDao.selectByPrimaryKey(orderItem.getItemId());
                //商品ID
                orderItem.setGoodsId(item.getGoodsId());
                //标题
                orderItem.setTitle(item.getTitle());
                //单价
                orderItem.setPrice(item.getPrice());
                //小计
                orderItem.setTotalFee(new BigDecimal(orderItem.getPrice().doubleValue() * orderItem.getNum()));
                //订单ID
                orderItem.setOrderId(id);
                //图片
                orderItem.setPicPath(item.getImage());
                //商家ID
                orderItem.setSellerId(item.getSellerId());
                //实付金额
                totalPrice += orderItem.getTotalFee().doubleValue();
                //保存订单项
                orderItemDao.insertSelective(orderItem);
            }

            //实付金额
            order.setPayment(new BigDecimal(totalPrice));

            tp += order.getPayment().doubleValue()*100;
            //状态
            order.setStatus("1");
            //添加时间
            order.setCreateTime(new Date());
            //更新时间
            order.setUpdateTime(new Date());
            //订单来源
            order.setSourceType("2");
            //商家ID
            order.setSellerId(cart.getSellerId());
            //保存订单
            orderDao.insertSelective(order);

        }

        //保存日志表(支付表)
        PayLog payLog = new PayLog();
        //支付订单号
        payLog.setOutTradeNo(String.valueOf(idWorker.nextId()));
        //添加时间
        payLog.setCreateTime(new Date());
        //支付金额
        payLog.setTotalFee(tp);
        //用户ID
        payLog.setUserId(order.getUserId());
        //支付状态
        payLog.setTradeState("0");
        //订单ID列表
        payLog.setOrderList(ids.toString().replace("[","").replace("]",""));

        //支付类型
        payLog.setPayType(order.getPaymentType());

        payLogDao.insertSelective(payLog);

        //清除redis  24小时就清除了
       //todo  redisTemplate.boundHashOps("CART")
        redisTemplate.boundValueOps(order.getUserId()).set(payLog,24, TimeUnit.HOURS);

        //清理购物车
//        redisTemplate.boundHashOps("CART").delete(order.getUserId());



    }
}
