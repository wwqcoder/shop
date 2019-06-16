package com.wwq.search.listener;

import com.wwq.core.pojo.item.Item;
import com.wwq.core.service.ItemSearchService;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.List;

/**
 * @author: Mr.Wang
 * @create: 2019-05-10 21:42
 **/
public class ItemSearchListener implements MessageListener {

    @Autowired
    ItemSearchService itemSearchService;

    @Override
    public void onMessage(Message message) {

        ActiveMQTextMessage amt = (ActiveMQTextMessage)message;
        try {
            String text = amt.getText();
            List<Item> list = itemSearchService.findItemListByGoodsIdAndStatus(Long.parseLong(text), "1");
            //导入索引库
            itemSearchService.importList(list);

        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
