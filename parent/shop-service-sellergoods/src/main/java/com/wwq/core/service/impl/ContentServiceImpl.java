package com.wwq.core.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wwq.core.dao.ad.ContentDao;
import com.wwq.core.pojo.ad.Content;
import com.wwq.core.pojo.ad.ContentQuery;
import com.wwq.core.pojo.entity.PageResult;
import com.wwq.core.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: Mr.Wang
 * @create: 2019-05-03 18:19
 **/
@Service
@Transactional
public class ContentServiceImpl implements ContentService {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    private ContentDao contentDao;

    @Override
    public List<Content> findAll() {
        List<Content> list = contentDao.selectByExample(null);
        return list;
    }

    @Override
    public PageResult findPage(Content content, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<Content> page = (Page<Content>)contentDao.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void add(Content content) {
        contentDao.insertSelective(content);

        //清空缓存
        redisTemplate.boundHashOps("content").delete(content.getCategoryId());
    }

    @Override
    public void edit(Content content) {
        //通过内容ID查询内容
        Content c = contentDao.selectByPrimaryKey(content.getId());

        contentDao.updateByPrimaryKeySelective(content);

        //清空缓存
        redisTemplate.boundHashOps("content").delete(content.getCategoryId());

        if (!c.getCategoryId().equals(content.getCategoryId())){
            //清空缓存
            redisTemplate.boundHashOps("content").delete(c.getCategoryId());
        }
    }

    @Override
    public Content findOne(Long id) {
        Content content = contentDao.selectByPrimaryKey(id);
        return content;
    }

    @Override
    public void delAll(Long[] ids) {
        if(ids != null){
            for(Long id : ids){

                Content content = contentDao.selectByPrimaryKey(id);
                contentDao.deleteByPrimaryKey(id);
                //清空缓存
                redisTemplate.boundHashOps("content").delete(content.getCategoryId());
            }
        }

    }

    @Override
    public List<Content> findByCategoryId(Long categoryId) {

        List<Content> contents = (List<Content>) redisTemplate.boundHashOps("content").get(categoryId);

        if (null == contents){
            ContentQuery query = new ContentQuery();
            query.createCriteria().andCategoryIdEqualTo(categoryId)
                                  .andStatusEqualTo("1");
            query.setOrderByClause("sort_order desc");
            contents = contentDao.selectByExample(query);
            redisTemplate.boundHashOps("content").put(categoryId,contents);
        }
        return contents;

    }

}
