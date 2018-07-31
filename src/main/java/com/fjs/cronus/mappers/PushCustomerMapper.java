package com.fjs.cronus.mappers;

import com.fjs.cronus.entity.PushCustomerEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PushCustomerMapper {


    Integer addPushCustomer(PushCustomerEntity pushCustomerEntity);
}
