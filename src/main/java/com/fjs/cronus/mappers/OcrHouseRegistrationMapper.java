package com.fjs.cronus.mappers;

import com.fjs.cronus.model.OcrHouseRegistration;
import com.fjs.cronus.util.MyMapper;

import java.util.List;
import java.util.Map;


public interface OcrHouseRegistrationMapper extends MyMapper<OcrHouseRegistration> {


    OcrHouseRegistration  findByfeild(Map<String,Object> paramMap);

    void updateHouseRegis(OcrHouseRegistration ocrHouseRegistration);

    void addHouseRegis(OcrHouseRegistration ocrHouseRegistration);//getOcrInfoList

    List<OcrHouseRegistration> getOcrInfoList(Map<String,Object> paramMap);

    Integer getOcrInfoCount(Map<String,Object> paramMap);
}