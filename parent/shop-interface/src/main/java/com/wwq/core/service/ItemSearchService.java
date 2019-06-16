package com.wwq.core.service;

import com.wwq.core.pojo.item.Item;

import java.util.List;
import java.util.Map;

/**
 * @author: Mr.Wang
 * @create: 2019-05-06 09:21
 **/
public interface ItemSearchService {

    /**
     * 根据关键字查询商品
     * @param searchMap
     */
    Map<String, Object> search(Map searchMap);


    /**
     * 通过goodIdH和状态查询item列表
     * @param id
     * @param status
     * @return
     */
    public List<Item> findItemListByGoodsIdAndStatus(Long id, String status);

    /**
     * 导入索引库
     * @param list
     */
    void importList(List<Item> list);

    /**
     * 通过goodsId删除索引库
     * @param goodsId
     */
    void deleteByGoodsId(Long goodsId);


}
