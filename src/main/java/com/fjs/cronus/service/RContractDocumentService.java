package com.fjs.cronus.service;

import com.fjs.cronus.Common.ResultResource;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.cronus.OcrDocumentDto;
import com.fjs.cronus.mappers.RContractDocumentMapper;
import com.fjs.cronus.model.RContractDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by msi on 2017/10/10.
 */
@Service
public class RContractDocumentService {

    @Autowired
    RContractDocumentMapper rContractDocumentMapper;

    public CronusDto findDocByCustomerId(Integer customerId){
        CronusDto resultDto = new CronusDto();
        Map<String,Object> paramsMap = new HashMap<>();
        //查询
        paramsMap.put("customerId",customerId);
        List<RContractDocument> documentList = rContractDocumentMapper.ocrDocument(paramsMap);
        List<OcrDocumentDto> ocrDocumentDtos = new ArrayList<>();
        if (documentList.size() > 0){
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
            resultDto.setData(ocrDocumentDtos);
            resultDto.setResult(ResultResource.CODE_SUCCESS);
            resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        }
        return  resultDto;
    }

     public CronusDto checkCustomerIsUpload (Integer customerId){
         boolean flag = false;
         CronusDto resultDto = new CronusDto();
         Map<String,Object> paramsMap = new HashMap<>();
         //拼接参数
         if (!StringUtils.isEmpty(customerId)){
             paramsMap.put("customerId",customerId);
         }
         //查询id

         paramsMap.put("identity",ResultResource.INENTITY);
         paramsMap.put("household",ResultResource.HOUSEHOLD);

         Integer count = rContractDocumentMapper.checkCustomerIsUpload(paramsMap);

         if (count != null && count > 0 ){
             flag = true;
         }
         resultDto.setData(flag);
         resultDto.setResult(ResultResource.CODE_SUCCESS);
         resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
         return  resultDto;
     }
}
