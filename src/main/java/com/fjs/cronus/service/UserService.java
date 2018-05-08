package com.fjs.cronus.service;

import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.api.PhpApiDto;
import com.fjs.cronus.config.RedisConfig;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.UserMonthInfoDTO;
import com.fjs.cronus.dto.api.ThorApiDTO;
import com.fjs.cronus.dto.api.uc.AppUserDto;
import com.fjs.cronus.dto.api.uc.CityDto;
import com.fjs.cronus.dto.api.uc.PhpDepartmentModel;
import com.fjs.cronus.dto.api.uc.SubCompanyDto;
import com.fjs.cronus.dto.uc.CrmCitySubCompanyDto;
import com.fjs.cronus.dto.uc.LightUserInfoDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.model.AllocateLog;
import com.fjs.cronus.model.CustomerUseful;
import com.fjs.cronus.model.UserMonthInfo;
import com.fjs.cronus.service.client.ThorService;
import com.fjs.cronus.service.redis.AllocateRedisService;
import com.fjs.cronus.service.redis.UserInfoRedisService;
import com.fjs.cronus.service.thea.TheaClientService;
import com.fjs.cronus.util.DateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import static java.util.stream.Collectors.*;

/**
 * Created by yinzf on 2017/10/20.
 */

@Service
public class UserService {

    @Value("${token.current}")
    private String publicToken;

//    @Autowired
//    private ThorUcService thorUcService;

    @Autowired
    private ThorService thorService;

    @Autowired
    private RedisConfig redisConfig;

    @Autowired
    private UserMonthInfoService userMonthInfoService;

    @Autowired
    private UserInfoRedisService userInfoRedisService;

    @Autowired
    private AllocateLogService allocateLogService;

    @Autowired
    private CustomerUsefulService customerUsefulService;

    @Autowired
    private AllocateRedisService allocateRedisService;

//    @Autowired
//    private ConfigRedisService configRedisService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private TheaClientService theaClientService;

    public List<SubCompanyDto> getDepartmentByWhere(String token, Integer user_id) {
        List<SubCompanyDto> subCompanyDtos = new ArrayList<SubCompanyDto>();

        long startTime = System.currentTimeMillis();
        PhpApiDto<List<SubCompanyDto>> phpApiDto = thorService.getAllCompanyByUserId(token, user_id, CommonConst.SYSTEMNAME);

        long endTime = System.currentTimeMillis();
        float seconds = (endTime - startTime) / 1000F;
//        System.out.println("执行时间："+Float.toString(seconds) + " seconds.");
//        System.out.println(phpApiDto.getRetData());
        subCompanyDtos = phpApiDto.getRetData();
        return subCompanyDtos;
    }


