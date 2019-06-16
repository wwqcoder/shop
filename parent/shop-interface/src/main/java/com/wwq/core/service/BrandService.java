package com.wwq.core.service;

import com.wwq.core.pojo.entity.PageResult;
import com.wwq.core.pojo.good.Brand;

import java.util.List;
import java.util.Map;

public interface BrandService {
    /**
     * 查询全部品牌列表
     * @return
     */
    List<Brand> findAll();

    /**
     * 返回分页列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageResult findPage(Integer pageNum,Integer pageSize);

    /**
     * 新增品牌
      * @param brand
     */
    public void add(Brand brand);

    /**
     * 查询一个品牌
     * @param id
     * @return
     */
    public Brand findOne(Long id);

    /**
     * 修改品牌
     * @param brand
     */
    public void update(Brand brand);

    /**
     * 批量删除品牌
     * @param ids
     */
    public void delete(Long[] ids);

    /**
     * 根据条件查询分页对象
     * @param pageNum
     * @param pageSize
     * @param brand
     * @return
     */
    public PageResult search(Integer pageNum,Integer pageSize,Brand brand);

    /**
     * 品牌下拉框数据
     * @return
     */
    List<Map> selectOptionList();
}
