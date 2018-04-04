package com.fjs.cronus.service.App;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonEnum;
import com.fjs.cronus.Common.ResultResource;
import com.fjs.cronus.dto.App.ReceiveAndKeepCountDTO;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.cronus.OcrDocumentDto;
import com.fjs.cronus.dto.uc.MemberApiDTO;
import com.fjs.cronus.mappers.AllocateLogMapper;
import com.fjs.cronus.mappers.CommunicationLogMapper;
import com.fjs.cronus.mappers.RContractDocumentMapper;
import com.fjs.cronus.model.RContractDocument;
import com.fjs.cronus.service.redis.CronusRedisService;
import com.fjs.cronus.service.uc.UcService;
import com.fjs.cronus.util.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by msi on 2017/12/28.
 */
@Service
public class AppService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

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
    @Resource
    RedisTemplate<String, String> redisTemplate;
    @Autowired
    UcService ucService;

    /**
     * redis缓存的key及有效时间10分钟(秒)
     */
    public static final String REDIS_CRONUS_GETRECEIVEANDKEEPCOUNT = "cronus-app-getReceiveAndKeepCount";
    public static final long REDIS_CRONUS_GETRECEIVEANDKEEPCOUNT_TIME = 600;


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
    public CronusDto<ReceiveAndKeepCountDTO> getReceiveAndKeepCount(Integer userId,String token){
        boolean boss = false; //调用UC当前登录人是否是总裁权限
        try {
            MemberApiDTO userRoles = ucService.getUserRoles(token);
            if (userRoles.getResult() == CommonEnum.ROLE_YES.getCode())
            if (null != userRoles ){
                String userRole = userRoles.getData().toString();
                if (userRole != null && userRole.contains(CommonConst.COMPANY_EXECUTIVES)){
                    boss = true;
                }
            }
        } catch (Exception e) {
            logger.error("getReceiveAndKeepCount >>>>> 获取高管角色失败" + e.getMessage(),e);
        }
        ValueOperations<String, String> redisOptions = null;

        CronusDto resultDto = new CronusDto();
        Map<String,Object> paramMap = new HashMap<>();
        ReceiveAndKeepCountDTO receiveAndKeepCountDTO = new ReceiveAndKeepCountDTO();
        Date date = new Date();
        if(!boss) {
            paramMap.put("createUserId", userId);
        } else {
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            redisTemplate.setValueSerializer(new StringRedisSerializer());
            redisOptions = redisTemplate.opsForValue();
            String redisDataStr = redisOptions.get(AppService.REDIS_CRONUS_GETRECEIVEANDKEEPCOUNT);
            if (StringUtils.isNotEmpty(redisDataStr)) {
                return JSONObject.parseObject(redisDataStr, CronusDto.class);
            }

            boss = true;
        }
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
        List<Integer> keepCustomer; //getKeepCustomerIdRedis
        if(!boss) {
            keepCustomer = getKeepCustomerId(userId);
        } else {
            keepCustomer = getKeepCustomerIdRedis();
        }
        receiveAndKeepCountDTO.setKeepCount(keepCustomer.size());
        if (keepCustomer == null || keepCustomer.size() == 0){
            receiveAndKeepCountDTO.setKeepCommunicationCount(0);
        }else {
            List<Integer> keepCommunication; //
            if (!boss) {
                keepCommunication = getKeepCommunication(keepCustomer,userId);
            } else {
                keepCommunication = getKeepCommunicationRedis(keepCustomer);
            }
            receiveAndKeepCountDTO.setKeepCommunicationCount(keepCommunication.size());
        }
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        resultDto.setData(receiveAndKeepCountDTO);

        //如果是boss权限，数据放入redis中
        try {
            if (boss) {
                redisOptions = redisTemplate.opsForValue();
                if (receiveAndKeepCountDTO.getAllocateCommunicationCount().intValue() != 0 || receiveAndKeepCountDTO.getAllocateCount().intValue() != 0
                        || receiveAndKeepCountDTO.getKeepCommunicationCount().intValue() != 0 || receiveAndKeepCountDTO.getKeepCount().intValue() != 0) {
                    redisOptions.set(AppService.REDIS_CRONUS_GETRECEIVEANDKEEPCOUNT, JSONObject.toJSONString(resultDto), AppService.REDIS_CRONUS_GETRECEIVEANDKEEPCOUNT_TIME, TimeUnit.SECONDS);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return resultDto;
    }

    /**
     * app移动CRM首页高管数据(分配数，沟通数)，放入缓存中
     */
    public void getReceiveAndKeepCountRedis(){
        ValueOperations<String, String> redisOptions = null;
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        CronusDto resultDto = new CronusDto();
        Map<String,Object> paramMap = new HashMap<>();
        ReceiveAndKeepCountDTO receiveAndKeepCountDTO = new ReceiveAndKeepCountDTO();
        Date date = new Date();

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
        List<Integer> keepCustomer = getKeepCustomerIdRedis();
        receiveAndKeepCountDTO.setKeepCount(keepCustomer.size());
        if (keepCustomer == null || keepCustomer.size() == 0){
            receiveAndKeepCountDTO.setKeepCommunicationCount(0);
        }else {
            List<Integer> keepCommunication = getKeepCommunicationRedis(keepCustomer);
            receiveAndKeepCountDTO.setKeepCommunicationCount(keepCommunication.size());
        }
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        resultDto.setData(receiveAndKeepCountDTO);

        //如果是boss权限，数据放入redis中
        try {
            redisOptions = redisTemplate.opsForValue();
            if (receiveAndKeepCountDTO.getAllocateCommunicationCount().intValue() != 0 || receiveAndKeepCountDTO.getAllocateCount().intValue() != 0
                    || receiveAndKeepCountDTO.getKeepCommunicationCount().intValue() != 0 || receiveAndKeepCountDTO.getKeepCount().intValue() != 0) {

                String redisDataStr = redisOptions.get(AppService.REDIS_CRONUS_GETRECEIVEANDKEEPCOUNT);
                if (StringUtils.isNotEmpty(redisDataStr)) {
                    //删除redis数据
                    redisTemplate.delete(AppService.REDIS_CRONUS_GETRECEIVEANDKEEPCOUNT);
                }
                redisOptions.set(AppService.REDIS_CRONUS_GETRECEIVEANDKEEPCOUNT, JSONObject.toJSONString(resultDto), AppService.REDIS_CRONUS_GETRECEIVEANDKEEPCOUNT_TIME, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }



    public List<Integer> getKeepCustomerId(Integer userId){
        Map<String,Object> paramMap = new HashMap<>();
        Date date = new Date();
        if(!(userId.equals(4)||userId.equals(1046)||userId.equals(1308))) {
            paramMap.put("createUserId", userId);
        }
        paramMap.put("operation", CommonConst.OPERATION);
        String  today = DateUtils.format(date,DateUtils.FORMAT_SHORT);
        paramMap.put("createTime",today);

        List<Integer> keepCount = allocateLogMapper.receiveIds(paramMap);
        return  keepCount;
    }
    public List<Integer> getKeepCustomerIdRedis(){
        Map<String,Object> paramMap = new HashMap<>();
        Date date = new Date();
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
        if(!(userId.equals(4)||userId.equals(1046)||userId.equals(1308))) {
            paramMap.put("createUserId", userId);
        }
        List<Integer> allocateCommunication = communicationLogMapper.allocateCommunication(paramMap);
        return  allocateCommunication;
    }
    public List<Integer> getKeepCommunicationRedis(List<Integer> keepCount){
        Map<String,Object> paramMap = new HashMap<>();
        Date date = new Date();
        String  today = DateUtils.format(date,DateUtils.FORMAT_SHORT);
        //today = "2017-12-27";
        paramMap.put("list",keepCount);
        paramMap.put("createTime",today);
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
                ocrDocumentDto.setUrl(aliyunOssUrl + ResultResource.DOWNLOADFOOTPATH +rcdocument.getDocument().getDocumentSavepath() + rcdocument.getDocument().getDocumentSavename());
                ocrDocumentDtos.add(ocrDocumentDto);
            }
        }
        queryResult.setRows(ocrDocumentDtos);
        queryResult.setTotal(count +"");
        return  queryResult;
    }
}
