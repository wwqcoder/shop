package com.wwq.core.service;

import com.wwq.core.pojo.entity.PageResult;
import com.wwq.core.pojo.specification.Specification;
import com.wwq.core.pojo.vo.SpecificationVo;

import java.util.List;
import java.util.Map;

public interface SpecificationService  {
    PageResult search(Integer page, Integer rows, Specification specification);

    public void add(SpecificationVo vo);

    public SpecificationVo findOne(Long id);

    void update(SpecificationVo vo);

    void delete(Long[] ids);

    List<Map> selectOptionList();
}
