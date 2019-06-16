package com.wwq.core.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.wwq.core.dao.address.AddressDao;
import com.wwq.core.pojo.address.Address;
import com.wwq.core.pojo.address.AddressQuery;
import com.wwq.core.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author: Mr.Wang
 * @create: 2019-05-18 21:09
 **/

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    AddressDao addressDao;

    /**
     * 通过user_id查询地址列表
     * @param name
     * @return
     */
    @Override
    public List<Address> findListByLoginUser(String name) {

        AddressQuery addressQuery = new AddressQuery();
        addressQuery.createCriteria().andUserIdEqualTo(name);
        return addressDao.selectByExample(addressQuery);
    }
}
