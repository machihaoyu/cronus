package com.fjs.cronus.mappers;

import com.fjs.cronus.model.OcrHouseRegistration;
import com.fjs.cronus.util.MyMapper;

import java.util.Map;


public interface OcrHouseRegistrationMapper extends MyMapper<OcrHouseRegistration> {


    OcrHouseRegistration  findByfeild(Map<String,Object> paramMap);

    void updateHouseRegis(OcrHouseRegistration ocrHouseRegistration);

    void addHouseRegis(OcrHouseRegistration ocrHouseRegistration);
}