package com.fjs.cronus.service;

import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.ocr.DriverLicenseDTO;
import com.fjs.cronus.dto.ocr.DriverVehicleDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.OcrDriverVehicleMapper;
import com.fjs.cronus.model.OcrDriverLicense;
import com.fjs.cronus.model.OcrDriverVehicle;
import com.fjs.cronus.util.EntityToDto;
import org.omg.CORBA.INTERNAL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

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
    public QueryResult getOcrInfoList(Integer create_user_id, String customer_telephone, String customer_name, String status,Integer page, Integer size, String order){
        QueryResult resultDto = new QueryResult();

        //拼装参数
        List<DriverVehicleDTO> resultList = new ArrayList<>();
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
        List<OcrDriverVehicle> ocrDriverVehicles = ocrDriverVehicleMapper.getOcrInfoList(paramsMap);
        Integer count  = ocrDriverVehicleMapper.getOcrInfoCount(paramsMap);
        if (ocrDriverVehicles != null && ocrDriverVehicles.size() > 0){
            for (OcrDriverVehicle ocrDriverVehicle : ocrDriverVehicles) {
                DriverVehicleDTO driverVehicleDTO = new DriverVehicleDTO();
                EntityToDto.copyDtoToEntity(ocrDriverVehicle,driverVehicleDTO);
                resultList.add(driverVehicleDTO);
            }
            resultDto.setRows(resultList);
            resultDto.setTotal(count.toString());
        }
        return  resultDto;
    }
}
