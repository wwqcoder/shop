package com.wwq.core.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.wwq.core.dao.item.ItemCatDao;
import com.wwq.core.pojo.item.ItemCat;
import com.wwq.core.pojo.item.ItemCatQuery;
import com.wwq.core.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Administrator
 */
@Service
@Transactional
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    ItemCatDao itemCatDao;

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public List<ItemCat> findByParentId(Long parentId) {

        ItemCatQuery itemCatQuery = new ItemCatQuery();
        itemCatQuery.createCriteria().andParentIdEqualTo(parentId);

        //每次执行查询的时候，一次性读取缓存进行存储 (因为每次增删改都要执行此方法)
        List<ItemCat> list = findAll();
        for (ItemCat itemCat : list) {
            redisTemplate.boundHashOps("itemCat").put(itemCat.getName(), itemCat.getTypeId());
        }

        System.out.println("更新缓存:商品分类表");



        return itemCatDao.selectByExample(itemCatQuery);
    }

    @Override
    public void add(ItemCat itemCat) {
        itemCatDao.insertSelective(itemCat);
    }

    @Override
    public ItemCat findOne(Long id) {
        return itemCatDao.selectByPrimaryKey(id);
    }

    @Override
    public void update(ItemCat itemCat) {
        itemCatDao.updateByPrimaryKeySelective(itemCat);
    }

    @Override
    public List<ItemCat> findAll() {
        List<ItemCat> itemCatList = itemCatDao.selectByExample(null);
        return itemCatList;
    }
}
