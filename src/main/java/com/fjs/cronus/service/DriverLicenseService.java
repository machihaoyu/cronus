package com.fjs.cronus.service;

import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.ocr.DriverLicenseDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.OcrDriverLicenseMapper;
import com.fjs.cronus.model.OcrDriverLicense;
import com.fjs.cronus.model.OcrDriverVehicle;
import com.fjs.cronus.util.EntityToDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

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
    public QueryResult getOcrInfoList(Integer create_user_id, String customer_telephone, String customer_name, String status,Integer page, Integer size, String order){
        QueryResult resultDto = new QueryResult();

        //拼装参数
        List<DriverLicenseDTO> resultList = new ArrayList<>();
        Map<String,Object> paramsMap = new HashMap<>();
        if (!StringUtils.isEmpty(create_user_id)) {
            paramsMap.put("create_user_id",create_user_id);
        }
        if (!StringUtils.isEmpty(customer_telephone)) {
            //TODO 加密解密
            paramsMap.put("customer_telephone",customer_telephone);
        }
        if (!StringUtils.isEmpty(customer_name)) {
            paramsMap.put("customer_name",customer_name);
        }
        if (!StringUtils.isEmpty(status)) {
            paramsMap.put("status",status);
        }
        if (!StringUtils.isEmpty(order)) {
            paramsMap.put("order",order);
        }
        //计算分页
        paramsMap.put("start",(page-1) * size);
        paramsMap.put("size",size);
        List<OcrDriverLicense> licenseList = ocrDriverLicenseMapper.getOcrInfoList(paramsMap);
        Integer count = ocrDriverLicenseMapper.getOcrInfoCount(paramsMap);

        if (licenseList != null && licenseList.size() > 0){
            for (OcrDriverLicense ocrDriverLicense : licenseList) {
                DriverLicenseDTO driverLicenseDTO = new DriverLicenseDTO();
                EntityToDto.copyDtoToDriverLience(ocrDriverLicense,driverLicenseDTO);
                resultList.add(driverLicenseDTO);
            }
            resultDto.setTotal(count.toString());
            resultDto.setRows(resultList);
        }
        return  resultDto;
    }
}
