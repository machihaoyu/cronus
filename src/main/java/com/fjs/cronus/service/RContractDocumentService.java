package com.fjs.cronus.service;

import com.fjs.cronus.Common.ResultResource;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.cronus.CustomerDTO;
import com.fjs.cronus.dto.cronus.DocumentListBase64DTO;
import com.fjs.cronus.dto.cronus.OcrDocumentDto;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.DocumentMapper;
import com.fjs.cronus.mappers.RContractDocumentMapper;
import com.fjs.cronus.model.Document;
import com.fjs.cronus.model.RContractDocument;
import com.fjs.cronus.util.FileBase64ConvertUitl;
import com.fjs.cronus.util.FtpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import sun.misc.BASE64Encoder;

import javax.print.Doc;
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
    @Value("${ftp.viewUrl}")
    private String viewUrl;
    @Value("${ftp.address}")
    private String FTP_ADDRESS;
    @Value("${ftp.port}")
    private Integer FTP_PORT;
    @Value("${ftp.username}")
    private String FTP_USERNAME;
    @Value("${ftp.password}")
    private String FTP_PASSWORD;
    @Value("${ftp.baseUrl}")
    private String FTP_BASE_PATH;
    @Value("${ftp.basePath}")
    private String IMAGE_BASE_URL;
    private static String endpoint;

    private static String accessKeyId;

    private static String accessKeySecret;

    private static String bucketName;

    private static String aliyunOssUrl;

    @Value("${aliyun.oss.endpoint}")
    public void setEndpoint(String endpoint) {
        RContractDocumentService.endpoint = endpoint;
    }

    @Value("${aliyun.oss.accessKeyId}")
    public void setAccessKeyId(String accessKeyId) {
        RContractDocumentService.accessKeyId = accessKeyId;
    }

    @Value("${aliyun.oss.accessKeySecret}")
    public void setAccessKeySecret(String accessKeySecret) {
        RContractDocumentService.accessKeySecret = accessKeySecret;
    }

    @Value("${aliyun.oss.bucketName}")
    public void setBucketName(String bucketName) {
        RContractDocumentService.bucketName = bucketName;
    }

    @Value("${aliyun.oss.url}")
    public void setAliyunOssUrl(String aliyunOssUrl) {
        RContractDocumentService.aliyunOssUrl = aliyunOssUrl;
    }


    @Autowired
    DocumentMapper documentMapper;
    @Autowired
    CustomerInfoService customerInfoService;
    @Autowired
    DocumentService documentService;
    public CronusDto findDocByCustomerId(Integer customerId){
        CronusDto resultDto = new CronusDto();
        Map<String,Object> paramsMap = new HashMap<>();
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
                ocrDocumentDto.setFlag(rcdocument.getDocument().getIsFlag());
                ocrDocumentDto.setDocumentSavepath(ResultResource.DOWNLOADFOOTPATH + rcdocument.getDocument().getDocumentSavepath());
                ocrDocumentDto.setUrl(aliyunOssUrl + ResultResource.DOWNLOADFOOTPATH +rcdocument.getDocument().getDocumentSavepath() + rcdocument.getDocument().getDocumentSavename());
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

    public boolean confirmDocument(Integer document_id){
        boolean flag = false;
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("document_id",document_id);

        Document document = documentMapper.findByFeild(paramsMap);
        if (document == null){
            throw new CronusException(CronusException.Type.CRM_CALLBACKCUSTOMER_ERROR);
        }
        //开始更新
        document.setIsFlag(2);//修改为已确认状态
        documentMapper.update(document);
        flag = true;
        return  flag;
    }

    /**
     * 获取单张附件的base64、文档id
     * @param telephone 登录人手机号
     * @param catagoryId 附件类型id
     * @return
     */
    public Map<String,String> getListBase64(String telephone, Integer catagoryId){
        CronusDto<CustomerDTO> resultDto = customerInfoService.fingByphone(telephone);
        CustomerDTO customerDTO = resultDto.getData();
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("customerId",customerDTO.getId());
        paramsMap.put("catagoryId",catagoryId);
        RContractDocument rcdocument = rContractDocumentMapper.ocrDocumentToClient(paramsMap);
        Integer confirm = 1;
        Map<String,String> map = new HashMap<>();
        map.put("documentId",rcdocument.getDocumentId().toString());
        map.put("url", aliyunOssUrl + ResultResource.DOWNLOADFOOTPATH +rcdocument.getDocument().getDocumentSavepath() + rcdocument.getDocument().getDocumentSavename());
        if (confirm.equals(rcdocument.getDocument().getIsFlag()))
            map.put("confirm", "0");
        else
            map.put("confirm", "1");
        return map;
    }


    /**
     * 获取多张附件的base64、文档id
     * @param telephone
     * @param catagoryId
     * @return
     */
    public List<Map<String,String>> getBaseList(String telephone, Integer catagoryId){
        CronusDto<CustomerDTO> resultDto = customerInfoService.fingByphone(telephone);
        CustomerDTO customerDTO = resultDto.getData();
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("customerId",customerDTO.getId());
        paramsMap.put("catagoryId",catagoryId);
        List<RContractDocument> rContractDocuments = rContractDocumentMapper.ocrDocument(paramsMap);
        List<Map<String,String>> mapList = new ArrayList<>();
        Integer confirm = 1;
        if (rContractDocuments != null) {
            for (RContractDocument rContractDocument : rContractDocuments){
                Map<String,String> map = new HashMap<>();
                map.put("documentId", rContractDocument.getDocumentId().toString());
                map.put("url", aliyunOssUrl + ResultResource.DOWNLOADFOOTPATH +rContractDocument.getDocument().getDocumentSavepath() + rContractDocument.getDocument().getDocumentSavename());
                if (confirm.equals(rContractDocument.getDocument().getIsFlag()))
                    map.put("confirm", "0");
                else
                    map.put("confirm", "1");
                mapList.add(map);
            }
        }
        return mapList;
    }

    //渠道交易的附件
    public CronusDto findDocByServiceId(Integer serviceId){
        CronusDto resultDto = new CronusDto();
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("serviceContractId",serviceId);
        List<RContractDocument> documentList = rContractDocumentMapper.ocrDocumentType(paramsMap);
        List<OcrDocumentDto> ocrDocumentDtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(documentList)){
            RContractDocument rcdocument = documentList.get(0);
            OcrDocumentDto ocrDocumentDto = new OcrDocumentDto();
            ocrDocumentDto.setDocument_id(rcdocument.getDocument().getId());
            ocrDocumentDto.setDocument_name(rcdocument.getDocumentName());
            ocrDocumentDto.setDocument_c_name(rcdocument.getDocumentCategory().getDocumentCName());
            ocrDocumentDto.setDocument_c_name_header(rcdocument.getDocumentCategory().getDocumentCNameHeader());
            ocrDocumentDto.setRc_document_id(rcdocument.getId());
            ocrDocumentDto.setDocumentSavename(rcdocument.getDocument().getDocumentSavename());
            ocrDocumentDto.setFlag(rcdocument.getDocument().getIsFlag());
            ocrDocumentDto.setDocumentSavepath(ResultResource.DOWNLOADFOOTPATH + rcdocument.getDocument().getDocumentSavepath());
            ocrDocumentDto.setUrl(aliyunOssUrl + ResultResource.DOWNLOADFOOTPATH +rcdocument.getDocument().getDocumentSavepath() + rcdocument.getDocument().getDocumentSavename());
            ocrDocumentDtos.add(ocrDocumentDto);
        }


        resultDto.setData(ocrDocumentDtos);
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);

        return  resultDto;
    }
}
