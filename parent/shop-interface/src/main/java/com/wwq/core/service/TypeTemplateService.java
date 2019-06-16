package com.wwq.core.service;

import com.wwq.core.pojo.entity.PageResult;
import com.wwq.core.pojo.template.TypeTemplate;

import java.util.List;
import java.util.Map;

public interface TypeTemplateService {

    /**
     * 根据分页查询类型模板列表
     * @param page
     * @param rows
     * @param typeTemplate
     * @return
     */
    PageResult search(Integer page, Integer rows, TypeTemplate typeTemplate);

    /**
     * 添加模板
     * @param typeTemplate
     */
    void add(TypeTemplate typeTemplate);

    /**
     *
     * 根据ID 查询
     * @param id
     * @return
     */
    TypeTemplate findOne(Long id);

    /**
     * 修改
     * @param typeTemplate
     */
    void update(TypeTemplate typeTemplate);

    /**
     * 通过Id查询规格列表
     * @param id
     * @return
     */
    List<Map> findBySpecList(Long id);

    List<TypeTemplate> findAll();
}
