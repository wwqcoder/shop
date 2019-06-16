package com.wwq.core.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wwq.core.dao.good.BrandDao;
import com.wwq.core.dao.good.GoodsDao;
import com.wwq.core.dao.good.GoodsDescDao;
import com.wwq.core.dao.item.ItemCatDao;
import com.wwq.core.dao.item.ItemDao;
import com.wwq.core.dao.seller.SellerDao;
import com.wwq.core.pojo.entity.PageResult;
import com.wwq.core.pojo.good.Brand;
import com.wwq.core.pojo.good.Goods;
import com.wwq.core.pojo.good.GoodsDesc;
import com.wwq.core.pojo.good.GoodsQuery;
import com.wwq.core.pojo.item.Item;
import com.wwq.core.pojo.item.ItemCat;
import com.wwq.core.pojo.item.ItemQuery;
import com.wwq.core.pojo.seller.Seller;
import com.wwq.core.pojo.vo.GoodsVo;
import com.wwq.core.service.GoodsService;
import com.wwq.core.service.ItemPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: Mr.Wang
 * @create: 2019-04-29 14:32
 **/

@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    GoodsDao goodsDao;
    @Autowired
    GoodsDescDao goodsDescDao;
    @Autowired
    ItemDao itemDao;
    @Autowired
    BrandDao brandDao;
    @Autowired
    ItemCatDao itemCatDao;
    @Autowired
    SellerDao sellerDao;

    @Override
    public void add(GoodsVo goodsVo) {

        /**
         *  0 未审核 1 审核通过 2 审核不通过 3 关闭
         *  商品表
         */
        goodsVo.getGoods().setAuditStatus("0");

        goodsDao.insertSelective(goodsVo.getGoods());
        /**
         * 商品详情
         */
        goodsVo.getGoodsDesc().setGoodsId(goodsVo.getGoods().getId());
        goodsDescDao.insertSelective(goodsVo.getGoodsDesc());

        /**
         *  如果启用了规格，再进行添加
         */
        if ("1".equals(goodsVo.getGoods().getIsEnableSpec())){

            /**
             * 商品SKU【库存】
             */
            for (Item item : goodsVo.getItemList()) {
                //标题
                String title = goodsVo.getGoods().getGoodsName();

                Map<String,Object> specMap = JSON.parseObject(item.getSpec());

                for (String key : specMap.keySet()) {
                    title += " "+specMap.get(key);
                }

                item.setTitle(title);
                item.setPrice(goodsVo.getGoods().getPrice());
                setItemValue(goodsVo,item);
                itemDao.insertSelective(item);
            }
        }else {
            Item item = new Item();
            /**
             * 商品SPU+规格描述串作为SKU名称
             */
            item.setTitle(goodsVo.getGoods().getGoodsName());
            /**
             * 价格
             */
            item.setPrice(goodsVo.getGoods().getPrice());
            /**
             * 状态
             */
            item.setStatus("1");
            /**
             * 是否默认
             */
            item.setIsDefault("1");
            /**
             * 库存数量
             */
            item.setNum(99999);
            item.setSpec("{}");
            setItemValue(goodsVo,item);
            itemDao.insertSelective(item);
        }


    }

    @Override
    public PageResult search(Integer page, Integer rows, Goods goods) {
        PageHelper.startPage(page,rows);
        GoodsQuery goodsQuery = new GoodsQuery();
        GoodsQuery.Criteria query = goodsQuery.createCriteria();

        if (null != goods.getSellerId()){
            query.andSellerIdEqualTo(goods.getSellerId());
        }
        //说明商品不删除！！！
        query.andIsDeleteIsNull();

        if (null != goods){
            if (null != goods.getAuditStatus()){
                query.andAuditStatusEqualTo(goods.getAuditStatus());
            }
            if (null != goods.getGoodsName() && !"".equals(goods.getGoodsName().trim())){
                query.andGoodsNameLike("%"+goods.getGoodsName()+"%");
            }
        }
        Page<Goods> pages = (Page<Goods>) goodsDao.selectByExample(goodsQuery);
        return new PageResult(pages.getTotal(),pages.getResult());
    }

    @Override
    public GoodsVo findOne(Long id) {

        GoodsVo goodsVo = new GoodsVo();
        //查询商品对象
        Goods goods = goodsDao.selectByPrimaryKey(id);
        //查询商品详细对象
        GoodsDesc goodsDesc = goodsDescDao.selectByPrimaryKey(id);
        //查询规格
        ItemQuery itemQuery = new ItemQuery();
        itemQuery.createCriteria().andGoodsIdEqualTo(id);
        List<Item> items = itemDao.selectByExample(itemQuery);
        goodsVo.setGoods(goods);
        goodsVo.setGoodsDesc(goodsDesc);
        goodsVo.setItemList(items);
        return goodsVo;
    }

    /**
     * 商品修改
     * @param goodsVo
     */
    @Override
    public void update(GoodsVo goodsVo) {
        //修改商品表
        goodsDao.updateByPrimaryKeySelective(goodsVo.getGoods());
        //修改商品详情表
        goodsDescDao.updateByPrimaryKeySelective(goodsVo.getGoodsDesc());
        //修改库存表
        //先删除
        ItemQuery itemQuery = new ItemQuery();
        itemQuery.createCriteria().andGoodsIdEqualTo(goodsVo.getGoods().getId());
        itemDao.deleteByExample(itemQuery);
        //是否启用了规格
        if ("1".equals(goodsVo.getGoods().getIsEnableSpec())){
            List<Item> itemList = goodsVo.getItemList();
            for (Item item : itemList) {
                //取出规格json
                // 规格 : {"机身内存":"16G","网络":"联通3G"}
                String spec = item.getSpec();
                //转对象
                Map<String,String> map = JSON.parseObject(spec, Map.class);
                String title = goodsVo.getGoods().getGoodsName();
                for (String key : map.keySet()) {
                    title += " "+map.get(key);
                }
                //商品标题
                item.setTitle(title);
                item.setPrice(goodsVo.getGoods().getPrice());
                setItemValue(goodsVo,item);

                itemDao.insertSelective(item);

            }
        }else {
            //不启用状态

            Item item = new Item();
            item.setTitle(goodsVo.getGoods().getGoodsName());
            item.setPrice(new BigDecimal(0));
            item.setSpec("{}");
            setItemValue(goodsVo,item);
            item.setNum(9999);
            item.setStatus("1");
            item.setIsDefault("1");

            itemDao.insertSelective(item);
        }
    }

    @Autowired
    JmsTemplate jmsTemplate;

    @Autowired
    Destination topicPageAndSolrDestination;

    @Override
    public void updateStatus(Long[] ids, String status) {
        Goods goods = new Goods();
        goods.setAuditStatus(status);
        for (Long id : ids) {
            goods.setId(id);
            goodsDao.updateByPrimaryKeySelective(goods);
            //发布订阅模式，发布审核通过的ID
            if ("1".equals(status)){
                jmsTemplate.send(topicPageAndSolrDestination, new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {

                        TextMessage message = session.createTextMessage(String.valueOf(id));
                        return message;
                    }
                });
            }
        }
    }

    @Autowired
    private Destination queueSolrDeleteDestination;

    /**
     * 逻辑删除商品
     * @param ids
     */
    @Override
    public void delete(Long[] ids) {
        Goods goods = new Goods();
        goods.setIsDelete("1");
        for (Long id : ids) {
            goods.setId(id);
            goodsDao.updateByPrimaryKeySelective(goods);
            //通过消息队列传参ID，从而删除索引库中的指定内容

            jmsTemplate.send(queueSolrDeleteDestination, new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    TextMessage message = session.createTextMessage(String.valueOf(id));
                    return message;
                }
            });

        }
    }

    private void setItemValue(GoodsVo goodsVo, Item item) {
        /**
         * 商品SPU编号
         */
        item.setGoodsId(goodsVo.getGoods().getId());
        /**
         * 商家编号
         */
        item.setSeller(goodsVo.getGoods().getSellerId());
        /**
         * 商品分类编号(3级)
         */
        item.setCategoryid(goodsVo.getGoods().getCategory3Id());
        /**
         * 创建日期
         */
        item.setCreateTime(new Date());
        /**
         * 修改日期
         */
        item.setUpdateTime(new Date());
        /**
         * 品牌名称
         */
        Brand brand = brandDao.selectByPrimaryKey(goodsVo.getGoods().getBrandId());
        item.setBrand(brand.getName());
        /**
         * 分类名称
         */
        ItemCat itemCat = itemCatDao.selectByPrimaryKey(goodsVo.getGoods().getCategory3Id());
        item.setCategory(itemCat.getName());
        /**
         * 商家名称
         */
        Seller seller = sellerDao.selectByPrimaryKey(goodsVo.getGoods().getSellerId());
        item.setSeller(seller.getNickName());

        /**
         * 图片地址(取SPU的第一张图片)
         */
        List<Map> imageList = JSON.parseArray(goodsVo.getGoodsDesc().getItemImages(), Map.class);
        if (imageList.size() > 0 && imageList != null){
            item.setImage((String) imageList.get(0).get("url"));
        }
    }
}
