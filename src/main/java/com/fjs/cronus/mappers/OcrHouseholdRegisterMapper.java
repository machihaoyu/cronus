package com.fjs.cronus.mappers;

import com.fjs.cronus.model.OcrHouseholdRegister;
import com.fjs.cronus.util.MyMapper;
import jdk.nashorn.internal.runtime.PrototypeObject;

import java.util.List;
import java.util.Map;


public interface OcrHouseholdRegisterMapper extends MyMapper<OcrHouseholdRegister> {

    OcrHouseholdRegister findByfeild(Map<String,Object> paramsMap);

    void updateHousReg(OcrHouseholdRegister ocrHouseholdRegister);

    void addHouseHold(OcrHouseholdRegister ocrHouseholdRegister);

    List<OcrHouseholdRegister> getOcrInfoList(Map<String,Object> paramsMap);

    Integer getOcrInfoCount(Map<String,Object> paramsMap);

}