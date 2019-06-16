package com.wwq.page.listener;

import com.wwq.core.service.ItemPageService;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * @author: Mr.Wang
 * @create: 2019-05-11 10:01
 **/
public class PageListener implements MessageListener {

    @Autowired
    ItemPageService itemPageService;

    @Override
    public void onMessage(Message message) {

        ActiveMQTextMessage amt = (ActiveMQTextMessage) message;
        try {
            String goodsId = amt.getText();
            itemPageService.genItemHtml(Long.parseLong(goodsId));

        } catch (JMSException e) {
            e.printStackTrace();
        }


    }
}
