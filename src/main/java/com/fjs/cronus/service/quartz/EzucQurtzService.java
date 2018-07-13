package com.fjs.cronus.service.quartz;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonEnum;
import com.fjs.cronus.Common.CommonRedisConst;
import com.fjs.cronus.entity.EzucDataDetail;
import com.fjs.cronus.entity.EzucQurtzLog;
import com.fjs.cronus.entity.SalesmanCallData;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.EzucDataDetailMapper;
import com.fjs.cronus.mappers.EzucQurtzLogMapper;
import com.fjs.cronus.mappers.SalesmanCallDataMapper;
import com.fjs.cronus.service.EzucDataDetailService;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 定时任务处理通过时间业务服务.
 */
@Service
public class EzucQurtzService {

    private Logger logger = LoggerFactory.getLogger(EzucQurtzService.class);

    /**
     * 请求EZUC系统：地址.
     */
    private String requestUrl = "http://120.26.74.199:8080/ucrm/api/exportCallDetails";
    /**
     * 请求EZUC系统：参数的模版.
     */
    private String paramsTemplate = "apiKey={APIKEY}&dialResult={DIALRESULT}&pageIndex={PAGEINDEX}&pageSize={PAGESIZE}&startTime={STARTTIME}&endTime={ENDTIME}";
    /**
     * 请求EZUC系统：认证.
     */
    private String apiKey = "XIOxUFOeE5BPWZhbmdqaW5zdW89TllZeE49PFQfUCI";
    /**
     * 请求EZUC系统：获取通话类型，0为成功.
     */
    private int dialResult = 0;

    /**
     * 分页处理，每页数据量.
     */
    private int sizePerPage = 1000;

    @Autowired
    private EzucDataDetailMapper ezucDataDetailMapper;

    @Autowired
    private EzucQurtzLogMapper ezucQurtzLogMapper;

    @Autowired
    private EzucDataDetailService ezucDataDetailService;

    @Resource
    RedisTemplate redisTemplateOps;

    @Autowired
    private SalesmanCallDataMapper salesmanCallDataMapper;

    /**
     * 当前时间.
     */
    private Date now;

    /**
     * 重试次数.
     */
    private int retry = 5;

    /**
     * 将 EZUC 数据同步过来（定时调用）.
     */
    public void syncData4Qurtz() {

        // 获取当前时间
        Date now = new Date();

        // 获取触发时间
        ValueOperations<String, Number> operations = redisTemplateOps.opsForValue();
        Number runTime = operations.get(CommonRedisConst.EZUC_DURATION_QUARTZ_KEY);
        if (runTime != null && now.getTime() < runTime.longValue()) {
            logger.info("EZUC 数据同步,被取消,未到触发时间,runTime=" + runTime);
            return;
        }

        // 设置下次触发时间
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        c.add(Calendar.DAY_OF_YEAR, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 10);
        c.set(Calendar.MILLISECOND, 0);
        Date nextRunTime = c.getTime();
        operations.set(CommonRedisConst.EZUC_DURATION_QUARTZ_KEY, nextRunTime.getTime());

        try {
            syncData(null, null);
        } catch (Exception e) {
            // 定时忽略错误
            logger.error("EZUC 数据同步,异常", e);
        }
    }

