package com.wwq.core.service;

import com.wwq.core.pojo.ad.Content;
import com.wwq.core.pojo.entity.PageResult;

import java.util.List;

/**
 * @author: Mr.Wang
 * @create: 2019-05-03 18:19
 **/
public interface ContentService {
    public List<Content> findAll();

    public PageResult findPage(Content content, Integer pageNum, Integer pageSize);

    public void add(Content content);

    public void edit(Content content);

    public Content findOne(Long id);

    public void delAll(Long[] ids);

    List<Content> findByCategoryId(Long categoryId);
}
