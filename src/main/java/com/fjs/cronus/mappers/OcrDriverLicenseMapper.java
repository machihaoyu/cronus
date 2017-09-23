package com.fjs.cronus.mappers;

import com.fjs.cronus.model.OcrDriverLicense;
import com.fjs.cronus.util.MyMapper;

import java.util.Map;


public interface OcrDriverLicenseMapper extends MyMapper<OcrDriverLicense> {

    OcrDriverLicense  findByFeild(Map<String,Object> paramsMap);

    void updateDriverLience(OcrDriverLicense ocrDriverLicense);

    void addDriverLience(OcrDriverLicense ocrDriverLicense);
}