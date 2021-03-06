package com.fjs.cronus.service;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.ResultResource;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.QueryResult;

import com.fjs.cronus.dto.cronus.OcrDocumentDto;
import com.fjs.cronus.dto.cronus.OcrIDdentityDTO;
import com.fjs.cronus.dto.ocr.IdCardDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.*;


import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.model.DocumentCategory;
import com.fjs.cronus.model.OcrIdentity;
import com.fjs.cronus.model.RContractDocument;
import com.fjs.cronus.service.uc.UcService;
import com.fjs.cronus.util.EntityToDto;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Created by msi on 2017/9/22.
 */
@Service
public class OcrIdentityService {
    @Autowired
    OcrIdentityMapper ocrIdentityMapper;
    @Autowired
    RContractDocumentMapper rContractDocumentMapper;
    @Autowired
    DocumentCategoryMapper documentCategoryMapper;
    @Autowired
    DocumentMapper documentMapper;
    @Autowired
    UcService ucService;
    @Autowired
    CustomerInfoMapper customerInfoMapper;
    @Autowired
    CustomerInfoService customerInfoService;

    @Value("${ftp.viewUrl}")
    private String viewUrl;
    @Transactional
    public Integer addOrUpdateOcrInden(IdCardDTO idCardDTO){
        //判断是更新还是插入数据
        Map paramsMap = new HashMap();
        if (idCardDTO.getId() != null){
            paramsMap.put("id",idCardDTO.getId());
            OcrIdentity ocrIdentity = ocrIdentityMapper.findByFeild(paramsMap);
            //判断身份证的正反面
            if (idCardDTO.getSide() != null && "face".equals(idCardDTO.getSide())){
                //更新正面信息
                ocrIdentity.setCardName(idCardDTO.getCard_name());
                ocrIdentity.setCardSex(idCardDTO.getCard_sex());
                ocrIdentity.setCardNation(idCardDTO.getCard_nation());
                ocrIdentity.setCardBirth(idCardDTO.getCard_birth());
                ocrIdentity.setCardAddress(idCardDTO.getCard_address());
                ocrIdentity.setCardNum(idCardDTO.getCard_num());
                ocrIdentity.setCrmAttachFaceId(idCardDTO.getCrm_attach_id().toString());
                Date date = new Date();
                ocrIdentity.setLastUpdateTime(date);
                ocrIdentity.setLastUpdateUser(Integer.parseInt(idCardDTO.getUpdate_user_id().toString()));
                //TODO 更新客户身份证
                if (idCardDTO.getCard_num() != null){
                   try {
                       CustomerInfo customerInfo = customerInfoService.findCustomerById(Integer.valueOf(idCardDTO.getCustomer_id().toString()));
                       customerInfo.setIdCard(idCardDTO.getCard_num());
                       customerInfoMapper.updateCustomer(customerInfo);
                   }catch (Exception e){
                       e.printStackTrace();
                   }
                }
            }else {
                //传入的身份证是反面信息的话 只更新反面信息
                ocrIdentity.setCardSignOrg(idCardDTO.getCard_sign_org());
                ocrIdentity.setCardValidStart(idCardDTO.getCard_valid_start());
                ocrIdentity.setCardValidEnd(idCardDTO.getCard_valid_end());
                ocrIdentity.setCrmAttachBackId(idCardDTO.getCrm_attach_id().toString());
                Date date = new Date();
                ocrIdentity.setLastUpdateTime(date);
                ocrIdentity.setLastUpdateUser(Integer.parseInt(idCardDTO.getUpdate_user_id().toString()));
            }
            //开始更新数据
            ocrIdentityMapper.updateOcrIdentity(ocrIdentity);
            Integer id = ocrIdentity.getId();
            if (id == null){
                throw new CronusException(CronusException.Type.CRM_OCRIDENTITY_ERROR);
            }
            return  id;
        }else {
            //CRM PC端的附件上传添加数据插入一条数据
            //定位
            //创建一条新型信息
            OcrIdentity identity = new OcrIdentity();
            String name = null;
            DocumentCategory documentCategory = documentCategoryMapper.selectByKey(idCardDTO.getCategory());
            String document_c_name  = documentCategory.getDocumentCName();
            if (idCardDTO.getSide() != null && "face".equals(idCardDTO.getSide())){
                  name = document_c_name.replace("(正)","(反)");
            }else {
                  name = document_c_name.replace("(反)","(正)");
            }
            //根据名称查询到附件类型
            Map<String,Object> map = new HashMap<>();
            if (name == null){
                throw new CronusException(CronusException.Type.CRM_OCRDOCUMENTCAGORY_ERROR);
            }
            map.put("document_c_name",name);
            DocumentCategory docCategory = documentCategoryMapper.findByFeild(map);
            if (idCardDTO.getSide() != null && "face".equals(idCardDTO.getSide())){
                identity.setDocumentCategoryIds(documentCategory.getId() + "," + docCategory.getId());
                identity.setCrmAttachFaceId(idCardDTO.getCrm_attach_id().toString());
            }else {
                identity.setDocumentCategoryIds(docCategory.getId() + "," + documentCategory.getId());
                identity.setCrmAttachBackId(idCardDTO.getCrm_attach_id().toString());
            }
            //查询业务员和客户之前是否有关联
            Map<String,Object> reviewMap = new HashMap<>();
            reviewMap.put("customer_id",idCardDTO.getCustomer_id());
            reviewMap.put("create_user_id",idCardDTO.getCreate_user_id());
            reviewMap.put("document_category_ids",documentCategory.getId());
            reviewMap.put("order","create_time");
            OcrIdentity ocrIdentity2 = ocrIdentityMapper.findByFeild(reviewMap);
            if (ocrIdentity2 !=null){
            //更新此信息
                if (idCardDTO.getSide() != null && "face".equals(idCardDTO.getSide())){
                    //更新正面信息
                    ocrIdentity2.setCardName(idCardDTO.getCard_name());
                    ocrIdentity2.setCardSex(idCardDTO.getCard_sex());
                    ocrIdentity2.setCardNation(idCardDTO.getCard_nation());
                    ocrIdentity2.setCardBirth(idCardDTO.getCard_birth());
                    ocrIdentity2.setCardAddress(idCardDTO.getCard_address());
                    ocrIdentity2.setCardNum(idCardDTO.getCard_num());
                    ocrIdentity2.setCrmAttachFaceId(idCardDTO.getCrm_attach_id().toString());
                    Date date = new Date();
                    ocrIdentity2.setLastUpdateTime(date);
                    ocrIdentity2.setLastUpdateUser(Integer.parseInt(idCardDTO.getUpdate_user_id().toString()));
                    ocrIdentity2.setDocumentCategoryIds(documentCategory.getId() + "," + docCategory.getId());
                    //同时更新
                    if (idCardDTO.getCard_num() != null){
                        try {
                            CustomerInfo customerInfo = customerInfoService.findCustomerById(Integer.valueOf(idCardDTO.getCustomer_id().toString()));
                            customerInfo.setIdCard(idCardDTO.getCard_num());
                            customerInfoMapper.updateCustomer(customerInfo);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }else {
                    //传入的身份证是反面信息的话 只更新反面信息
                    ocrIdentity2.setCardSignOrg(idCardDTO.getCard_sign_org());
                    ocrIdentity2.setCardValidStart(idCardDTO.getCard_valid_start());
                    ocrIdentity2.setCardValidEnd(idCardDTO.getCard_valid_end());
                    ocrIdentity2.setCrmAttachBackId(idCardDTO.getCrm_attach_id().toString());
                    Date date = new Date();
                    ocrIdentity2.setLastUpdateTime(date);
                    ocrIdentity2.setLastUpdateUser(Integer.parseInt(idCardDTO.getUpdate_user_id().toString()));
                    ocrIdentity2.setDocumentCategoryIds(docCategory.getId() + "," + documentCategory.getId());
                }
                ocrIdentityMapper.updateOcrIdentity(ocrIdentity2);
                Integer id = ocrIdentity2.getId();
                if (id == null){
                    throw new CronusException(CronusException.Type.CRM_OCRIDENTITY_ERROR);
                }
                return  id;
            }else {
                  //插入一条新数据
                identity.setCustomerId(Integer.parseInt(idCardDTO.getCustomer_id().toString()));
                identity.setCustomerName(idCardDTO.getCustomer_name());
                //TODO 手机号加密
                identity.setCustomerTelephone(idCardDTO.getCustomer_telephone());
                identity.setCardName(idCardDTO.getCard_name());
                identity.setCardSex(idCardDTO.getCard_sex());
                identity.setCardNation(idCardDTO.getCard_nation());
                identity.setCardBirth(idCardDTO.getCard_birth());
                identity.setCardAddress(idCardDTO.getCard_address());
                identity.setCardNum(idCardDTO.getCard_num());
                identity.setCardSignOrg(idCardDTO.getCard_sign_org());
                identity.setCardValidStart(idCardDTO.getCard_valid_start());
                identity.setCardValidEnd(idCardDTO.getCard_valid_end());
                identity.setStatus(identity.getStatus());
                Date date = new Date();
                identity.setCreateTime(date);
                identity.setCreateUser(Integer.parseInt(idCardDTO.getCreate_user_id().toString()));
                identity.setLastUpdateTime(date);
                identity.setLastUpdateUser(Integer.parseInt(idCardDTO.getUpdate_user_id().toString()));
                identity.setStatus("草稿");
                identity.setIsDeleted(0);
                //开始插入数据
                ocrIdentityMapper.addOcrIdentity(identity);
                if (idCardDTO.getCard_num() != null){
                    try {
                        CustomerInfo customerInfo = customerInfoService.findCustomerById(Integer.valueOf(idCardDTO.getCustomer_id().toString()));
                        customerInfo.setIdCard(idCardDTO.getCard_num());
                        customerInfoMapper.updateCustomer(customerInfo);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                Integer id  = identity.getId();
                if (id == null){
                    throw new CronusException(CronusException.Type.CRM_OCRIDENTITY_ERROR);
                }
                return  id;
            }
        }
    }

    public QueryResult getOcrInfoList(Integer create_user_id, String customer_telephone, String customer_name, String status,
                                      Integer page, Integer size, String order){

        QueryResult resultDto  = new QueryResult();
        //拼装参数
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
        List<OcrIdentity> ocrInfoList = ocrIdentityMapper.getOcrInfoList(paramsMap);
        Integer count = ocrIdentityMapper.getOcrInfoListCount(paramsMap);
        //转换Dto
        List<OcrIDdentityDTO> resultList = new ArrayList<>();
        if (ocrInfoList != null && ocrInfoList.size() > 0){
            for ( OcrIdentity ocrIdentity :ocrInfoList ) {
                OcrIDdentityDTO idCardDTO = new OcrIDdentityDTO();
                EntityToDto.coptIdCardEntityToDto(ocrIdentity,idCardDTO);
                resultList.add(idCardDTO);
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
        OcrIdentity ocrIdentity = ocrIdentityMapper.findByFeild(paramsMap);
        OcrIDdentityDTO dto = new OcrIDdentityDTO();
        EntityToDto.coptIdCardEntityToDto(ocrIdentity,dto);
        //查询附件信息
        List crm_attach_ids = new ArrayList();
        if (!StringUtils.isEmpty(ocrIdentity.getCrmAttachFaceId() )&& !StringUtils.isEmpty(ocrIdentity.getCrmAttachBackId())){
            crm_attach_ids.add(ocrIdentity.getCrmAttachFaceId());
            crm_attach_ids.add(ocrIdentity.getCrmAttachBackId());
        }else if (!StringUtils.isEmpty(ocrIdentity.getCrmAttachFaceId() )&& StringUtils.isEmpty(ocrIdentity.getCrmAttachBackId())){
            crm_attach_ids.add(ocrIdentity.getCrmAttachFaceId());
        }else if (StringUtils.isEmpty(ocrIdentity.getCrmAttachFaceId() )&& !StringUtils.isEmpty(ocrIdentity.getCrmAttachBackId())){
            crm_attach_ids.add(ocrIdentity.getCrmAttachBackId());
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
                ocrDocumentDto.setDocumentSavepath(ResultResource.DOWNLOADFOOTPATH + rcdocument.getDocument().getDocumentSavepath());
                ocrDocumentDto.setUrl(viewUrl + rcdocument.getDocument().getDocumentSavepath() + rcdocument.getDocument().getDocumentSavename());
                ocrDocumentDtos.add(ocrDocumentDto);
            }
            dto.setOcrDocumentDto(ocrDocumentDtos);
        }
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        resultDto.setData(dto);
        return  resultDto;
    }

    @Transactional
    public CronusDto editOcrInfoOK(JSONObject jsonObject,String token){
        CronusDto resultDto = new CronusDto();
         Integer id = jsonObject.getInteger("id");
         String card_name = jsonObject.getString("card_name");
         String card_sex  = jsonObject.getString("card_sex");
         String card_nation= jsonObject.getString("card_nation");
         String card_birth= jsonObject.getString("card_birth");
         String card_address= jsonObject.getString("card_address");
         String card_num= jsonObject.getString("card_num");
         String card_sign_org= jsonObject.getString("card_sign_org");
         String card_valid_start= jsonObject.getString("card_valid_start");
         String card_valid_end = jsonObject.getString("card_valid_end");
         if(StringUtils.isEmpty(id)){
             throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
         }
        vallidataParams(card_name,card_sex,card_nation,card_birth,card_address,card_num,card_sign_org);
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("id",id);
        OcrIdentity ocrIdentity = ocrIdentityMapper.findByFeild(paramsMap);
        if (ocrIdentity == null){
            throw new CronusException(CronusException.Type.CEM_CUSTOMERIDENTITYINFO_ERROR);
        }
        //开始更新
        Integer user_id = ucService.getUserIdByToken(token);
        Date date = new Date();
        if (!StringUtils.isEmpty(card_name)){
            ocrIdentity.setCardName(card_name);
        }
        if (!StringUtils.isEmpty(card_sex)){
            ocrIdentity.setCardSex(card_sex);
        }
        if (!StringUtils.isEmpty(card_nation)){
            ocrIdentity.setCardNation(card_nation);
        }
        if (!StringUtils.isEmpty(card_birth)){
            ocrIdentity.setCardBirth(card_birth);
        }
        if (!StringUtils.isEmpty(card_address)){
            ocrIdentity.setCardAddress(card_address);
        }
        if (!StringUtils.isEmpty(card_num)){
            ocrIdentity.setCardNum(card_num);
        }
        if (!StringUtils.isEmpty(card_sign_org)){
            ocrIdentity.setCardSignOrg(card_sign_org);
        }
        if (!StringUtils.isEmpty(card_valid_start)){
            ocrIdentity.setCardValidStart(card_valid_start);
        }
        if (!StringUtils.isEmpty(card_valid_end)){
            ocrIdentity.setCardValidEnd(card_valid_end);
        }
        ocrIdentity.setStatus(ResultResource.OCRSTATUS);
        ocrIdentity.setLastUpdateUser(user_id);
        ocrIdentity.setLastUpdateTime(date);
        ocrIdentityMapper.updateOcrIdentity(ocrIdentity);
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
       return resultDto;
    }


    public void vallidataParams(String card_name,String card_sex,String card_nation,String card_birth,String card_address,String card_num,String card_sign_org){

        if (StringUtils.isEmpty(card_name)){
            throw new CronusException(CronusException.Type.CRM_CUSTOMERINENTITYNAME_ERROR);
        }
        if (StringUtils.isEmpty(card_sex)){
            throw new CronusException(CronusException.Type.CRM_CUSTOMERINENTITYSEX_ERROR);
        }
        if (StringUtils.isEmpty(card_nation)){
            throw new CronusException(CronusException.Type.CRM_CUSTOMERINENTITYNATION_ERROR);
        }
        if (StringUtils.isEmpty(card_birth)){
            throw new CronusException(CronusException.Type.CRM_CUSTOMERINENTITYBIRTH_ERROR);
        }
        if (StringUtils.isEmpty(card_address)){
            throw new CronusException(CronusException.Type.CRM_CUSTOMERINENTITYADDRESS_ERROR);
        }
        if (StringUtils.isEmpty(card_num)){
            throw new CronusException(CronusException.Type.CRM_CUSTOMERINENTITYNUMBER_ERROR);
        }
        if (StringUtils.isEmpty(card_sign_org)){
            throw new CronusException(CronusException.Type.CRM_CUSTOMERINENTITYSIGNORG_ERROR);
        }

    }
}
