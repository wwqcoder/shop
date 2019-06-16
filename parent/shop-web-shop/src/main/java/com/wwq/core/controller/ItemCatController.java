package com.wwq.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wwq.core.pojo.item.ItemCat;
import com.wwq.core.service.ItemCatService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: Mr.Wang
 * @create: 2019-04-30 09:33
 **/

@RestController
@RequestMapping("itemCat")
public class ItemCatController {

    @Reference
    ItemCatService itemCatService;

    @RequestMapping("/findByParentId")
    public List<ItemCat> findByParentId(Long parentId){
        return itemCatService.findByParentId(parentId);
    }

    @RequestMapping("/findOne")
    public ItemCat findOne(Long id){
        return itemCatService.findOne(id);
    }

    @RequestMapping("/findAll")
    public List<ItemCat> findAll(){
        return itemCatService.findAll();
    }
}
