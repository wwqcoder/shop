package com.wwq.search.listener;

import com.wwq.core.service.ItemSearchService;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * @author: Mr.Wang
 * @create: 2019-05-10 21:43
 **/
public class ItemDeleteListener implements MessageListener {

    @Autowired
    ItemSearchService itemSearchService;

    @Override
    public void onMessage(Message message) {

        ActiveMQTextMessage amt = (ActiveMQTextMessage) message;

        try {
            String goodsId = amt.getText();
            itemSearchService.deleteByGoodsId(Long.parseLong(goodsId));
        } catch (JMSException e) {
            e.printStackTrace();
        }


    }
}
