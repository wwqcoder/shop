package com.wwq.core.service;

import com.wwq.core.pojo.item.ItemCat;

import java.util.List;

/**
 * @author Administrator
 */
public interface ItemCatService {
    /**
     * 根据父ID查询分类列表
     * @param parentId
     * @return
     */
    List<ItemCat> findByParentId(Long parentId);

    /**
     * 添加分类项
     * @param itemCat
     */
    void add(ItemCat itemCat);

    /**
     * 通过ID查询分类
     * @param id
     * @return
     */
    ItemCat findOne(Long id);

    /**
     * 修改分类
     * @param itemCat
     */
    void update(ItemCat itemCat);

    /**
     * 查询分类列表
     * @return
     */
    List<ItemCat> findAll();
}
