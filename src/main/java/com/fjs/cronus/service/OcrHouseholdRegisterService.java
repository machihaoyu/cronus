package com.fjs.cronus.service;

import com.fjs.cronus.dto.ocr.HouseholdRegisterDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.OcrHouseholdRegisterMapper;
import com.fjs.cronus.model.OcrHouseholdRegister;
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

}
