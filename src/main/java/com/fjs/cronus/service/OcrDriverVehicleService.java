package com.fjs.cronus.service;

import com.fjs.cronus.dto.ocr.DriverVehicleDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.OcrDriverVehicleMapper;
import com.fjs.cronus.model.OcrDriverVehicle;
import com.fjs.cronus.util.EntityToDto;
import org.omg.CORBA.INTERNAL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by msi on 2017/9/23.
 */
@Service
public class OcrDriverVehicleService {

    @Autowired
    OcrDriverVehicleMapper ocrDriverVehicleMapper;


    @Transactional
    public Integer addOrUpdateDriverVeh(DriverVehicleDTO driverVehicleDTO){
        Date date = new Date();
       if (driverVehicleDTO.getId() != null){
           Map<String,Object> paramsMap  = new HashMap<>();
           paramsMap.put("id",driverVehicleDTO.getId());
           OcrDriverVehicle ocrDriverVehicle = ocrDriverVehicleMapper.findByFeild(paramsMap);
           if (ocrDriverVehicle == null){
               return null;
           }
           //参数转换
           EntityToDto.copyDriverVehToDto(driverVehicleDTO,ocrDriverVehicle);
           ocrDriverVehicle.setLastUpdateTime(date);
           //更新
           ocrDriverVehicleMapper.updateDriverVeh(ocrDriverVehicle);
           Integer id = ocrDriverVehicle.getId();
           if (id == null){
               throw new CronusException(CronusException.Type.CRM_OCRDRIVERLICENCEVEH_ERROR);
           }
           return  id;

       }else {
           OcrDriverVehicle ocrDriverVehicle = new OcrDriverVehicle();
           EntityToDto.copyDriverVehToDto(driverVehicleDTO,ocrDriverVehicle);
           ocrDriverVehicle.setCreateTime(date);
           ocrDriverVehicle.setLastUpdateTime(date);
           ocrDriverVehicle.setStatus("草稿");
           ocrDriverVehicleMapper.addDriverVeh(ocrDriverVehicle);
           Integer id = ocrDriverVehicle.getId();
           if (id == null){
               throw new CronusException(CronusException.Type.CRM_OCRDRIVERLICENCEVEH_ERROR);
           }
           return  id;
       }
    }
}
