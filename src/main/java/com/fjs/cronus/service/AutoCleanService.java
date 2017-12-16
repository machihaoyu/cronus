package com.fjs.cronus.service;

import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonEnum;
import com.fjs.cronus.api.thea.Config;
import com.fjs.cronus.api.thea.Loan;
import com.fjs.cronus.api.thea.MailDTO;
import com.fjs.cronus.dto.api.crius.CriusApiDTO;
import com.fjs.cronus.dto.loan.TheaApiDTO;
import com.fjs.cronus.dto.thea.MailBatchDTO;
import com.fjs.cronus.dto.uc.BaseUcDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.CustomerInfoLogMapper;
import com.fjs.cronus.model.AllocateLog;
import com.fjs.cronus.model.AutoCleanManage;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.model.CustomerInfoLog;
import com.fjs.cronus.service.client.TheaService;
import com.fjs.cronus.service.client.ThorUcService;
import com.fjs.cronus.service.thea.TheaClientService;
import com.fjs.cronus.util.CommonUtil;
import com.fjs.cronus.util.EntityToDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 自动清洗
 * Created by feng on 2017/10/11.
 */
@Service
public class AutoCleanService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${token.current}")
    private String publicToken;

    @Autowired
    private TheaService theaService;
    @Autowired
    private CustomerInfoService customerInfoService;
    @Autowired
    private AutoCleanManageService autoCleanManageService;

    @Autowired
    private ThorUcService thorUcService;

    @Autowired
    private AllocateLogService allocateLogService;

//    @Autowired
//    private LoanLogService loanLogService;

//    @Autowired
//    private Mai mailService;

    @Autowired
    private TheaClientService theaClientService;

    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private CustomerInfoLogMapper customerInfoLogMapper;
