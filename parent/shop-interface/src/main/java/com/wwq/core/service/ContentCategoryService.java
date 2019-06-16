package com.wwq.core.service;

import com.wwq.core.pojo.ad.ContentCategory;
import com.wwq.core.pojo.entity.PageResult;

import java.util.List;

/**
 * @author: Mr.Wang
 * @create: 2019-05-03 18:08
 **/
public interface ContentCategoryService {
    public List<ContentCategory> findAll();

    public PageResult findPage(ContentCategory contentCategory, Integer pageNum, Integer pageSize);

    public void add(ContentCategory contentCategory);

    public void edit(ContentCategory contentCategory);

    public ContentCategory findOne(Long id);

    public void delAll(Long[] ids);
}
