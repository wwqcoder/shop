package com.wwq.core.service;

import com.wwq.core.pojo.entity.PageResult;
import com.wwq.core.pojo.good.Goods;
import com.wwq.core.pojo.vo.GoodsVo;

/**
 * @author: Mr.Wang
 * @create: 2019-04-29 14:32
 **/
public interface GoodsService {

    /**
     * 添加商品信息
     * @param goodsVo
     */
    void add(GoodsVo goodsVo);

    /**
     * 分页查询商品列表
     * @param page
     * @param rows
     * @param goods
     * @return
     */
    PageResult search(Integer page, Integer rows, Goods goods);

    /**
     * 通过ID查询商品包装实体类
     * @param id
     * @return
     */
    GoodsVo findOne(Long id);

    /**
     * 商品修改
     * @param goodsVo
     */
    void update(GoodsVo goodsVo);

    /**
     * 修改商品状态
     * @param ids
     * @param status
     */
    void updateStatus(Long[] ids, String status);

    /**
     * 逻辑删除商品
     * @param ids
     */
    void delete(Long[] ids);
}