    /**
     * 将 EZUC 数据同步过来（接口调用）.
     *
     * @param date 指定同步哪天的数据；null则同步昨天的
     */
    public void syncData(String token, Date date) {

        JSONArray runInfo = new JSONArray(); // 收集运行中各数据，记录日志
        String key = null;
        try {
            runInfo.add(ImmutableMap.of("定时任务：开始", "start"));

            // 通时间内的数据，同时只能一个线程处理
            ValueOperations operations = redisTemplateOps.opsForValue();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startTime = ezucDataDetailService.getStartTime(date);
            key = CommonRedisConst.EZUC_DURATION_QUARTZ_KEY + sdf.format(startTime);
            Boolean aBoolean = operations.setIfAbsent(key, 1);
            aBoolean = aBoolean == null ? false : aBoolean;

            if (!aBoolean) {
                runInfo.add(ImmutableMap.of("该时间的数据是否正被处理", aBoolean));
            } else {

                now = new Date();

                // 需要处理的总数
                int count = getDataCount(runInfo, date);
                if (count > 0) {
                    // 分页
                    int pageCount = getPageCount(count, sizePerPage);
                    runInfo.add(ImmutableMap.of("总页数", pageCount));

                    // 分页处理数据
                    for (int i = 1; i <= pageCount; i++) {
                        JSONArray jSONArray = new JSONArray();
                        try {
                            jSONArray = getPageDate(i, runInfo, date);
                        } catch (Exception e) {
                            // 吃掉单页请求，错误，不影响其他页数据，但必须记录日志.
                            if (e instanceof CronusException) {
                                // 已知异常
                                CronusException temp = (CronusException) e;
                                runInfo.add(ImmutableMap.of("已知异常,查询第 " + i + " 页, status", Integer.valueOf(temp.getResponseError().getStatus())));
                                runInfo.add(ImmutableMap.of("已知异常,查询第 " + i + " 页, message", temp.getResponseError().getMessage()));
                            } else {
                                // 未知异常
                                logger.error("将 EZUC 数据同步过来", e);
                                runInfo.add(ImmutableMap.of("未知异常,查询第 " + i + " 页, message", e.getMessage()));
                            }
                        }
                        runInfo.add(ImmutableMap.of("查询第 " + i + " 页,该页总数据量", jSONArray.size()));
                        for (int j = 0; j < jSONArray.size(); j++) {
                            JSONObject jsonObject = jSONArray.getJSONObject(j);
                            addSingleData(jsonObject);
                        }
                    }

                    // 数据入缓存
                    //ezucDataDetailService.refreshCache(date);
                }

            }

        } catch (Exception e) {
            if (e instanceof CronusException) {
                // 已知异常
                CronusException temp = (CronusException) e;
                runInfo.add(ImmutableMap.of("已知异常, status", Integer.valueOf(temp.getResponseError().getStatus())));
                runInfo.add(ImmutableMap.of("已知异常, message", temp.getResponseError().getMessage()));
            } else {
                // 未知异常
                logger.error("将 EZUC 数据同步过来", e);
                runInfo.add(ImmutableMap.of("未知异常, message", e.getMessage()));
            }
        } finally {
            if (StringUtils.isNotBlank(key)) {
                redisTemplateOps.delete(key);
            }
        }

        EzucQurtzLog e = new EzucQurtzLog();
        e.setCreated(now);
        e.setStatus(CommonEnum.entity_status1.getCode());
        e.setRuninfo(runInfo.toString());
        ezucQurtzLogMapper.insert(e);

    }

    /**
     * 数据入库.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void addSingleData(JSONObject jsonObject) {

        String callerDispName = jsonObject.getString("callerDispName");
        Long startTime = jsonObject.getLong("startTime");

        // 记录EZUC数据
        Boolean isexist = null;
        Boolean isexist2 = null;
        if (StringUtils.isNotBlank(callerDispName) && startTime != null) {

            /*EzucDataDetail i = new EzucDataDetail();
            i.setCallerDispName(callerDispName);
            i.setStartTime(startTime);
            i.setStatus(CommonEnum.entity_status1.getCode());
            int i1 = ezucDataDetailMapper.selectCount(i);
            if (i1 > 0) {
                isexist = true; // 已存在的记录无需再次插入
            };*/

