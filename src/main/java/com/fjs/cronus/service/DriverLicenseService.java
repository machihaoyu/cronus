package com.fjs.cronus.service;

import com.fjs.cronus.dto.ocr.DriverLicenseDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.OcrDriverLicenseMapper;
import com.fjs.cronus.model.OcrDriverLicense;
import com.fjs.cronus.util.EntityToDto;
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
public class DriverLicenseService {

    @Autowired
    OcrDriverLicenseMapper ocrDriverLicenseMapper;

    @Transactional
    public Integer  addOrUpdateDriverLicense(DriverLicenseDTO driverLicenseDTO){

        Date date = new Date();
          if (driverLicenseDTO.getId() != null){
              Map<String,Object> paramsMap = new HashMap<>();
              paramsMap.put("id",driverLicenseDTO.getId());
              OcrDriverLicense ocrDriverLicense = ocrDriverLicenseMapper.findByFeild(paramsMap);
              //转换参数
              EntityToDto.copyDriverLienceToDto(driverLicenseDTO,ocrDriverLicense);
              ocrDriverLicense.setUpdateTime(date);
              //开始更新
              ocrDriverLicenseMapper.updateDriverLience(ocrDriverLicense);
              Integer id = ocrDriverLicense.getId();
              if (id == null){
                  throw new CronusException(CronusException.Type.CRM_OCRDRIVERLICENCE_ERROR);
              }
              return  id;
          }else {
              //新增一条数据
              OcrDriverLicense ocrDriverLicense = new OcrDriverLicense();
              EntityToDto.copyDriverLienceToDto(driverLicenseDTO,ocrDriverLicense);
              ocrDriverLicense.setCreateTime(date);
              ocrDriverLicense.setUpdateTime(date);
              ocrDriverLicense.setStatus("草稿");
              //
              ocrDriverLicenseMapper.addDriverLience(ocrDriverLicense);
              Integer id = ocrDriverLicense.getId();
              if (id == null){
                  throw new CronusException(CronusException.Type.CRM_OCRDRIVERLICENCE_ERROR);
              }
              return  id;
          }
    }

}
