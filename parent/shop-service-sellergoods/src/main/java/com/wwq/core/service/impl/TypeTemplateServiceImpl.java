package com.wwq.core.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wwq.core.dao.specification.SpecificationOptionDao;
import com.wwq.core.dao.template.TypeTemplateDao;
import com.wwq.core.pojo.entity.PageResult;
import com.wwq.core.pojo.specification.SpecificationOption;
import com.wwq.core.pojo.specification.SpecificationOptionQuery;
import com.wwq.core.pojo.template.TypeTemplate;
import com.wwq.core.service.TypeTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;


/**
 * @author Administrator
 */
@Service
public class TypeTemplateServiceImpl implements TypeTemplateService {

    @Autowired
    TypeTemplateDao typeTemplateDao;
    @Autowired
    SpecificationOptionDao specificationOptionDao;

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public PageResult search(Integer page, Integer rows, TypeTemplate typeTemplate) {
        PageHelper.startPage(page,rows);
        PageHelper.orderBy("id desc");
        Page<TypeTemplate> pages = (Page<TypeTemplate>) typeTemplateDao.selectByExample(null);

        saveToRedis();

        return new PageResult(pages.getTotal(),pages.getResult());
    }

    @Override
    public void add(TypeTemplate typeTemplate) {
        typeTemplateDao.insertSelective(typeTemplate);
    }

    @Override
    public TypeTemplate findOne(Long id) {
        return typeTemplateDao.selectByPrimaryKey(id);
    }

    @Override
    public void update(TypeTemplate typeTemplate) {
        typeTemplateDao.updateByPrimaryKeySelective(typeTemplate);
    }

    @Override
    public List<Map> findBySpecList(Long id) {

        /**
         * 查询模板
         */
        TypeTemplate typeTemplate = typeTemplateDao.selectByPrimaryKey(id);

        //json转对象
        List<Map> list = JSON.parseArray(typeTemplate.getSpecIds(),Map.class);

        for (Map map : list) {

            SpecificationOptionQuery query = new SpecificationOptionQuery();
            query.createCriteria().andSpecIdEqualTo(Long.valueOf((Integer) map.get("id")));

            List<SpecificationOption> options = specificationOptionDao.selectByExample(query);

            map.put("options",options);
        }

        return list;
    }

    @Override
    public List<TypeTemplate> findAll() {
        return typeTemplateDao.selectByExample(null);
    }

    /**
     * 将数据存入缓存
     */
    private void saveToRedis(){

        List<TypeTemplate> typeTemplateList = findAll();
        for (TypeTemplate typeTemplate : typeTemplateList) {
            //存储品牌列表
            List<Map> brandList = JSON.parseArray(typeTemplate.getBrandIds(), Map.class);
            redisTemplate.boundHashOps("brandList").put(typeTemplate.getId(),brandList);

            //存储规格列表
            List<Map> specList = findBySpecList(typeTemplate.getId());
            redisTemplate.boundHashOps("specList").put(typeTemplate.getId(),specList);

        }


    }
}
