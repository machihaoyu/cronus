package com.fjs.cronus.service;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.ResultResource;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.cronus.OcrDocumentDto;
import com.fjs.cronus.dto.ocr.HouseRegisterDTO;
import com.fjs.cronus.dto.ocr.HouseholdRegisterDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.OcrHouseholdRegisterMapper;
import com.fjs.cronus.mappers.RContractDocumentMapper;
import com.fjs.cronus.model.OcrHouseholdRegister;
import com.fjs.cronus.model.RContractDocument;
import com.fjs.cronus.service.uc.UcService;
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

    @Autowired
    RContractDocumentMapper rContractDocumentMapper;
    @Autowired
    UcService ucService;
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
       List crm_attach_ids = new ArrayList();
       if (!StringUtils.isEmpty(ocrHouseholdRegister.getDocumentId())){
           crm_attach_ids.add(ocrHouseholdRegister.getDocumentId());
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
               ocrDocumentDto.setUrl(rcdocument.getDocument().getDocumentSavepath()  + rcdocument.getDocument().getDocumentSavename());
               ocrDocumentDtos.add(ocrDocumentDto);
           }
           householdRegisterDTO.setOcrDocumentDto(ocrDocumentDtos);
       }
       resultDto.setData(householdRegisterDTO);
       resultDto.setResult(ResultResource.CODE_SUCCESS);
       resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
       return  resultDto;
   }

     public CronusDto editOcrInfoOK(JSONObject jsonObject, String token){
         CronusDto resultDto = new CronusDto();
         Integer id = jsonObject.getInteger("id");
         String household_name = jsonObject.getString("household_name");
         String household_sex  = jsonObject.getString("household_sex");
         String household_native_place= jsonObject.getString("household_native_place");
         String household_birthday= jsonObject.getString("household_birthday");
         String household_id_number= jsonObject.getString("household_id_number");
         String household_people= jsonObject.getString("household_people");
         String household_job= jsonObject.getString("household_job");
         String household_merriage= jsonObject.getString("household_merriage");
         String household_education = jsonObject.getString("household_education");
         if(StringUtils.isEmpty(id)){
             throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
         }
         Map<String,Object> paramsMap = new HashMap<>();
         paramsMap.put("id",id);
         OcrHouseholdRegister ocrHouseholdRegister = ocrHouseholdRegisterMapper.findByfeild(paramsMap);
         if (ocrHouseholdRegister == null){
             throw new CronusException(CronusException.Type.CEM_CUSTOMERIDENTITYINFO_ERROR);
         }
         if (!StringUtils.isEmpty(household_name)){
             ocrHouseholdRegister.setHouseholdName(household_name);
         }
         if (!StringUtils.isEmpty(household_sex)){
             ocrHouseholdRegister.setHouseholdSex(household_sex);
         }
         if (!StringUtils.isEmpty(household_native_place)){
             ocrHouseholdRegister.setHouseholdNativePlace(household_native_place);
         }
         if (!StringUtils.isEmpty(household_birthday)){
             ocrHouseholdRegister.setHouseholdBirthday(household_birthday);
         }
         if (!StringUtils.isEmpty(household_id_number)){
             ocrHouseholdRegister.setHouseholdIdNumber(household_id_number);
         }
         if (!StringUtils.isEmpty(household_people)){
             ocrHouseholdRegister.setHouseholdPeople(household_people);
         }
         if (!StringUtils.isEmpty(household_job)){
             ocrHouseholdRegister.setHouseholdJob(household_job);
         }
         if (!StringUtils.isEmpty(household_merriage)){
             ocrHouseholdRegister.setHouseholdMerriage(household_merriage);
         }
         if (!StringUtils.isEmpty(household_education)){
             ocrHouseholdRegister.setHouseholdEducation(household_education);
         }
         //
         Integer user_id = ucService.getUserIdByToken(token);
         Date date = new Date();
         ocrHouseholdRegister.setLastUpdateTime(date);
         ocrHouseholdRegister.setLastUpdateUser(user_id);
         ocrHouseholdRegister.setStatus(ResultResource.OCRSTATUS);
         ocrHouseholdRegisterMapper.updateHousReg(ocrHouseholdRegister);
         resultDto.setResult(ResultResource.CODE_SUCCESS);
         resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
         return  resultDto;
     }
}
