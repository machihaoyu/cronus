package com.fjs.cronus.mappers;


import com.fjs.cronus.model.CallbackConfig;
import com.fjs.cronus.util.MyMapper;

import java.util.List;
import java.util.Map;

public interface CallbackConfigMapper extends MyMapper<CallbackConfig> {

    CallbackConfig findByFeild(Map<String,Object> parmasMap);

    void  updateCallbackConfig(CallbackConfig callbackConfig);

    List<CallbackConfig> findAll();

}