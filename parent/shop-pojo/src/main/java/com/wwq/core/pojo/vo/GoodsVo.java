package com.wwq.core.pojo.vo;

import com.wwq.core.pojo.good.Goods;
import com.wwq.core.pojo.good.GoodsDesc;
import com.wwq.core.pojo.item.Item;

import java.io.Serializable;
import java.util.List;

/**
 * @author: Mr.Wang
 * @create: 2019-04-29 15:52
 **/
public class GoodsVo implements Serializable {
    /**
        商品SPU
     *
     */
    private Goods goods;
    /**
     * 商品扩展（描述）
     */
    private GoodsDesc goodsDesc;
    /**
     * 商品SKU列表
     */
    private List<Item> itemList;

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public GoodsDesc getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(GoodsDesc goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }
}
