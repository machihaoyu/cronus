package com.fjs.cronus.service.App;

import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.ResultResource;
import com.fjs.cronus.dto.App.ReceiveAndKeepCountDTO;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.cronus.OcrDocumentDto;
import com.fjs.cronus.mappers.AllocateLogMapper;
import com.fjs.cronus.mappers.CommunicationLogMapper;
import com.fjs.cronus.mappers.RContractDocumentMapper;
import com.fjs.cronus.model.CommunicationLog;
import com.fjs.cronus.model.RContractDocument;
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
                String bytes = FtpUtil.getInputStream(FTP_ADDRESS, FTP_PORT, FTP_USERNAME, FTP_PASSWORD, rcdocument.getDocument().getDocumentSavepath(), rcdocument.getDocument().getDocumentSavename());
                ocrDocumentDto.setUrl("data:image/jpeg;base64," + bytes);
                ocrDocumentDtos.add(ocrDocumentDto);
            }
            resultDto.setData(ocrDocumentDtos);
            resultDto.setResult(ResultResource.CODE_SUCCESS);
            resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        }
        return  resultDto;
    }
}
