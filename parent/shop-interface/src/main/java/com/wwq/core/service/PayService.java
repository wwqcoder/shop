package com.wwq.core.service;

import java.util.Map;

/**
 * @author: Mr.Wang
 * @create: 2019-05-20 14:33
 **/
public interface PayService {
    Map<String, String> createNative(String name);

    Map<String, String> queryPayStatus(String out_trade_no,String name);
}
