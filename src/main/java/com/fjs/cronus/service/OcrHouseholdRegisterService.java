package com.fjs.cronus.service;

import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.ocr.HouseRegisterDTO;
import com.fjs.cronus.dto.ocr.HouseholdRegisterDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.OcrHouseholdRegisterMapper;
import com.fjs.cronus.model.OcrHouseholdRegister;
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
public class OcrHouseholdRegisterService {

    @Autowired
    OcrHouseholdRegisterMapper ocrHouseholdRegisterMapper;

    @Transactional
    public Integer  addOrUpdateHouse(HouseholdRegisterDTO householdRegisterDTO){
        if (householdRegisterDTO.getId() != null){
            //修改数据
            Map<String,Object> paramsMap = new HashMap<>();
            paramsMap.put("id",householdRegisterDTO.getId());
            OcrHouseholdRegister ocrHouseholdRegister = ocrHouseholdRegisterMapper.findByfeild(paramsMap);
            //更新信息
            Date date = new Date();
            EntityToDto.ConyDtoToEntityHOuseReg(householdRegisterDTO,ocrHouseholdRegister);
            ocrHouseholdRegister.setLastUpdateTime(date);
            //开始保存
            ocrHouseholdRegisterMapper.updateHousReg(ocrHouseholdRegister);
            Integer id = ocrHouseholdRegister.getId();
            if (id == null){
                throw new CronusException(CronusException.Type.CRM_OCRIDENTITY_ERROR);
            }
            return  id;
        }else {
            //添加数据
            Date date = new Date();
            OcrHouseholdRegister ocrHouseholdRegister  = new OcrHouseholdRegister();
            EntityToDto.ConyDtoToEntityHOuseReg(householdRegisterDTO,ocrHouseholdRegister);
            ocrHouseholdRegister.setCreateTime(date);
            ocrHouseholdRegister.setLastUpdateTime(date);
            ocrHouseholdRegister.setStatus("草稿");
            ocrHouseholdRegisterMapper.addHouseHold(ocrHouseholdRegister);
            Integer id = ocrHouseholdRegister.getId();
            if (id == null){
                throw new CronusException(CronusException.Type.CRM_OCRIDENTITY_ERROR);
            }
            return  id;
        }
    }
   public QueryResult getOcrInfoList(Integer create_user_id, String customer_telephone, String customer_name, String status,
                                     Integer page, Integer size, String order){

       QueryResult queryResult = new QueryResult();
       //拼装参数
       List<HouseholdRegisterDTO> resultList = new ArrayList<>();
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

       List<OcrHouseholdRegister> ocrHouseholdRegisters = ocrHouseholdRegisterMapper.getOcrInfoList(paramsMap);
       Integer count = ocrHouseholdRegisterMapper.getOcrInfoCount(paramsMap);
       if (ocrHouseholdRegisters !=null && ocrHouseholdRegisters.size() > 0){
           for (OcrHouseholdRegister ocrHouseholdRegister : ocrHouseholdRegisters) {
               HouseholdRegisterDTO houseRegisterDTO = new HouseholdRegisterDTO();
               EntityToDto.EntityHOuseRegToDTo(ocrHouseholdRegister,houseRegisterDTO);
               resultList.add(houseRegisterDTO);
           }
           queryResult.setTotal(count.toString());
           queryResult.setRows(resultList);
       }
       return  queryResult;
   }

   public CronusDto editOcrInfo(Integer id){

       CronusDto resultDto = new CronusDto();
       Map<String,Object> paramsMap = new HashMap<>();
       paramsMap.put("id",id);
       OcrHouseholdRegister ocrHouseholdRegister = ocrHouseholdRegisterMapper.findByfeild(paramsMap);
       //转换Dto
       HouseholdRegisterDTO householdRegisterDTO = new HouseholdRegisterDTO();
       EntityToDto.EntityHOuseRegToDTo(ocrHouseholdRegister,householdRegisterDTO);

       return  resultDto;
   }
}
