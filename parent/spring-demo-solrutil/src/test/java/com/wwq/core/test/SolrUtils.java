package com.wwq.core.test;

import com.alibaba.fastjson.JSON;
import com.wwq.core.dao.item.ItemDao;
import com.wwq.core.pojo.item.Item;
import com.wwq.core.pojo.item.ItemQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

/**
 * @author: Mr.Wang
 * @create: 2019-05-05 21:29
 **/

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext-solr.xml",
        "classpath*:spring/applicationContext*.xml"})
public class SolrUtils {

    @Autowired
    SolrTemplate solrTemplate;

    @Autowired
    ItemDao itemDao;


    //导入数据到索引库
    @Test
    public void importSolr(){

        ItemQuery itemQuery = new ItemQuery();
        itemQuery.createCriteria().andStatusEqualTo("1");
        List<Item> items = itemDao.selectByExample(itemQuery);

        if (null != items && items.size() > 0){
            for (Item item : items) {
                //json转成对象
                Map<String,String> specMap = JSON.parseObject(item.getSpec(), Map.class);
                item.setSpecMap(specMap);
            }
            solrTemplate.saveBeans(items);
            solrTemplate.commit();
        }



    }




}
