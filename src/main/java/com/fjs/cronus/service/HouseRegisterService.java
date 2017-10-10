package com.fjs.cronus.service;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.ResultResource;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.cronus.OcrDocumentDto;
import com.fjs.cronus.dto.ocr.DriverVehicleDTO;
import com.fjs.cronus.dto.ocr.HouseRegisterDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.OcrHouseRegistrationMapper;
import com.fjs.cronus.mappers.OcrHouseholdRegisterMapper;
import com.fjs.cronus.mappers.RContractDocumentMapper;
import com.fjs.cronus.model.OcrDriverVehicle;
import com.fjs.cronus.model.OcrHouseRegistration;
import com.fjs.cronus.model.RContractDocument;
import com.fjs.cronus.service.uc.UcService;
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

    @Autowired
    RContractDocumentMapper rContractDocumentMapper;
    @Autowired
    UcService ucService;

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

    public CronusDto editOcrInfo(Integer id){
        CronusDto resultDto = new CronusDto();
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("id",id);
        OcrHouseRegistration ocrHouseRegistration = ocrHouseRegistrationMapper.findByfeild(paramsMap);
        if (ocrHouseRegistration == null){
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        HouseRegisterDTO houseRegisterDTO = new HouseRegisterDTO();
        EntityToDto.copyDtoToHouseReg(ocrHouseRegistration,houseRegisterDTO);
        List crm_attach_ids = new ArrayList();
        if (!StringUtils.isEmpty(ocrHouseRegistration.getDocumentId())){
            crm_attach_ids.add(ocrHouseRegistration.getDocumentId());
        }
        Map<String,Object> requestMap = new HashMap<>();
        requestMap.put("crm_attach_ids",crm_attach_ids);
        List<OcrDocumentDto> ocrDocumentDtos =new ArrayList<>();
        List<RContractDocument> documentList = rContractDocumentMapper.ocrDocument(requestMap);
        if (documentList != null && documentList.size() > 0) {
            for (RContractDocument rcdocument : documentList) {
                OcrDocumentDto ocrDocumentDto = new OcrDocumentDto();
                ocrDocumentDto.setDocument_id(rcdocument.getDocument().getId());
                ocrDocumentDto.setDocument_name(rcdocument.getDocumentName());
                ocrDocumentDto.setDocument_c_name(rcdocument.getDocumentCategory().getDocumentCName());
                ocrDocumentDto.setDocument_c_name_header(rcdocument.getDocumentCategory().getDocumentCNameHeader());
                ocrDocumentDto.setRc_document_id(rcdocument.getId());
                ocrDocumentDto.setDocumentSavename(rcdocument.getDocument().getDocumentSavename());
                ocrDocumentDto.setDocumentSavepath(rcdocument.getDocument().getDocumentSavepath());
                ocrDocumentDto.setUrl(rcdocument.getDocument().getDocumentSavepath() +  rcdocument.getDocument().getDocumentSavename());
                ocrDocumentDtos.add(ocrDocumentDto);
            }
            houseRegisterDTO.setOcrDocumentDto(ocrDocumentDtos);
        }
        resultDto.setData(houseRegisterDTO);
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        return  resultDto;
    }
    public CronusDto editOcrInfoOK(JSONObject jsonObject, String token) {
        CronusDto resultDto = new CronusDto();
        Integer id = jsonObject.getInteger("id");
        String house_ownner = jsonObject.getString("house_ownner");
        String house_address  = jsonObject.getString("house_address");
        String house_purpose= jsonObject.getString("house_purpose");
        String house_usage_term= jsonObject.getString("house_usage_term");
        String house_area= jsonObject.getString("house_area");
        String house_type= jsonObject.getString("house_type");
        String house_completion_date= jsonObject.getString("house_completion_date");
        if(StringUtils.isEmpty(id)){
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("id",id);
        OcrHouseRegistration ocrHouseRegistration = ocrHouseRegistrationMapper.findByfeild(paramsMap);
        if (ocrHouseRegistration == null){
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        if (!StringUtils.isEmpty(house_ownner)){
            ocrHouseRegistration.setHouseOwnner(house_ownner);
        }
        if (!StringUtils.isEmpty(house_address)){
            ocrHouseRegistration.setHouseAddress(house_address);
        }
        if (!StringUtils.isEmpty(house_purpose)){
            ocrHouseRegistration.setHousePurpose(house_purpose);
        }
        if (!StringUtils.isEmpty(house_usage_term)){
            ocrHouseRegistration.setHouseUsageTerm(house_usage_term);
        }
        if (!StringUtils.isEmpty(house_area)){
            ocrHouseRegistration.setHouseArea(house_area);
        }
        if (!StringUtils.isEmpty(house_type)){
            ocrHouseRegistration.setHouseType(house_type);
        }
        if (!StringUtils.isEmpty(house_completion_date)){
            ocrHouseRegistration.setHouseCompletionDate(house_completion_date);
        }
        Integer user_id = ucService.getUserIdByToken(token);
        Date date = new Date();
        ocrHouseRegistration.setLastUpdateTime(date);
        ocrHouseRegistration.setLastUpdateUser(user_id);
        ocrHouseRegistration.setStatus(ResultResource.OCRSTATUS);
        ocrHouseRegistrationMapper.updateHouseRegis(ocrHouseRegistration);
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        return resultDto;
    }
}
