package com.fjs.cronus.service;

import com.fjs.cronus.Common.ResultResource;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.cronus.OcrDocumentDto;
import com.fjs.cronus.dto.cronus.OcrIDdentityDTO;
import com.fjs.cronus.dto.cronus.OcrSaveBaseInfoDTO;
import com.fjs.cronus.dto.ocr.IdCardDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.DocumentCategoryMapper;
import com.fjs.cronus.mappers.DocumentMapper;
import com.fjs.cronus.mappers.OcrIdentityMapper;
import com.fjs.cronus.mappers.RContractDocumentMapper;
import com.fjs.cronus.model.Document;
import com.fjs.cronus.model.DocumentCategory;
import com.fjs.cronus.model.OcrIdentity;
import com.fjs.cronus.model.RContractDocument;
import com.fjs.cronus.util.EntityToDto;
import org.omg.CORBA.INTERNAL;
import org.springframework.beans.factory.annotation.Autowired;
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
            DocumentCategory documentCategory = documentCategoryMapper.selectByPrimaryKey(idCardDTO.getCategory());
            String document_c_name  = documentCategory.getDocumentCName();
            if (idCardDTO.getSide() != null && "face".equals(idCardDTO.getSide())){
                  name = document_c_name.replace("(正)","反");
            }else {
                  name = document_c_name.replace("(反)","正");
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
                identity.setIsDeleted(0);
                //开始插入数据
                ocrIdentityMapper.addOcrIdentity(identity);
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
       /* List<Document> documentList = documentMapper.ocrDocument(requestMap);
        List<OcrDocumentDto> ocrDocumentDtos = new ArrayList<>();
        if (documentList != null && documentList.size() > 0){
           for (Document document : documentList){
               OcrDocumentDto ocrDocumentDto = new OcrDocumentDto();
               ocrDocumentDto.setDocument_id(document.getId());
               ocrDocumentDto.setDocument_name(document.getrContractDocument().getDocumentName());
               ocrDocumentDto.setDocument_c_name(document.getDocumentCategory().getDocumentCName());
               ocrDocumentDto.setDocument_c_name_header(document.getDocumentCategory().getDocumentCNameHeader());
               ocrDocumentDto.setRc_document_id(document.getrContractDocument().getId());
               ocrDocumentDtos.add(ocrDocumentDto);
           }
           dto.setOcrDocumentDto(ocrDocumentDtos);
        }*/
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        resultDto.setData(dto);
        return  resultDto;
    }
}
