package com.wwq.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wwq.core.pojo.entity.PageResult;
import com.wwq.core.pojo.entity.Result;
import com.wwq.core.pojo.good.Goods;
import com.wwq.core.pojo.vo.GoodsVo;
import com.wwq.core.service.GoodsService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Mr.Wang
 * @create: 2019-04-29 14:29
 **/
@RestController
@RequestMapping("goods")
public class GoodsController {

    @Reference
    GoodsService goodsService;

    @RequestMapping("/add")
    public Result add(@RequestBody GoodsVo goodsVo){
        try {
            //商家ID
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            goodsVo.getGoods().setSellerId(name);
            goodsService.add(goodsVo);
            return new Result(true,"成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"失败");
        }
    }

    /**
     * 分页查询商品列表
     * @param page
     * @param rows
     * @param goods
     * @return
     */
    @RequestMapping("/search")
    public PageResult search(Integer page, Integer rows, @RequestBody Goods goods){

        /*
            获取用户名
         */
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
        goods.setSellerId(sellerId);

        return goodsService.search(page,rows,goods);
    }

    /**
     * 通过ID查询商品包装实体类
     * @param id
     * @return
     */
    @RequestMapping("/findOne")
    public GoodsVo findOne(Long id){
        return goodsService.findOne(id);
    }

    /**
     * 商品修改
     * @param goodsVo
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody GoodsVo goodsVo){


        try {
            //获取用户名存入商品实体
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            goodsVo.getGoods().setSellerId(name);
            goodsService.update(goodsVo);
            return new Result(true,"成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"失败");
        }

    }





}
