package com.fjs.cronus.service;

import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.ocr.DriverVehicleDTO;
import com.fjs.cronus.dto.ocr.HouseRegisterDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.OcrHouseRegistrationMapper;
import com.fjs.cronus.mappers.OcrHouseholdRegisterMapper;
import com.fjs.cronus.model.OcrDriverVehicle;
import com.fjs.cronus.model.OcrHouseRegistration;
import com.fjs.cronus.util.EntityToDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.util.*;

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
    public QueryResult getOcrInfoList(Integer create_user_id, String customer_telephone, String customer_name, String status, Integer page, Integer size, String order){
        QueryResult resultDto = new QueryResult();

        //拼装参数
        List<HouseRegisterDTO> resultList = new ArrayList<>();
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
        List<OcrHouseRegistration> houseRegistrations = ocrHouseRegistrationMapper.getOcrInfoList(paramsMap);
        Integer count = ocrHouseRegistrationMapper.getOcrInfoCount(paramsMap);
        if (houseRegistrations != null && houseRegistrations.size() > 0){
            for (OcrHouseRegistration ocrHouseRegistration : houseRegistrations) {
                HouseRegisterDTO houseRegisterDTO = new HouseRegisterDTO();
                EntityToDto.copyDtoToHouseReg(ocrHouseRegistration,houseRegisterDTO);
                resultList.add(houseRegisterDTO);
            }
            resultDto.setTotal(count.toString());
            resultDto.setRows(resultList);
        }
        return  resultDto;
    }
}
