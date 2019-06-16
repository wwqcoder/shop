package com.wwq.core.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wwq.core.dao.specification.SpecificationDao;
import com.wwq.core.dao.specification.SpecificationOptionDao;
import com.wwq.core.pojo.entity.PageResult;
import com.wwq.core.pojo.specification.Specification;
import com.wwq.core.pojo.specification.SpecificationOption;
import com.wwq.core.pojo.specification.SpecificationOptionQuery;
import com.wwq.core.pojo.vo.SpecificationVo;
import com.wwq.core.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Service
public class SpecificationServiceImpl implements SpecificationService {

    @Autowired
    private SpecificationDao specificationDao;

    @Autowired
    private SpecificationOptionDao specificationOptionDao;

    @Override
    public PageResult search(Integer page, Integer rows, Specification specification) {
        PageHelper.startPage(page,rows);
        Page<Specification> pages = (Page<Specification>) specificationDao.selectByExample(null);
        return new PageResult(pages.getTotal(),pages.getResult());
    }

    @Override
    public void add(SpecificationVo vo) {
        //规格表
        specificationDao.insertSelective(vo.getSpecification());
        //规格选项表
        for (SpecificationOption specificationOption : vo.getSpecificationOptionList()) {
            //外键
            specificationOption.setSpecId(vo.getSpecification().getId());
            specificationOptionDao.insertSelective(specificationOption);
        }
    }

    @Override
    public SpecificationVo findOne(Long id) {
        SpecificationVo vo = new SpecificationVo();
        vo.setSpecification(specificationDao.selectByPrimaryKey(id));

        SpecificationOptionQuery query = new SpecificationOptionQuery();
        query.createCriteria().andSpecIdEqualTo(id);
        vo.setSpecificationOptionList(specificationOptionDao.selectByExample(query));
        return vo;
    }

    @Override
    public void update(SpecificationVo vo) {
        specificationDao.updateByPrimaryKeySelective(vo.getSpecification());

        SpecificationOptionQuery query = new SpecificationOptionQuery();
        query.createCriteria().andSpecIdEqualTo(vo.getSpecification().getId());
        specificationOptionDao.deleteByExample(query);
        for (SpecificationOption specificationOption : vo.getSpecificationOptionList()) {
                specificationOption.setSpecId(vo.getSpecification().getId());
                specificationOptionDao.insertSelective(specificationOption);
        }
    }

    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            specificationDao.deleteByPrimaryKey(id);

            SpecificationOptionQuery query = new SpecificationOptionQuery();
            query.createCriteria().andSpecIdEqualTo(id);
            specificationOptionDao.deleteByExample(query);
        }
    }

    @Override
    public List<Map> selectOptionList() {
        return specificationDao.selectOptionList();
    }
}
