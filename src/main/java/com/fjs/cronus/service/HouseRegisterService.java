package com.fjs.cronus.service;

import com.fjs.cronus.dto.ocr.HouseRegisterDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.OcrHouseRegistrationMapper;
import com.fjs.cronus.mappers.OcrHouseholdRegisterMapper;
import com.fjs.cronus.model.OcrHouseRegistration;
import com.fjs.cronus.util.EntityToDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by msi on 2017/9/23.
 */
@Service
public class HouseRegisterService {

    @Autowired
    OcrHouseRegistrationMapper ocrHouseRegistrationMapper;



    @Transactional
    public  Integer addOrUpdateHousRegister(HouseRegisterDTO houseRegisterDTO){
        //判断是否有id
        Date date = new Date();
        if (houseRegisterDTO.getId()!=null){
            Map<String,Object> paramsMap = new HashMap<>();
            OcrHouseRegistration ocrHouseRegistration = ocrHouseRegistrationMapper.findByfeild(paramsMap);
            if (ocrHouseRegistration == null){
                return  null;
            }
            EntityToDto.copyHouseRegToDto(houseRegisterDTO,ocrHouseRegistration);
            ocrHouseRegistration.setLastUpdateTime(date);
            ocrHouseRegistrationMapper.updateHouseRegis(ocrHouseRegistration);
            Integer id = ocrHouseRegistration.getId();
            if (id == null){
                throw new CronusException(CronusException.Type.CRM_OCRHOUSEREGISTION_ERROR);
            }
            return  id;
        }else {

            OcrHouseRegistration ocrHouseRegistration = new OcrHouseRegistration();
            EntityToDto.copyHouseRegToDto(houseRegisterDTO,ocrHouseRegistration);
            ocrHouseRegistrationMapper.addHouseRegis(ocrHouseRegistration);
            Integer id = ocrHouseRegistration.getId();
            if (id == null){
                throw new CronusException(CronusException.Type.CRM_OCRHOUSEREGISTION_ERROR);
            }
            return  id;
        }
    }
}
