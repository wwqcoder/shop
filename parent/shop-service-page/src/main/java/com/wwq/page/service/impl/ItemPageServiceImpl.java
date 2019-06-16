package com.wwq.page.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.wwq.core.dao.good.GoodsDao;
import com.wwq.core.dao.good.GoodsDescDao;
import com.wwq.core.dao.item.ItemCatDao;
import com.wwq.core.dao.item.ItemDao;
import com.wwq.core.pojo.good.Goods;
import com.wwq.core.pojo.good.GoodsDesc;
import com.wwq.core.pojo.item.Item;
import com.wwq.core.pojo.item.ItemQuery;
import com.wwq.core.service.ItemPageService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import javax.servlet.ServletContext;
import java.io.*;
import java.util.HashMap;
import java.util.List;

/**
 * @author: Mr.Wang
 * @create: 2019-05-09 00:13
 **/
@Service
public class ItemPageServiceImpl implements ItemPageService, ServletContextAware {

    @Autowired
    FreeMarkerConfigurer freeMarkerConfigurer;


    @Autowired
    GoodsDao goodsDao;
    @Autowired
    GoodsDescDao goodsDescDao;
    @Autowired
    ItemDao itemDao;
    @Autowired
    ItemCatDao itemCatDao;

    /**
     * 通过商品ID 生成商品详情页面
     * @param goodsId
     */
    @Override
    public void genItemHtml(Long goodsId) {
        //创建map对象用于封装静态化数据
        HashMap<String, Object> root = new HashMap<>();
        //商品表  SPU
        Goods goods = goodsDao.selectByPrimaryKey(goodsId);
        root.put("goods",goods);
        //商品详情表  大字段
        GoodsDesc goodsDesc = goodsDescDao.selectByPrimaryKey(goodsId);
        root.put("goodsDesc",goodsDesc);
        //商品表结果集  SKU

        ItemQuery itemQuery = new ItemQuery();
        itemQuery.createCriteria().andGoodsIdEqualTo(goodsId).andStatusEqualTo("1");
        List<Item> itemList = itemDao.selectByExample(itemQuery);
        root.put("itemList",itemList);

        //三级分类

        root.put("itemCat1",itemCatDao.selectByPrimaryKey(goods.getCategory1Id()).getName());
        root.put("itemCat2",itemCatDao.selectByPrimaryKey(goods.getCategory2Id()).getName());
        root.put("itemCat3",itemCatDao.selectByPrimaryKey(goods.getCategory3Id()).getName());

        //FreeMarker
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        //静态页面生成的真实路径
        String realPath = getRealPath(goodsId + ".html");
        Writer out = null;

        try {
            Template template = configuration.getTemplate("item.ftl");
            out = new OutputStreamWriter(new FileOutputStream(realPath),"UTF-8");
            template.process(root,out);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (null != out){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    /**
     * 返回真实的路径
     * @param path
     * @return
     */
    private String getRealPath(String path){
        return servletContext.getRealPath(path);
    }

    private ServletContext servletContext;
    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
