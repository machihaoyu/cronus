package com.fjs.cronus.mappers;

import com.fjs.cronus.model.AgainAllocateCustomer;

import java.util.List;
import java.util.Map;

/**
 * Created by feng on 2017/10/9.
 */
public interface AgainAllocateCustomerMapper {

    Integer saveStatusByDataId(Map<String, Object> map);

    Integer addAgainAllocateCustomer(AgainAllocateCustomer againAllocateCustomer);

    List<AgainAllocateCustomer> getNonAllocateCustomer();

}
