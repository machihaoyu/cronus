package com.fjs.cronus.mappers;


import com.fjs.cronus.model.CallbackPhoneLog;

import java.util.List;
import java.util.Map;

public interface CallbackPhoneLogMapper {



    List  getCustomerId(Map<String,Object> paramsMap);

   List<CallbackPhoneLog> findByFeild(Map<String,Object> paramsMap);

   void addCallbackPhoneLog(CallbackPhoneLog callbackPhoneLog);
}