    public Map<String, List<UserMonthInfoDTO>> getUserMonthInfoList(String city, Integer companyId, String effectiveDate, Integer userIdByOption, Integer mediaid) throws Exception {

        // 获取公司员工列表
        ThorApiDTO<List<LightUserInfoDTO>> baseUcDTO = thorService.getUserlistByCompanyId(publicToken, companyId);
        if (baseUcDTO.getResult().equals(1) || baseUcDTO.getData().size() == 0) {
            return null;
        }

        // 员工ids
        List<Integer> companyUserIds = new ArrayList<>();
        // 设置初始化列表项
        List<UserMonthInfoDTO> userMonthInfoDTOList = new ArrayList<>();
        for (LightUserInfoDTO lightUserInfoDTO : baseUcDTO.getData()) {

            companyUserIds.add(lightUserInfoDTO.getId());

            UserMonthInfoDTO userMonthInfoDTO = new UserMonthInfoDTO();
            userMonthInfoDTO.setUserId(lightUserInfoDTO.getId());
            userMonthInfoDTO.setDepartmentId(lightUserInfoDTO.getDepartmentId());

            if (StringUtils.isNotBlank(lightUserInfoDTO.getName())) {
                userMonthInfoDTO.setName(lightUserInfoDTO.getName());
            }
            if (StringUtils.isNotBlank(lightUserInfoDTO.getDepartment())) {
                userMonthInfoDTO.setDepartmentName(lightUserInfoDTO.getDepartment());
            }
            userMonthInfoDTOList.add(userMonthInfoDTO);
        }

        // 获取员工分配信息
        Map<String, Object> selectMap = new HashMap<>();
        selectMap.put("effectiveDate", effectiveDate);
        selectMap.put("userIds", companyUserIds);
        selectMap.put("companyid", companyId);
        selectMap.put("mediaid", mediaid);
        List<UserMonthInfo> userMonthInfoList = userMonthInfoService.selectByParamsMap(selectMap);

        // ===== 下面循环，做2件事 =====
        // 1、db中的数据 ----赋值---> userMonthInfoDTOList
        // 2、未设置值的数据需要入库db
        List<UserMonthInfo> toAddUserMonthInfoList = new ArrayList<>();
        Map<Integer, List<UserMonthInfo>> userIdMappingData = CollectionUtils.isEmpty(userMonthInfoList) ? new HashMap<>() : userMonthInfoList.stream().collect(groupingBy(UserMonthInfo::getUserId));
        Date now = new Date();

        for (UserMonthInfoDTO userMonthInfoDTO : userMonthInfoDTOList) {
            List<UserMonthInfo> userMonthInfos = userIdMappingData.get(userMonthInfoDTO.getUserId());
            if (CollectionUtils.isNotEmpty(userMonthInfos) && userMonthInfos.get(0) != null) {
                // db中有，则赋值给vo
                UserMonthInfo userMonthInfo = userMonthInfos.get(0);
                if (userMonthInfo.getBaseCustomerNum() == null) {
                    throw new CronusException(CronusException.Type.THEA_SYSTEM_ERROR, "数据异常，库中有数据，但是该条数据无BaseCustomerNum");
                }

                userMonthInfoDTO.setAssignedCustomerNum(0);
                userMonthInfoDTO.setEffectiveCustomerNum(0);
                userMonthInfoDTO.setBaseCustomerNum(userMonthInfo.getBaseCustomerNum());
                userMonthInfoDTO.setLastUpdateTime(userMonthInfo.getLastUpdateTime());
                userMonthInfoDTO.setRewardCustomerNum(userMonthInfo.getRewardCustomerNum());
                userMonthInfoDTO.setLastUpdateTime(userMonthInfo.getLastUpdateTime());
                userMonthInfoDTO.setLastUpdateUser(userMonthInfo.getLastUpdateUser());
                userMonthInfoDTO.setEffectiveDate(userMonthInfo.getEffectiveDate());

            } else {
                // db中无，则需要db新增，vo设置初始化值
                userMonthInfoDTO.setBaseCustomerNum(CommonConst.COMPANY_MEDIA_QUEUE_COUNT.equals(mediaid) ? CommonConst.BASE_CUSTOMER_NUM : 0);
                userMonthInfoDTO.setRewardCustomerNum(CommonConst.REWARD_CUSTOMER_NUM);
                userMonthInfoDTO.setLastUpdateTime(now);
                userMonthInfoDTO.setEffectiveCustomerNum(0);
                userMonthInfoDTO.setCreateTime(now);
                userMonthInfoDTO.setEffectiveDate(effectiveDate);

                UserMonthInfo userMonthInfoTemp = new UserMonthInfo();
                userMonthInfoTemp.setBaseCustomerNum(CommonConst.COMPANY_MEDIA_QUEUE_COUNT.equals(mediaid) ? CommonConst.BASE_CUSTOMER_NUM : 0);
                userMonthInfoTemp.setAssignedCustomerNum(0);
                userMonthInfoTemp.setEffectiveCustomerNum(0);
                userMonthInfoTemp.setEffectiveDate(effectiveDate);
                userMonthInfoTemp.setRewardCustomerNum(CommonConst.REWARD_CUSTOMER_NUM);
                userMonthInfoTemp.setLastUpdateTime(now);
                userMonthInfoTemp.setCreateTime(now);
                userMonthInfoTemp.setUserId(userMonthInfoDTO.getUserId());
                userMonthInfoTemp.setCreateUserId(userIdByOption);
                userMonthInfoTemp.setLastUpdateUser(userIdByOption);
                userMonthInfoTemp.setCompanyid(companyId);
                userMonthInfoTemp.setMediaid(mediaid);
                toAddUserMonthInfoList.add(userMonthInfoTemp);
            }
        }

        // 未分配数据数据入库
        if (CollectionUtils.isNotEmpty(toAddUserMonthInfoList)) {
            userMonthInfoService.insertList(toAddUserMonthInfoList);
        }

        // ===== 获取这些业务员的 <已分配数> 和 <有效数> =====
        // 获取计算<已分配数>的数据源
        Map<String, Object> allocateMap = new HashMap<>();
        allocateMap.put("newOwnerIds", companyUserIds);
        allocateMap.put("createBeginDate", DateUtils.getBeginDateByStr(effectiveDate));
        allocateMap.put("createEndDate", DateUtils.getEndDateByStr(effectiveDate));
        List<AllocateLog> allocateLogList = allocateLogService.selectByParamsMap(allocateMap);
        Map<Integer, Long> newOwnerIdMappingCount = CollectionUtils.isEmpty(allocateLogList) ? new HashMap<>() : allocateLogList.stream().collect(groupingBy(AllocateLog::getNewOwnerId, counting()));

        // 获取计算<有效数>的数据源
        Map<String, Object> userFullSelectMap = new HashMap<>();
        Date date = DateUtils.getBeginDateByStr(effectiveDate);
        userFullSelectMap.put("year", DateUtils.getYear2(date));
        userFullSelectMap.put("month", DateUtils.getMonth(date));
        userFullSelectMap.put("loanAmountMin", 0);
        if (newOwnerIdMappingCount.keySet() != null && newOwnerIdMappingCount.keySet().size() > 0) {
            userFullSelectMap.put("inLoanId", newOwnerIdMappingCount.keySet());
        }
        List<CustomerUseful> customerUsefulList = customerUsefulService.countByMap(userFullSelectMap);
        Map<Integer, Long> createUserMappingCount = CollectionUtils.isEmpty(customerUsefulList) ? new HashMap<>() : customerUsefulList.stream().collect(groupingBy(CustomerUseful::getCreateUser, counting()));

        for (UserMonthInfoDTO userMonthInfoDTO : userMonthInfoDTOList) {
            // <已分配数>
            Long allocateLogs = newOwnerIdMappingCount.get(userMonthInfoDTO.getUserId());
            if (allocateLogs != null) {
                userMonthInfoDTO.setAssignedCustomerNum(userMonthInfoDTO.getAssignedCustomerNum() + allocateLogs.intValue());
            }
            // <有效数>
            Long aLong = createUserMappingCount.get(userMonthInfoDTO.getUserId());
            if (aLong != null) {
                userMonthInfoDTO.setEffectiveCustomerNum(userMonthInfoDTO.getEffectiveCustomerNum() + aLong.intValue());
            }
        }

        // 组装界面需要的数据结构
        return userMonthInfoDTOList.parallelStream()
                .filter(item -> item != null && StringUtils.isNotEmpty(item.getDepartmentName()))
                .collect(groupingBy(UserMonthInfoDTO::getDepartmentName, toList()));
    }

