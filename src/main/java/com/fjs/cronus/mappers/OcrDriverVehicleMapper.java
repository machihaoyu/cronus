package com.fjs.cronus.mappers;

import com.fjs.cronus.model.OcrDriverVehicle;
import com.fjs.cronus.util.MyMapper;

import java.util.List;
import java.util.Map;

public interface OcrDriverVehicleMapper extends MyMapper<OcrDriverVehicle> {


    OcrDriverVehicle findByFeild(Map<String,Object> parmasMap);

    void updateDriverVeh(OcrDriverVehicle ocrDriverVehicle);

    void addDriverVeh(OcrDriverVehicle ocrDriverVehicle);

    List<OcrDriverVehicle> getOcrInfoList(Map<String,Object> parmasMap);

    Integer  getOcrInfoCount(Map<String,Object> parmasMap);
}