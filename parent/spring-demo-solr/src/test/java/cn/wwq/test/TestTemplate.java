package cn.wwq.test;

import cn.wwq.pojo.Item;
import org.apache.http.annotation.Obsolete;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

/**
 * @author: Mr.Wang
 * @create: 2019-05-05 20:01
 **/

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-solr.xml")
public class TestTemplate {

    @Autowired
    SolrTemplate solrTemplate;

    @Test
    public void Add(){

        LinkedList<Item> list = new LinkedList<>();

        for (int i = 0; i < 50; i++) {

            Item item = new Item();
            item.setId(i+1l);
            item.setBrand("iPhone");
            item.setCategory("手机");
            item.setGoodsId(1l);
            item.setSeller("iphone专卖店");
            item.setTitle("iphone"+i+"S");
            item.setPrice(new BigDecimal(8000+i));
            list.add(item);
        }
        solrTemplate.saveBeans(list);

        solrTemplate.commit();

    }

    @Test
    public void testFindOne(){

        Item item = solrTemplate.getById(1, Item.class);
        System.out.println(item.getTitle());
    }

    @Test
    public void testDelete(){
        solrTemplate.deleteById("1");
        solrTemplate.commit();
    }


    //根据分页查询索引库
    @Test
    public void testPageQuery(){

        Query query = new SimpleQuery("*:*");
        //页码
        query.setOffset(5);
        //每页显示条数
        query.setRows(10);

        ScoredPage<Item> items = solrTemplate.queryForPage(query, Item.class);

        System.out.println("总记录数："+ items.getTotalElements());

        List<Item> content = items.getContent();
        for (Item item : content) {
            System.out.println("title:"+item.getTitle());
            System.out.println("price:"+item.getPrice());
        }

    }

    /**
     * 多条件查询索引库
     */
    @Test
    public void testPageQueryMutil(){

        Query query = new SimpleQuery("*:*");

        Criteria criteria = new Criteria();
        criteria = criteria.or("item_title").contains("2");
        criteria = criteria.or("item_title").contains("5");
        query.addCriteria(criteria);

        ScoredPage<Item> items = solrTemplate.queryForPage(query, Item.class);

        System.out.println("总记录数："+ items.getTotalElements());

        List<Item> content = items.getContent();
        for (Item item : content) {
            System.out.println("title:"+item.getTitle());
            System.out.println("price:"+item.getPrice());
        }


    }

    @Test
    public void testDeleteAll(){
        Query query=new SimpleQuery("*:*");
        solrTemplate.delete(query);
        solrTemplate.commit();
    }


}
