package com.fjs.cronus.service;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.ResultResource;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.cronus.OcrDocumentDto;
import com.fjs.cronus.dto.cronus.UcUserDTO;
import com.fjs.cronus.dto.ocr.DriverLicenseDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.OcrDriverLicenseMapper;
import com.fjs.cronus.mappers.RContractDocumentMapper;
import com.fjs.cronus.model.OcrDriverLicense;
import com.fjs.cronus.model.OcrDriverVehicle;
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
public class DriverLicenseService {

    @Autowired
    OcrDriverLicenseMapper ocrDriverLicenseMapper;
    @Autowired
    RContractDocumentMapper rContractDocumentMapper;
    @Autowired
    UcService ucService;
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

    public CronusDto editOcrInfo(Integer id){
        CronusDto resultDto = new CronusDto();
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("id",id);
        OcrDriverLicense ocrDriverLicense  = ocrDriverLicenseMapper.findByFeild(paramsMap);
        if (ocrDriverLicense == null){
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        DriverLicenseDTO driverLicenseDTO = new DriverLicenseDTO();
        EntityToDto.copyDtoToDriverLience(ocrDriverLicense,driverLicenseDTO);

        List crm_attach_ids = new ArrayList();
        if (!StringUtils.isEmpty(ocrDriverLicense.getDocumentId())){
            crm_attach_ids.add(ocrDriverLicense.getDocumentId());
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
                ocrDocumentDto.setUrl(rcdocument.getDocument().getDocumentSavepath() + rcdocument.getDocument().getDocumentSavename());
                ocrDocumentDtos.add(ocrDocumentDto);
            }
            driverLicenseDTO.setOcrDocumentDto(ocrDocumentDtos);
        }
        resultDto.setData(driverLicenseDTO);
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        return  resultDto;
    }
    public CronusDto editOcrInfoOK(JSONObject jsonObject, String token){
        CronusDto resultDto = new CronusDto();
        Integer id = jsonObject.getInteger("id");
        String driver_name = jsonObject.getString("driver_name");
        String driver_num  = jsonObject.getString("driver_num");
        String driver_vehicle_type= jsonObject.getString("driver_vehicle_type");
        String driver_start_date= jsonObject.getString("driver_start_date");
        String driver_end_date= jsonObject.getString("driver_end_date");
        if(StringUtils.isEmpty(id)){
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("id",id);
        OcrDriverLicense ocrDriverLicense  = ocrDriverLicenseMapper.findByFeild(paramsMap);
        if (ocrDriverLicense == null){
            throw new CronusException(CronusException.Type.CEM_CUSTOMERIDENTITYINFO_ERROR);
        }
        if (!StringUtils.isEmpty(driver_name)){
            ocrDriverLicense.setDriverName(driver_name);
        }
        if (!StringUtils.isEmpty(driver_num)){
            ocrDriverLicense.setDriverNum(driver_num);
        }
        if (!StringUtils.isEmpty(driver_vehicle_type)){
            ocrDriverLicense.setDriverVehicleType(driver_vehicle_type);
        }
        if (!StringUtils.isEmpty(driver_start_date)){
            ocrDriverLicense.setDriverStartDate(driver_start_date);
        }
        if (!StringUtils.isEmpty(driver_end_date)){
            ocrDriverLicense.setDriverEndDate(driver_end_date);
        }
        Integer user_id = ucService.getUserIdByToken(token);
        UcUserDTO ucUserDTO = ucService.getUserInfoByID(token,user_id);
        if (ucUserDTO == null){
            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
        }
        Date date = new Date();
        ocrDriverLicense.setUpdateTime(date);
        ocrDriverLicense.setUpdateUserName(ucUserDTO.getName());
        ocrDriverLicense.setUpdateUserId(user_id);
        ocrDriverLicense.setStatus(ResultResource.OCRSTATUS);
        ocrDriverLicenseMapper.updateDriverLience(ocrDriverLicense);
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        return  resultDto;


    }

}
