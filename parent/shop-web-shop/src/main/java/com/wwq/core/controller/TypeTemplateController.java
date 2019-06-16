package com.wwq.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wwq.core.pojo.template.TypeTemplate;
import com.wwq.core.service.TypeTemplateService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author: Mr.Wang
 * @create: 2019-04-30 10:22
 **/

@RestController
@RequestMapping("typeTemplate")
public class TypeTemplateController {

    @Reference
    TypeTemplateService typeTemplateService;


    /**
     * 通过ID查询模板
     * @param id
     * @return
     */
    @RequestMapping("/findOne")
    public TypeTemplate findOne(Long id){
        return typeTemplateService.findOne(id);
    }

    /**
     * 返回规格列表
     * @param id
     * @return
     */
    @RequestMapping("/findBySpecList")
    public List<Map> findBySpecList(Long id){
        return typeTemplateService.findBySpecList(id);
    }
}
