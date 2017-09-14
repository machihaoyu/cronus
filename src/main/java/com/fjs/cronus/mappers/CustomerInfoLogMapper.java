package com.fjs.cronus.mappers;

import com.fjs.cronus.model.CustomerInfoLog;

public interface CustomerInfoLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomerInfoLog record);

    int insertSelective(CustomerInfoLog record);

    CustomerInfoLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CustomerInfoLog record);

    int updateByPrimaryKeyWithBLOBs(CustomerInfoLog record);

    int updateByPrimaryKey(CustomerInfoLog record);
}