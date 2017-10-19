package com.fjs.cronus.service;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.ResultResource;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.cronus.OcrDocumentDto;
import com.fjs.cronus.dto.ocr.DriverVehicleDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.OcrDriverVehicleMapper;
import com.fjs.cronus.mappers.RContractDocumentMapper;
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
public class OcrDriverVehicleService {

    @Autowired
    OcrDriverVehicleMapper ocrDriverVehicleMapper;
    @Autowired
    RContractDocumentMapper rContractDocumentMapper;
    @Autowired
    UcService ucService;
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


    public CronusDto editOcrInfo(Integer id){
        CronusDto resultDto = new CronusDto();
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("id",id);
        OcrDriverVehicle ocrDriverVehicle = ocrDriverVehicleMapper.findByFeild(paramsMap);
        if (ocrDriverVehicle == null){
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        DriverVehicleDTO driverVehicleDTO = new DriverVehicleDTO();
        EntityToDto.copyDtoToEntity(ocrDriverVehicle,driverVehicleDTO);
        List crm_attach_ids = new ArrayList();
        if (!StringUtils.isEmpty(ocrDriverVehicle.getDocumentId())){
            crm_attach_ids.add(ocrDriverVehicle.getDocumentId());
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
            driverVehicleDTO.setOcrDocumentDto(ocrDocumentDtos);
        }
        resultDto.setData(driverVehicleDTO);
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        return  resultDto;
    }

    @Transactional
    public CronusDto editOcrInfoOK(JSONObject jsonObject, String token) {
        CronusDto resultDto = new CronusDto();
        Integer id = jsonObject.getInteger("id");
        String driver_owner = jsonObject.getString("driver_owner");
        String driver_plate_num  = jsonObject.getString("driver_plate_num");
        String driver_vehicle_type= jsonObject.getString("driver_vehicle_type");
        String driver_vin= jsonObject.getString("driver_vin");
        String driver_engine_num= jsonObject.getString("driver_engine_num");
        String driver_register_date= jsonObject.getString("driver_register_date");
        if(StringUtils.isEmpty(id)){
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        validateParams(driver_owner,driver_plate_num,driver_vehicle_type,driver_vin,driver_engine_num,driver_register_date);
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("id",id);
        OcrDriverVehicle ocrDriverVehicle = ocrDriverVehicleMapper.findByFeild(paramsMap);
        if (ocrDriverVehicle == null){
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        if (!StringUtils.isEmpty(driver_owner)){
            ocrDriverVehicle.setDriverOwner(driver_owner);
        }
        if (!StringUtils.isEmpty(driver_plate_num)){
            ocrDriverVehicle.setDriverPlateNum(driver_plate_num);
        }
        if (!StringUtils.isEmpty(driver_vehicle_type)){
            ocrDriverVehicle.setDriverVehicleType(driver_vehicle_type);
        }
        if (!StringUtils.isEmpty(driver_vin)){
            ocrDriverVehicle.setDriverVin(driver_vin);
        }
        if (!StringUtils.isEmpty(driver_engine_num)){
            ocrDriverVehicle.setDriverEngineNum(driver_engine_num);
        }
        if (!StringUtils.isEmpty(driver_register_date)){
            ocrDriverVehicle.setDriverRegisterDate(driver_register_date);
        }
        Integer user_id = ucService.getUserIdByToken(token);
        Date date = new Date();
        ocrDriverVehicle.setLastUpdateTime(date);
        ocrDriverVehicle.setLastUpdateUser(user_id);
        ocrDriverVehicle.setStatus(ResultResource.OCRSTATUS);
        ocrDriverVehicleMapper.updateDriverVeh(ocrDriverVehicle);
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        return  resultDto;
    }

    public void  validateParams(String driver_owner,String driver_plate_num,String driver_vehicle_type, String driver_vin, String driver_engine_num,String driver_register_date){

        if (StringUtils.isEmpty(driver_owner)){
            throw new CronusException(CronusException.Type.CRM_DRIVERVEGHICLEOWNER_ERROR);
        }
        if (StringUtils.isEmpty(driver_plate_num)){
            throw new CronusException(CronusException.Type.CRM_DRIVERVEGHICLEPLATNUM_ERROR);
        }
        if (StringUtils.isEmpty(driver_vehicle_type)){
            throw new CronusException(CronusException.Type.CRM_DRIVERLIEVELICHRTYPE_ERROR);
        }
        if (StringUtils.isEmpty(driver_vin)){
            throw new CronusException(CronusException.Type.CRM_DRIVERVEGHICLEWIN_ERROR);
        }
        if (StringUtils.isEmpty(driver_engine_num)){
            throw new CronusException(CronusException.Type.CRM_DRIVERVEGHICLEENGINENUM_ERROR);
        }
        if (StringUtils.isEmpty(driver_register_date)){
            throw new CronusException(CronusException.Type.CRM_DRIVERVEGHICLEREGIST_ERROR);
        }
    }
}
