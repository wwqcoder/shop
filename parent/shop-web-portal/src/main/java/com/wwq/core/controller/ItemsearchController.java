package com.wwq.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wwq.core.service.ItemSearchService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author: Mr.Wang
 * @create: 2019-05-06 09:17
 **/
@RestController
@RequestMapping("itemsearch")
public class ItemsearchController {

    @Reference
    ItemSearchService itemsearchService;

    /**
     * 根据关键字查询商品
     * @param searchMap
     * @return
     */
    @RequestMapping("/search")
    public Map<String,Object> search(@RequestBody Map searchMap){
        return itemsearchService.search(searchMap);

    }
}
