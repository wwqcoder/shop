package com.wwq.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.wwq.core.dao.item.ItemDao;
import com.wwq.core.pojo.item.Item;
import com.wwq.core.pojo.item.ItemQuery;
import com.wwq.core.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Mr.Wang
 * @create: 2019-05-06 09:29
 **/
@Service
@Transactional
public class ItemSearchServiceImpl implements ItemSearchService {

    @Autowired
    SolrTemplate solrTemplate;

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public Map<String, Object> search(Map searchMap) {

        String keywords = (String) searchMap.get("keywords");
        searchMap.put("keywords",keywords.replace(" ",""));

        HashMap<String, Object> map = new HashMap<>();
        //1.按关键字查询（高亮显示）
        map.putAll(searchList(searchMap));

        //2.根据关键字查询商品分类
        List categoryList = searchCategoryList(searchMap);
        map.put("categoryList",categoryList);

        //3.查询品牌和规格列表
        if (categoryList.size() > 0 && null != categoryList){
            map.putAll(searchBrandAndSpecList(String.valueOf(categoryList.get(0))));
        }
        return map;
    }

    /**
     * 根据关键字查询列表
     * @param searchMap
     * @return
     */
    private Map searchList(Map searchMap){
        HashMap map = new HashMap();
        HighlightQuery query = new SimpleHighlightQuery();
        //设置高亮的域
        HighlightOptions highlightOptions = new HighlightOptions().addField("item_title");
        highlightOptions.setSimplePrefix("<em style='color:red'>");
        highlightOptions.setSimplePostfix("</em>");
        //设置高亮选项
        query.setHighlightOptions(highlightOptions);

        //关键字查询
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);
        //1.2 按商品分类过滤
        if(!"".equals(searchMap.get("category"))  )	{//如果用户选择了分类
            FilterQuery filterQuery=new SimpleFilterQuery();
            Criteria filterCriteria=new Criteria("item_category").is(searchMap.get("category"));
            filterQuery.addCriteria(filterCriteria);
            query.addFilterQuery(filterQuery);
        }

        //1.3 按品牌过滤
        if(!"".equals(searchMap.get("brand"))  )	{//如果用户选择了品牌
            FilterQuery filterQuery=new SimpleFilterQuery();
            Criteria filterCriteria=new Criteria("item_brand").is(searchMap.get("brand"));
            filterQuery.addCriteria(filterCriteria);
            query.addFilterQuery(filterQuery);
        }

        //1.4 按规格过滤
        if(searchMap.get("spec")!=null){
            Map<String,String> specMap= (Map<String, String>) searchMap.get("spec");
            for(String key :specMap.keySet()){

                FilterQuery filterQuery=new SimpleFilterQuery();
                Criteria filterCriteria=new Criteria("item_spec_"+key).is( specMap.get(key)  );
                filterQuery.addCriteria(filterCriteria);
                query.addFilterQuery(filterQuery);

            }

        }
        //按价格过滤
        if (!"".equals(searchMap.get("price"))){

            String[] price = ((String) searchMap.get("price")).split("-");
            //如果区间起点不等于0
            if (!price[0].equals(0)){
                Criteria filterCriteria = new Criteria("item_price").greaterThanEqual(price[0]);
                FilterQuery filterQuery = new SimpleFilterQuery(filterCriteria);
                query.addFilterQuery(filterQuery);
            }

            //如果区间终点不等于*
            if (!price[1].equals("*")){
                Criteria filterCriteria = new Criteria("item_price").lessThanEqual(price[1]);
                FilterQuery filterQuery = new SimpleFilterQuery(filterCriteria);
                query.addFilterQuery(filterQuery);
            }

        }

        //排序  ASC  DESC
        String sortValue = (String) searchMap.get("sort");
        String sortField = (String) searchMap.get("sortField");
        if (sortValue != null && !sortValue.equals("")){
            if (sortValue.equals("ASC")){
                Sort sort = new Sort(Sort.Direction.ASC, "item_" + sortField);
                query.addSort(sort);

            }

            if (sortValue.equals("DESC")){
                Sort sort = new Sort(Sort.Direction.DESC, "item_" + sortField);
                query.addSort(sort);

            }
        }