            SalesmanCallData i2 = new SalesmanCallData();
            i2.setSalesManName(callerDispName);
            i2.setStartTime(startTime);
            i2.setStatus(CommonEnum.entity_status1.getCode());
            int i3 = salesmanCallDataMapper.selectCount(i2);
            if (i3 > 0) {
                isexist2 = true; // 已存在的记录无需再次插入
            }
        }

        /*if (isexist == null || !isexist) {
            // 记录到 ezuc_data_detail 表
            EzucDataDetail data = new EzucDataDetail();
            data.setCreated(now);
            data.setStatus(CommonEnum.entity_status1.getCode());
            data.setCallerDispName(callerDispName);
            data.setCallerDeptName(jsonObject.getString("callerDeptName"));
            data.setCallerAccount(jsonObject.getString("callerAccount"));
            data.setCallerDbid(jsonObject.getInteger("callerDbid"));
            data.setCallerDeptId(jsonObject.getInteger("callerDeptId"));
            data.setDuration(jsonObject.getLong("duration"));
            data.setTotalDuration(jsonObject.getLong("totalDuration"));
            data.setStartTime(startTime);
            data.setEndTime(jsonObject.getInteger("endTime"));
            data.setAnswerTime(jsonObject.getInteger("answerTime"));
            data.setCalleeExt(jsonObject.getString("calleeExt"));
            data.setData(jsonObject.toString());
            ezucDataDetailMapper.insertSelective(data);
        }*/

        if (isexist2 == null || !isexist2) {
            // 记录到 salesman_call_data 表
            SalesmanCallData data = new SalesmanCallData();
            data.setSalesManName(callerDispName);
            data.setCustomerPhoneNum(jsonObject.getString("calleeExt"));
            data.setStartTime(startTime);
            data.setAnswerTime(jsonObject.getLong("answerTime"));
            data.setEndTime(jsonObject.getLong("endTime"));
            data.setDuration(jsonObject.getLong("duration"));
            data.setTotalDuration(jsonObject.getLong("totalDuration"));
            data.setCallType(0);
            data.setRecordingUrl(jsonObject.getString("recordingUrl"));
            data.setSystype(CommonConst.SYSTYPE_EZUC);
            data.setCreated(new Date());
            salesmanCallDataMapper.insertSelective(data);
        }
    }

    /**
     * 获取url中请求的endtime参数.
     */
    private String getEndTime(Date date) {
        Calendar now = Calendar.getInstance();
        if (date != null) {
            // 指定同步的时间
            now.setTime(date);
        } else {
            // 无就去昨天的
            now.add(Calendar.DAY_OF_MONTH, -1);
        }
        now.set(Calendar.HOUR_OF_DAY, 23);
        now.set(Calendar.MINUTE, 59);
        now.set(Calendar.MILLISECOND, 59);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(now.getTime());
    }

    /**
     * 获取url中请求的startTime参数.
     */
    private static String getStartTime(Date date) {
        Calendar now = Calendar.getInstance();
        if (date != null) {
            // 指定同步的时间
            now.setTime(date);
        } else {
            // 无就去昨天的
            now.add(Calendar.DAY_OF_MONTH, -1);
        }
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.MILLISECOND, 0);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(now.getTime());
    }

    /**
     * 获取每页数据.
     */
    private JSONArray getPageDate(Integer pageNum, JSONArray runInfo, Date date) {
        runInfo.add(ImmutableMap.of("查询第 " + pageNum + " 页", "start"));

        String url = requestUrl + "?" + paramsTemplate;
        Map<String, Object> requestParams = getRequestParams(date);
        requestParams.put("PAGEINDEX", pageNum);

        ResponseEntity<JSONObject> responseEntity = null;

        int i = 0;
        while (i <= retry) {
            if (i > 0) {
                runInfo.add(ImmutableMap.of("查询第 " + pageNum + " 页", "开始第" + i + "次，重试请求"));
            }
            i++;
            try {
                // 处理网络请求可能的异常（例如：服务器网络不通）
                responseEntity = getRestTemplate().getForEntity(url, JSONObject.class, requestParams);
                break;
            } catch (Exception e) {
                if (i >= retry) {
                    throw new CronusException(CronusException.Type.CRM_OTHER_ERROR, "http request exception, retry=" + i + ", error message=" + e.getMessage());
                }
            }
        }

        if (responseEntity != null && responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
            JSONObject body = responseEntity.getBody();

            // 处理业务层面可能的异常
            Integer returnCode = body.getInteger("returnCode");
            if (returnCode == null || returnCode != 100) {
                String returnInfo = body.getString("returnInfo");
                throw new CronusException(CronusException.Type.CRM_OTHER_ERROR, "http request business exception, returnCode=" + returnCode + ",returnInfo=" + returnInfo);
            }

            runInfo.add(ImmutableMap.of("查询第 " + pageNum + " 页", "end"));
            return body.getJSONArray("results"); // 每页具体数据量太大，不记录到日志中去
        }
        // 处理网络请求可能的异常（例如：500）
        throw new CronusException(CronusException.Type.CRM_OTHER_ERROR, "http request exception, http response code=" + responseEntity.getStatusCodeValue());
    }

    /**
     * 获取需要处理数据总数.
     */
    private int getDataCount(JSONArray runInfo, Date date) {
        runInfo.add(ImmutableMap.of("查询总数据数", "start"));

        String url = requestUrl + "?" + paramsTemplate;
        Map<String, Object> requestParams = getRequestParams(date);
        requestParams.put("PAGESIZE", 1); // 此方法只获取数据总数量，不需要太多明细数据
        runInfo.add(ImmutableMap.of("查询总数据数，url", url));
        runInfo.add(ImmutableMap.of("查询总数据数，参数", requestParams));

        ResponseEntity<JSONObject> responseEntity = null;

        int i = 0;
        while (i <= retry) {
            i++;
            try {
                // 处理网络请求可能的异常（例如：服务器网络不通）
                responseEntity = getRestTemplate().getForEntity(url, JSONObject.class, requestParams);
                break;
            } catch (Exception e) {
                if (i >= retry) {
                    throw new CronusException(CronusException.Type.CRM_OTHER_ERROR, "http request exception, retry=" + i + ", error message=" + e.getMessage());
                }
            }
        }

        if (responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
            JSONObject body = responseEntity.getBody();

            // 处理业务层面可能的异常
            Integer returnCode = body.getInteger("returnCode");
            if (returnCode == null || returnCode != 100) {
                String returnInfo = body.getString("returnInfo");
                throw new CronusException(CronusException.Type.CRM_OTHER_ERROR, "http request business exception, returnCode=" + returnCode + ",returnInfo=" + returnInfo);
            }
            Integer totalCount = body.getInteger("totalCount");

            runInfo.add(ImmutableMap.of("查询总数据数,结束。totalCount", totalCount));
            return totalCount == null ? 0 : totalCount;
        }
        // 处理网络请求可能的异常（例如：500）
        throw new CronusException(CronusException.Type.CRM_OTHER_ERROR, "http request exception, http response code=" + responseEntity.getStatusCodeValue());
    }

    /**
     * 请求的参数.
     */
    private Map<String, Object> getRequestParams(Date date) {
        Map<String, Object> params = new HashMap<>();
        params.put("APIKEY", apiKey);
        params.put("DIALRESULT", dialResult);
        params.put("PAGEINDEX", 1);
        params.put("PAGESIZE", sizePerPage);
        params.put("STARTTIME", getStartTime(date));
        params.put("ENDTIME", getEndTime(date));
        return params;
    }

    /**
     * 获取总页数.
     */
    private static int getPageCount(int count, int size) {
        if (count == 0 || size == 0) {
            return 0;
        }
        if (count > size) {
            return count % size == 0 ? count / size : count / size + 1;
        } else {
            return 1;
        }
    }

    /**
     * 获取restTemplate.
     */
    private RestTemplate getRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000);
        factory.setReadTimeout(3000);
        return new RestTemplate(factory);
    }
}
