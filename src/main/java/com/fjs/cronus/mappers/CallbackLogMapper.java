package com.fjs.cronus.mappers;


import com.fjs.cronus.model.CallbackLog;

import java.util.List;
import java.util.Map;

public interface CallbackLogMapper {


    List<CallbackLog> findByFeild(Map<String,Object> paramsMap);

    void adCallbackLog(Map<String,Object> paramsMap);
}