        //分页查询
        //提取页码
        Integer pageNo = (Integer) searchMap.get("pageNo");

        if (pageNo == null){
            //默认第一页
            pageNo = 1;
        }

        //每页记录数
        Integer pageSize = (Integer) searchMap.get("pageSize");

        if (pageSize == null){
            //默认第一页
            pageSize = 20;
        }

        //起始索引
        query.setOffset((pageNo - 1) * pageSize);
        query.setRows(pageSize);


        //***********  获取高亮结果集  ***********
        //高亮页对象
        HighlightPage<Item> page = solrTemplate.queryForHighlightPage(query, Item.class);
        //高亮入口集合(每条记录的高亮入口)
        List<HighlightEntry<Item>> entryList = page.getHighlighted();
        for(HighlightEntry<Item> entry:entryList  ){
            //获取高亮列表(高亮域的个数)
            List<HighlightEntry.Highlight> highlightList = entry.getHighlights();

            if(highlightList.size()>0 &&  highlightList.get(0).getSnipplets().size()>0 ){
                Item item = entry.getEntity();
                item.setTitle(highlightList.get(0).getSnipplets().get(0));
            }
        }



        map.put("rows",page.getContent());
        //返回总页数
        map.put("totalPages",page.getTotalPages());
        //返回总记录数
        map.put("total",page.getTotalElements());
        return map;
    }

    /**
     * 查询分类列表
     * @param searchMap
     * @return
     */
    private List searchCategoryList(Map searchMap){
        ArrayList<String> list = new ArrayList<>();

        //按照关键字查询
        Query query = new SimpleQuery();
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);
        //设置分组选项
        GroupOptions groupOptions = new GroupOptions().addGroupByField("item_category");

        query.setGroupOptions(groupOptions);

        //得到分组页
        GroupPage<Item> page = solrTemplate.queryForGroupPage(query, Item.class);
        //根据列得到分组列表
        GroupResult<Item> groupResult = page.getGroupResult("item_category");
        //得到分组列表页
        Page<GroupEntry<Item>> groupEntries = groupResult.getGroupEntries();
        //得到分组入口集合
        List<GroupEntry<Item>> content = groupEntries.getContent();
        for (GroupEntry<Item> entry : content) {
            list.add(entry.getGroupValue());
        }
        return list;
    }

    /**
     * 查询品牌和规格列表
     * @param category
     * @return
     */
    private Map searchBrandAndSpecList(String category){
        HashMap map = new HashMap();

        //获取模板ID
        Long typeId = (Long) redisTemplate.boundHashOps("itemCat").get(category);

        if (null != typeId){
            //根据模板ID查询品牌列表
            List brandList = (List) redisTemplate.boundHashOps("brandList").get(typeId);
            //返回值添加品牌列表
            map.put("brandList",brandList);
            //根据模板ID查询规格列表
            List specList = (List) redisTemplate.boundHashOps("specList").get(typeId);
            map.put("specList", specList);
        }

        return map;

    }

    @Autowired
    ItemDao itemDao;

    @Override
    public List<Item> findItemListByGoodsIdAndStatus(Long id, String status){

        ItemQuery itemQuery = new ItemQuery();
        itemQuery.createCriteria().andGoodsIdEqualTo(id).andStatusEqualTo(status);
        return itemDao.selectByExample(itemQuery);
    }

    /**
     * 导入索引库
     * @param list
     */
    @Override
    public void importList(List<Item> list) {
        solrTemplate.saveBeans(list,1000);
    }

    /**
     * 通过goodsId删除索引库
     * @param goodsId
     */
    @Override
    public void deleteByGoodsId(Long goodsId) {
        Query query = new SimpleQuery();
        Criteria criteria = new Criteria("item_goodsid").is(goodsId);
        query.addCriteria(criteria);
        solrTemplate.delete(query);
        solrTemplate.commit();
    }
}