    public List<PhpDepartmentModel> getSubCompanys(String token, Integer companyId) {
        List<PhpDepartmentModel> phpDepartmentModelList = new ArrayList<PhpDepartmentModel>();
        PhpApiDto<List<PhpDepartmentModel>> phpApiDto = thorService.getSubCompanies(token, null, 1, null, companyId);
        System.out.println(phpApiDto.getRetData());
        phpDepartmentModelList = phpApiDto.getRetData();
        return phpDepartmentModelList;
    }

    /**
     * 获取分配队列
     */
    public List<Map<String, String>> getAllocateQueue(Integer companyid, Integer media, String effectiveDate) {
        List<Map<String, String>> allocateQueue = new ArrayList<>();
        List<Integer> ids = allocateRedisService.finaAllFromQueue(companyid, media, effectiveDate);
        for (Integer userId : ids) {
            Map<String, String> allocateQueueMap = new HashMap<>();
            AppUserDto appUserDto = this.getUserInfoByField(null, Integer.valueOf(userId), null);
            allocateQueueMap.put(userId.toString(), appUserDto.getName());
            allocateQueue.add(allocateQueueMap);
        }
        return allocateQueue;
    }

    public List<CrmCitySubCompanyDto> getOperateCompany(String token, Integer userId) {
        List<CrmCitySubCompanyDto> crmCitySubCompanyDtoList = new ArrayList<>();
        //获取用户可操作的城市
        PhpApiDto<List<CityDto>> phpApiDto =
                thorService.getSubcompanyByUserId(token, userId, CommonConst.SYSTEMNAME);
        if (null == phpApiDto.getRetData() || phpApiDto.getRetData().size() == 0) {
            return crmCitySubCompanyDtoList;
        }
        List<CityDto> cityDtoList = phpApiDto.getRetData();
        String mainCityStr = theaClientService.getConfigByName(CommonConst.MAIN_CITY);
        String remoteCityStr = theaClientService.getConfigByName(CommonConst.REMOTE_CITY);
        StringBuffer citiesStr = new StringBuffer();
        for (CityDto cityDto : cityDtoList) {
            //异地城市及主要城市包含在城市列表中
            if (mainCityStr.contains(cityDto.getName()) || remoteCityStr.contains(cityDto.getName())) {
                citiesStr.append(cityDto.getName());
                citiesStr.append(",");
            }
        }
        if (StringUtils.isNoneEmpty(citiesStr)) {
            CronusDto<List<CrmCitySubCompanyDto>> phpApiDto1 = thorService.getSubCompanyByCitys(token, citiesStr.toString());
            crmCitySubCompanyDtoList = phpApiDto1.getData();
        }
        return crmCitySubCompanyDtoList;
    }

    private AppUserDto getUserInfoByField(String telephone, Integer userId, String ownUserName) {
        AppUserDto appUserDto = null;
        if (userId != null) {
            String key = "USERINFO" + userId;
            RedisTemplate<String, Object> redisTemplate = redisConfig.valueOperations();
            ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
            if (redisTemplate.hasKey(key)) {
                appUserDto = (AppUserDto) valueOperations.get(key);
            }
            if (appUserDto != null) {
                return appUserDto;
            }
        }
        PhpApiDto<AppUserDto> apiDto = thorService.getUserInfoByFields(telephone, publicToken, userId, ownUserName);
        if (apiDto.getErrNum() == 0) {
            appUserDto = apiDto.getRetData();
        }
        return appUserDto;
    }

}
