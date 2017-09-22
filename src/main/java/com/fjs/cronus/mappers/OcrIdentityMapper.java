package com.fjs.cronus.mappers;


import com.fjs.cronus.model.OcrIdentity;
import com.fjs.cronus.util.MyMapper;

import java.util.Map;

public interface OcrIdentityMapper extends MyMapper<OcrIdentity> {

    OcrIdentity findByFeild(Map<String,Object> paramsMap);

    void  updateOcrIdentity(OcrIdentity ocrIdentity);

    void addOcrIdentity(OcrIdentity ocrIdentity);
}