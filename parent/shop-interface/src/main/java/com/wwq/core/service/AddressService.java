package com.wwq.core.service;

import com.wwq.core.pojo.address.Address;

import java.util.List;

/**
 * @author: Mr.Wang
 * @create: 2019-05-18 21:08
 **/
public interface AddressService {
    /**
     * 通过user_id查询地址列表
     * @param name
     * @return
     */
    List<Address> findListByLoginUser(String name);

}
