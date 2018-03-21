package com.fjs.cronus.mappers;

import com.fjs.cronus.model.UtmCustomerManger;

import java.util.List;
import java.util.Map;

public interface UtmCustomerMangerMapper {
    int deleteByPrimaryKey(Integer id);

    void insert(UtmCustomerManger record);

    UtmCustomerManger findByField(Map<String,Object> paramsMap);

    int updateByPrimaryKeySelective(UtmCustomerManger record);

    void updateByPrimaryKey(UtmCustomerManger record);

    List<UtmCustomerManger> vipUserManList();

    List<UtmCustomerManger> findListByField(Map<String,Object> paramsMap);

    List<UtmCustomerManger> selectList(Map<String,Object> paramsMap);
}