//    @Autowired
//    private ConfigMapper configMapper;

    /**
     * 自动清洗（暂定清洗时间为每周日晚上8点）
     */
    @Transactional
    public void autoClean(String token) {
        //将清洗状态配置设置为清洗中
        try {
            ValueOperations<String, String> redisConfigOptions = stringRedisTemplate.opsForValue();
            Config config;
            TheaApiDTO<Config> theaApiDTO = theaService.findByName(token, CommonConst.AUTO_CLEAN_STATUS);
            if (theaApiDTO.getResult() == 0) {
                config = theaApiDTO.getData();
                config.setConName(CommonConst.AUTO_CLEAN_STATUS);
                config.setConValue(CommonEnum.YES.getCode().toString());
                TheaApiDTO<Integer> theaApiDTO1 = theaService.updatebConfig(token, config);
                int save = 0;
//                if (theaApiDTO1.getResult()==0)
//                {
//                    save =theaApiDTO1.getData();
//                }
//                if (1 != save) {
//                    throw new CronusException(CronusException.Type.AUTO_CLEAN_ERROR);
//                }
            } else {
                config = new Config();
                config.setConName(CommonConst.AUTO_CLEAN_STATUS);
                config.setConValue(CommonEnum.YES.getCode().toString());
                CriusApiDTO criusApiDTO = theaService.addConfig(token, config);
                if (criusApiDTO.getResult() != 0) {
                    throw new CronusException(CronusException.Type.THEA_SYSTEM_ERROR);
                }
            }
            //修改redis配置
            redisConfigOptions.set(CommonConst.AUTO_CLEAN_STATUS, CommonEnum.YES.getCode().toString());
            //计算清洗前的各类型的数据量
            Map<String, Integer> beforeCountMap = new HashMap<>();
            beforeCountMap = customerInfoService.countForAutoClean();
            //获取各种需要屏蔽自动分配的的ID
            List<Integer> customerIdsByManage = this.countCannotAutoNumberModel();
            List<Integer> allUserIds = new ArrayList<>();
            List<Integer> userIdsByConfig = this.getCannotAutoUserIdByConfig();
            List<Integer> userIdsByCompany = this.getCannotAutoUserIdByCompany();
            //合并屏蔽用户ID集合
            if (null != userIdsByConfig && userIdsByConfig.size() > 0) {
                allUserIds.addAll(userIdsByConfig);
            }
            if (null != userIdsByCompany && userIdsByCompany.size() > 0) {
                userIdsByCompany.removeAll(allUserIds);
                allUserIds.addAll(userIdsByCompany);
            }
            //构建需要清洗的条件
            Map<String, Object> paramsMap = new HashMap<>();
//        List<Integer> inStatus = this.getAutoCleanStatus();
            paramsMap.put("level", "意向客户");
            paramsMap.put("remain", CommonEnum.NO.getCode());
            if (null != customerIdsByManage && customerIdsByManage.size() > 0) {
                paramsMap.put("customerIds", customerIdsByManage);
            }
            allUserIds.add(0);
            if (null != allUserIds && allUserIds.size() > 0) {
                paramsMap.put("notInOwnUserIds", allUserIds);
            }
            //查询需要清洗的交易ID的集合
            List<Integer> autoCleanCustomerIds = customerInfoService.selectForAutoClean(paramsMap);
            //清洗总条数
            Integer autoCleanCount = 0;
            if (null != autoCleanCustomerIds && autoCleanCustomerIds.size() > 0) {
                autoCleanCount = autoCleanCustomerIds.size();
                //遍历修改需要数据
                //获取每次批量需要清洗的数据的ID
                List<Loan> cleanLoanList = new ArrayList<>();
                List<List<Integer>> perAutoCleanIds = CommonUtil.splitList(autoCleanCustomerIds, 500);
                Map<String, Object> loanSaveBatchMap = new HashMap<>();
                Map<String, Object> loanSelectMap = new HashMap<>();
                Date updateDate = new Date();
                for (List<Integer> customerIdList : perAutoCleanIds) {//开始遍历修改数据
                    loanSaveBatchMap.clear();
                    loanSelectMap.clear();
                    //查找数据
                    loanSelectMap.put("ids", customerIdList);
                    List<CustomerInfo> loanList = customerInfoService.selectByParams(loanSelectMap);
                    //修改交易
                    loanSaveBatchMap.put("ownUserId", 0);
                    loanSaveBatchMap.put("lastUpdateTime", updateDate);
                    loanSaveBatchMap.put("paramsList", customerIdList);
                    customerInfoService.batchUpdate(loanSaveBatchMap);
                    //添加分配日志
                    List<AllocateLog> allocateLogList = this.initAllocateLog(loanList);
                    allocateLogService.insertBatch(allocateLogList);
                    //allocateLogSaveBatchMap.put("",)
                    //添加客户日志
//                    List<CustomerInfoLog> loanLogList = this.initLoanLog(loanList);
//                    customerInfoLogMapper.insertList(loanLogList);
                }
            }
            Map<String, Integer> afterCountMap = new HashMap<>();
            afterCountMap = customerInfoService.countForAutoClean();
            //重新设置清洗状态
            //修改redis配置
            config.setConValue(CommonEnum.NO.getCode().toString());
            TheaApiDTO<Integer> theaApiDTO1 = theaService.updatebConfig(token, config);
            int save = 0;
            if (theaApiDTO1.getResult() == 0) {
                save = theaApiDTO1.getData();
            }
            if (1 != save) {
                throw new CronusException(CronusException.Type.AUTO_CLEAN_ERROR);
            }
            redisConfigOptions.set(CommonConst.AUTO_CLEAN_STATUS, CommonEnum.NO.getCode().toString());
            //获取所有的业务员
            //添加消息信息！
            BaseUcDTO<List<Integer>> baseUcDTO = thorUcService.getAllSalesman(publicToken, "all");
            List<Integer> toIds = new ArrayList<>();
            if (baseUcDTO.getErrNum() == 0) {
                toIds = baseUcDTO.getRetData();
            }
            MailBatchDTO mailBatchDTO = new MailBatchDTO();
            List<MailDTO> mails = new ArrayList<>();
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < toIds.size(); i++) {
                stringBuilder.append(toIds.get(i).toString());
                if (i < toIds.size()-1) {
                    stringBuilder.append(",");
                }
            }
            mailBatchDTO.setContent(new Date().toString() + "系统自动清洗完毕！清洗总数:"+beforeCountMap.get("total")+"。" +
                    "清洗前的数据：未保留的客户有"+beforeCountMap.get("un_retain")+"条、保留的客户有"+beforeCountMap.get("is_retain")+"条。" +
                    "清洗后的数据：未保留的客户有"+afterCountMap.get("un_retain")+"条、保留的客户有"+afterCountMap.get("is_retain")+"条。" +
                    "自动清洗管理中屏蔽清洗的条数有:"+customerIdsByManage.size()+"条';");
            mailBatchDTO.setToId(stringBuilder.toString());
            theaClientService.sendMailBatch(publicToken, mailBatchDTO);

            System.out.println("清洗完成，清洗前：" + beforeCountMap + ",清洗后：" + afterCountMap);
        } catch (Exception e) {
            System.out.println("清洗失败：" + e.getMessage());
        }
    }


    /**
     * 获取屏蔽配置人员ID
     *
     * @return
     */
    public List<Integer> countCannotAutoNumberModel() {
        //获取有效的数据集合
        List<AutoCleanManage> autoCleanManageList = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();
        List<Integer> inStatus = this.getAutoCleanStatus();
        Map<String, Object> map = new HashMap<>();
        map.put("isDeleted", CommonEnum.NO.getCode());
        autoCleanManageList = autoCleanManageService.selectByParamsMap(map);
        if (null != autoCleanManageList && autoCleanManageList.size() > 0) {
            List<CustomerInfo> loanList = new ArrayList<>();
            for (AutoCleanManage autoCleanManage : autoCleanManageList) {
                map.clear();
                map.put("remain", CommonEnum.NO.getCode());
                map.put("ownUserId", autoCleanManage.getUserId());
                map.put("customerSource", autoCleanManage.getCustomerSource());
                //判断是否屏蔽这个来源的所有渠道
                if (!"*".equals(autoCleanManage.getUtmSource())) {//需要优化
                    map.put("utmSource", autoCleanManage.getUtmSource());
                }
                loanList = customerInfoService.selectByParams(map);
                if (null != loanList && loanList.size() > 0) {
                    for (CustomerInfo customerInfo : loanList) {
                        ids.add(customerInfo.getId());
                    }
                }
            }
        }
        return ids;
    }

    /**
     * 获取公司屏蔽自动清洗的用户id组（根据公司ID）
     *
     * @return
     */
    public List<Integer> getCannotAutoUserIdByCompany() {
        /*String value = configMapper.findValueByName(CommonConst.CAN_NOT_CLEAN_CUSTOMER_COMPANY);
        List<Integer> userIds = new ArrayList<>();
        if (StringUtils.isNotBlank(value)) {
            BaseUcDTO<List<Integer>> baseUcDTO = thorUcService.getUserIds(publicToken, null, null,
                    null, null, null, value);
            if (null != baseUcDTO.getRetData()) {
                userIds = baseUcDTO.getRetData();
            }
        }
        return userIds;*/
        return null;
    }

    /**
     * 需要屏蔽自动清洗的整个用户
     */
    public List<Integer> getCannotAutoUserIdByConfig() {
//        String value = configMapper.findValueByName(CommonConst.CAN_NOT_CLEAN_CUSTOMER_USER_ID);
//        List<Integer> userIds = new ArrayList<>();
//        if ( StringUtils.isNotBlank(value)) {
//            userIds = CommonUtil.initStrtoIntegerList(value);
//        }
//        return userIds;
        return null;
    }

    /**
     * 获取自动清洗的的状态
     */
    public List<Integer> getAutoCleanStatus() {
        List<Integer> list = new ArrayList<>();
        list.add(CommonEnum.RETAIN_STATUE_0.getCode());
//        list.add(CommonEnum.LOAN_STATUE_1.getCode());
//        list.add(CommonEnum.LOAN_STATUE_2.getCode());
//        list.add(CommonEnum.LOAN_STATUE_3.getCode());
        return list;
    }


    public List<AllocateLog> initAllocateLog(List<CustomerInfo> customerInfos) {
        List<AllocateLog> allocateLogList = new ArrayList<>();
        for (CustomerInfo customerInfo : customerInfos) {
            AllocateLog allocateLog = new AllocateLog();
            allocateLog.setOperation(CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_8.getCodeDesc());
            allocateLog.setCustomerId(customerInfo.getId());
            allocateLog.setLoanId(customerInfo.getId());
//                        allocateLog.get//客户名
            allocateLog.setOldOwnerId(customerInfo.getOwnUserId());
            allocateLog.setCreateUserId(0);
            allocateLog.setNewOwnerId(0);
            allocateLog.setCreateTime(new Date());
            allocateLog.setCreateUserName(CommonConst.SYSTEM_NAME);
            allocateLogList.add(allocateLog);
        }
        return allocateLogList;
//        return null;
    }

    public List<CustomerInfoLog> initLoanLog(List<CustomerInfo> loanList) {
        List<CustomerInfoLog> loanLogList = new ArrayList<>();
        for (CustomerInfo customerInfo : loanList) {
            CustomerInfoLog customerInfoLog = new CustomerInfoLog();
            EntityToDto.customerEntityToCustomerLog(customerInfo, customerInfoLog);
            customerInfoLog.setLogUserId(0);
            customerInfoLog.setLogCreateTime(new Date());
            customerInfoLog.setLogDescription(CommonEnum.LOAN_OPERATION_TYPE_8.getCodeDesc());
            loanLogList.add(customerInfoLog);
        }
        return loanLogList;
    }

    public Config findByType(Integer type, String token) {
        Config config = null;
        String name = "";
        if (type == 1) {
            name = CommonConst.CAN_NOT_CLEAN_CUSTOMER_COMPANY;
        }
        if (type == 2) {
            name = CommonConst.CAN_NOT_CLEAN_CUSTOMER_USER_ID;
        }
        TheaApiDTO<Config> resultDto = theaService.findByName(token, CommonConst.CAN_NOT_CLEAN_CUSTOMER_COMPANY);
        config = resultDto.getData();
        return config;
    }

    public static void main(String args[]) {
        /*List<Integer> all = new ArrayList<>();
        List<Integer> ad = new ArrayList<>();
        ad.add(1);
        ad.add(2);
        List<Integer> add = new ArrayList<>();
        add.add(1);
        add.add(3);
        all.addAll(ad);
        all.removeAll(add);
        all.addAll(add);
        System.out.println(all);*/
        List<Integer> a = new ArrayList<>();
        for (int i = 0; i <= 2; i++) {
            a.add(i);
        }
        List<List<Integer>> once = CommonUtil.splitList(a, 4);
        System.out.println(once);

    }
}
