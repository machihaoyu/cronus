package com.fjs.cronus.service.App;

import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.ResultResource;
import com.fjs.cronus.dto.App.ReceiveAndKeepCountDTO;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.cronus.OcrDocumentDto;
import com.fjs.cronus.mappers.AllocateLogMapper;
import com.fjs.cronus.mappers.CommunicationLogMapper;
import com.fjs.cronus.mappers.RContractDocumentMapper;
import com.fjs.cronus.model.CommunicationLog;
import com.fjs.cronus.model.RContractDocument;
import com.fjs.cronus.service.DocumentService;
import com.fjs.cronus.service.redis.CronusRedisService;
import com.fjs.cronus.util.DateUtils;
import com.fjs.cronus.util.FtpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by msi on 2017/12/28.
 */
@Service
public class AppService {


    private List<String> list = new ArrayList<String>(){
        {
            add(CommonConst.OPERARIONAll);
            add(CommonConst.OPERARIONNO);
        }
    };
    @Autowired
    private AllocateLogMapper allocateLogMapper;
    @Autowired
    private CommunicationLogMapper communicationLogMapper;
    @Autowired
    RContractDocumentMapper rContractDocumentMapper;
    @Autowired
    CronusRedisService cronusRedisService;


    private static String endpoint;

    private static String accessKeyId;

    private static String accessKeySecret;

    private static String bucketName;

    private static String aliyunOssUrl;

    @Value("${aliyun.oss.endpoint}")
    public void setEndpoint(String endpoint) {
        AppService.endpoint = endpoint;
    }

    @Value("${aliyun.oss.accessKeyId}")
    public void setAccessKeyId(String accessKeyId) {
        AppService.accessKeyId = accessKeyId;
    }

    @Value("${aliyun.oss.accessKeySecret}")
    public void setAccessKeySecret(String accessKeySecret) {
        AppService.accessKeySecret = accessKeySecret;
    }

    @Value("${aliyun.oss.bucketName}")
    public void setBucketName(String bucketName) {
        AppService.bucketName = bucketName;
    }

    @Value("${aliyun.oss.url}")
    public void setAliyunOssUrl(String aliyunOssUrl) {
        AppService.aliyunOssUrl = aliyunOssUrl;
    }
    public CronusDto<ReceiveAndKeepCountDTO> getReceiveAndKeepCount(Integer userId){

        CronusDto resultDto = new CronusDto();
        Map<String,Object> paramMap = new HashMap<>();
        ReceiveAndKeepCountDTO receiveAndKeepCountDTO = new ReceiveAndKeepCountDTO();
        Date date = new Date();
        paramMap.put("createUserId",userId);
        paramMap.put("operationList", list);
        String  today = DateUtils.format(date,DateUtils.FORMAT_SHORT);
       // String today = "2017-12-27";
        // paramMap.put("operation",CommonConst.OPERATION);
        paramMap.put("createTime",today);
        List<Integer> allocateIds =allocateLogMapper.getReceiveCount(paramMap);
        //获取分配的沟通数
        receiveAndKeepCountDTO.setAllocateCount(allocateIds.size());
        paramMap.put("list",allocateIds);
        if (allocateIds == null || allocateIds.size() == 0){
            receiveAndKeepCountDTO.setAllocateCommunicationCount(0);
        }else {
            List<Integer> allocateCommunication = communicationLogMapper.allocateCommunication(paramMap);
            receiveAndKeepCountDTO.setAllocateCommunicationCount(allocateCommunication.size());
        }
        //获取客户领取数
        List<Integer> keepCustomer = getKeepCusomerId(userId);
        receiveAndKeepCountDTO.setKeepCount(keepCustomer.size());
        if (keepCustomer == null || keepCustomer.size() == 0){
            receiveAndKeepCountDTO.setKeepCommunicationCount(0);
        }else {
            List<Integer> keepCommunication = getKeepCommunication(keepCustomer,userId);
            receiveAndKeepCountDTO.setKeepCommunicationCount(keepCommunication.size());
        }
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        resultDto.setData(receiveAndKeepCountDTO);
        return resultDto;
    }

    public List<Integer> getKeepCusomerId(Integer userId){
        Map<String,Object> paramMap = new HashMap<>();
        Date date = new Date();
        paramMap.put("createUserId",userId);
        paramMap.put("operation", CommonConst.OPERATION);
        String  today = DateUtils.format(date,DateUtils.FORMAT_SHORT);
        paramMap.put("createTime",today);

        List<Integer> keepCount = allocateLogMapper.receiveIds(paramMap);
        return  keepCount;
    }
    public List<Integer> getKeepCommunication(List<Integer> keepCount,Integer userId){
        Map<String,Object> paramMap = new HashMap<>();
        Date date = new Date();
        String  today = DateUtils.format(date,DateUtils.FORMAT_SHORT);
        //today = "2017-12-27";
        paramMap.put("list",keepCount);
        paramMap.put("createTime",today);
        paramMap.put("createUserId",userId);
        List<Integer> allocateCommunication = communicationLogMapper.allocateCommunication(paramMap);
        return  allocateCommunication;
    }

    /**
     * 获取附件列表
     * @param customerId
     * @return
     */
    public QueryResult<OcrDocumentDto> findDocByCustomerId(Integer customerId, Integer page, Integer size){
        QueryResult<OcrDocumentDto>  queryResult = new QueryResult<OcrDocumentDto> ();
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("customerId",customerId);
        paramsMap.put("start",(page-1) * size);
        paramsMap.put("size",size);
        List<OcrDocumentDto> ocrDocumentDtos = null;
        ocrDocumentDtos = new ArrayList<>();
        List<RContractDocument> documentList = rContractDocumentMapper.ocrAppDocument(paramsMap);
        Integer count  = rContractDocumentMapper.ocrAppDocumentCount(paramsMap);
        if (documentList.size() > 0) {
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
                ocrDocumentDto.setUrl(aliyunOssUrl + ResultResource.DOWNLOADFOOTPATH +rcdocument.getDocument().getDocumentSavepath() +"/"+ rcdocument.getDocument().getDocumentSavename());
                ocrDocumentDtos.add(ocrDocumentDto);
            }
        }
        queryResult.setRows(ocrDocumentDtos);
        queryResult.setTotal(count +"");
        return  queryResult;
    }
}
