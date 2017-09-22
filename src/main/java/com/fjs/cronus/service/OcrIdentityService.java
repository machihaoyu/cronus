package com.fjs.cronus.service;

import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.cronus.OcrSaveBaseInfoDTO;
import com.fjs.cronus.dto.ocr.IdCardDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.DocumentCategoryMapper;
import com.fjs.cronus.mappers.OcrIdentityMapper;
import com.fjs.cronus.mappers.RContractDocumentMapper;
import com.fjs.cronus.model.DocumentCategory;
import com.fjs.cronus.model.OcrIdentity;
import com.fjs.cronus.model.RContractDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
}
