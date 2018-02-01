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
import com.fjs.cronus.dto.uc.BaseUcDTO;
import com.fjs.cronus.dto.uc.CrmCitySubCompanyDto;
import com.fjs.cronus.dto.uc.LightUserInfoDTO;
import com.fjs.cronus.model.AllocateLog;
import com.fjs.cronus.model.CustomerUseful;
import com.fjs.cronus.model.UserMonthInfo;
import com.fjs.cronus.service.client.ThorService;
import com.fjs.cronus.service.redis.AllocateRedisService;
import com.fjs.cronus.service.redis.UserInfoRedisService;
import com.fjs.cronus.service.thea.TheaClientService;
import com.fjs.cronus.util.CommonUtil;
import com.fjs.cronus.util.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

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


    public Map<String, List<UserMonthInfoDTO>> getUserMonthInfoList(String city, Integer companyId, String effectiveDate, Integer userIdByOption) throws Exception {
        ThorApiDTO<List<LightUserInfoDTO>> baseUcDTO = thorService.getUserlistByCompanyId(publicToken, companyId);
        if (baseUcDTO.getResult().equals(1) || baseUcDTO.getData().size() == 0) {
            return null;
        }
        List<Integer> companyUserIds = new ArrayList<>();
        //设置初始化列表项
        List<UserMonthInfoDTO> userMonthInfoDTOList = new ArrayList<>();
        for (LightUserInfoDTO lightUserInfoDTO : baseUcDTO.getData()) {
            UserMonthInfoDTO userMonthInfoDTO = new UserMonthInfoDTO();
            userMonthInfoDTO.setUserId(lightUserInfoDTO.getId());
            companyUserIds.add(lightUserInfoDTO.getId());
            userMonthInfoDTO.setDepartmentId(lightUserInfoDTO.getDepartmentId());

            if (StringUtils.isNotBlank(lightUserInfoDTO.getName())) {
                userMonthInfoDTO.setName(lightUserInfoDTO.getName());
            }
            if (StringUtils.isNotBlank(lightUserInfoDTO.getDepartment())) {
                userMonthInfoDTO.setDepartmentName(lightUserInfoDTO.getDepartment());
            }
            userMonthInfoDTOList.add(userMonthInfoDTO);
        }
        //查找全部区段的信息
        Map<String, Object> selectMap = new HashMap<>();
        selectMap.put("effectiveDate", effectiveDate);
        selectMap.put("userIds", companyUserIds);
        List<UserMonthInfo> userMonthInfoList = userMonthInfoService.selectByParamsMap(selectMap);
        //检查系统中已查找出的userIds
        for (UserMonthInfoDTO userMonthInfoDTO : userMonthInfoDTOList) {
            for (UserMonthInfo userMonthInfo : userMonthInfoList) {
                if (userMonthInfo.getUserId().equals(userMonthInfoDTO.getUserId())) {
                    userMonthInfoDTO.setAssignedCustomerNum(0);
                    userMonthInfoDTO.setEffectiveCustomerNum(0);
                    userMonthInfoDTO.setBaseCustomerNum(userMonthInfo.getBaseCustomerNum());
                    userMonthInfoDTO.setLastUpdateTime(userMonthInfo.getLastUpdateTime());
                    userMonthInfoDTO.setRewardCustomerNum(userMonthInfo.getRewardCustomerNum());
                    userMonthInfoDTO.setLastUpdateTime(userMonthInfo.getLastUpdateTime());
                    userMonthInfoDTO.setLastUpdateUser(userMonthInfo.getLastUpdateUser());
                    userMonthInfoDTO.setEffectiveDate(userMonthInfo.getEffectiveDate());
                }
            }
        }
        //系统中未设置分配数的业务员，给他们设置初始化的分配数,并且给数据库新增数据
        List<Integer> unSetUserIds = new ArrayList<>();
        List<UserMonthInfo> insertList = new ArrayList<>();
        for (UserMonthInfoDTO userMonthInfoDTO : userMonthInfoDTOList) {
            if (null == userMonthInfoDTO.getBaseCustomerNum()) {
                UserMonthInfo userMonthInfo = new UserMonthInfo();
                userMonthInfoDTO.setBaseCustomerNum(CommonConst.BASE_CUSTOMER_NUM);
                userMonthInfoDTO.setRewardCustomerNum(CommonConst.REWARD_CUSTOMER_NUM);
                userMonthInfoDTO.setLastUpdateTime(new Date());
                userMonthInfoDTO.setEffectiveCustomerNum(0);
                userMonthInfoDTO.setCreateTime(new Date());
                userMonthInfoDTO.setEffectiveDate(effectiveDate);
//                unSetUserIds.add(userMonthInfoDTO.getUserId());
                userMonthInfo.setBaseCustomerNum(CommonConst.BASE_CUSTOMER_NUM);
                userMonthInfo.setAssignedCustomerNum(0);
                userMonthInfo.setEffectiveCustomerNum(0);
                userMonthInfo.setEffectiveDate(effectiveDate);
                userMonthInfo.setRewardCustomerNum(CommonConst.REWARD_CUSTOMER_NUM);
                userMonthInfo.setLastUpdateTime(new Date());
                userMonthInfo.setCreateTime(new Date());
                userMonthInfo.setUserId(userMonthInfoDTO.getUserId());
                userMonthInfo.setCreateUserId(userIdByOption);
                userMonthInfo.setLastUpdateUser(userIdByOption);
                insertList.add(userMonthInfo);
            }
        }
        //初始化用户分配数并且，将初始化数据保存至数据库中
        if (null != insertList && insertList.size() > 0) {
            Integer insertCount = userMonthInfoService.insertList(insertList);
        }
        //获取这些业务员的自动分配数和确认数
        Map<String, Object> allocateMap = new HashMap<>();
        allocateMap.put("newOwnerIds", companyUserIds);
        allocateMap.put("createBeginDate", DateUtils.getBeginDateByStr(effectiveDate));
        allocateMap.put("createEndDate", DateUtils.getEndDateByStr(effectiveDate));
        List<AllocateLog> allocateLogList = allocateLogService.selectByParamsMap(allocateMap);
        //计算业务员的已分配数
        for (AllocateLog allocateLog : allocateLogList) {
            for (UserMonthInfoDTO userMonthInfoDTO : userMonthInfoDTOList) {
                if (null != allocateLog.getNewOwnerId() && allocateLog.getNewOwnerId().equals(userMonthInfoDTO.getUserId())) {
                    userMonthInfoDTO.setAssignedCustomerNum(userMonthInfoDTO.getAssignedCustomerNum() + 1);
                }
            }
        }
        List<Integer> userIds = new ArrayList<>();

        if (allocateLogList.size() > 0) {
            for (int i = 1; i < allocateLogList.size(); i++) {
                userIds.add(allocateLogList.get(i).getNewOwnerId());
            }
        }
        Map<String, Object> userFullSelectMap = new HashMap<>();
        Date date = DateUtils.getBeginDateByStr(effectiveDate);

        userFullSelectMap.put("year", DateUtils.getYear2(date));
        userFullSelectMap.put("month", DateUtils.getMonth(date));
        userFullSelectMap.put("loanAmountMin", 0);
        if (userIds.size()>0) {
            userFullSelectMap.put("inLoanId", userIds);
        }
        List<CustomerUseful> customerUsefulList = customerUsefulService.countByMap(userFullSelectMap);
        //计算业务员的有效分配数
        for (UserMonthInfoDTO userMonthInfoDTO : userMonthInfoDTOList) {
            int effectCountNum = userMonthInfoDTO.getEffectiveCustomerNum();
            for (CustomerUseful customerUseful : customerUsefulList) {
                if (null != customerUseful.getCreateUser() && customerUseful.getCreateUser().equals(userMonthInfoDTO.getUserId())) {
                    effectCountNum++;
                }
            }
            userMonthInfoDTO.setEffectiveCustomerNum(effectCountNum);
        }
        //处理最终返回值
//        List<Map<String, List<UserMonthInfoDTO>>> resultDTO = new ArrayList<>();
        //部门名列表
        List<String> departmentNameList = new ArrayList<>();
        for (UserMonthInfoDTO userMonthInfoDTO : userMonthInfoDTOList) {
            if (StringUtils.isNotBlank(userMonthInfoDTO.getDepartmentName())
                    && !departmentNameList.contains(userMonthInfoDTO.getDepartmentName())) {
                departmentNameList.add(userMonthInfoDTO.getDepartmentName());
            }
        }
        Map<String, List<UserMonthInfoDTO>> departmentMap = new HashMap<>();
        for (String departmentName : departmentNameList) {
            List<UserMonthInfoDTO> singleList = new ArrayList<>();
            for (UserMonthInfoDTO userMonthInfoDTO : userMonthInfoDTOList) {
                if (StringUtils.isNotBlank(userMonthInfoDTO.getDepartmentName())
                        && userMonthInfoDTO.getDepartmentName().equals(departmentName)) {
                    singleList.add(userMonthInfoDTO);
                }
            }
            departmentMap.put(departmentName, singleList);
        }
        return departmentMap;
    }


    public List<PhpDepartmentModel> getSubCompanys(String token, Integer companyId) {
        List<PhpDepartmentModel> phpDepartmentModelList = new ArrayList<PhpDepartmentModel>();
        PhpApiDto<List<PhpDepartmentModel>> phpApiDto = thorService.getSubCompanies(token, null, 1, null, companyId);
        System.out.println(phpApiDto.getRetData());
        phpDepartmentModelList = phpApiDto.getRetData();
        return phpDepartmentModelList;
    }

    /**
     * 获取城市分配队列
     *
     * @param city 城市
     * @return
     */
    public List<Map<String, String>> getAllocateQueue(String city) {
        List<Map<String, String>> allocateQueue = new ArrayList<>();
        String userIdsStr = allocateRedisService.getAllocateTemplet(city);
        if (StringUtils.isBlank(userIdsStr)) {
            return allocateQueue;
        }
        List<Integer> ids = CommonUtil.initStrtoIntegerList(userIdsStr);
        for (Integer userId : ids) {
            Map<String, String> allocateQueueMap = new HashMap<>();
            AppUserDto appUserDto = getUserInfoByField(null, Integer.valueOf(userId), null);
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

    public AppUserDto getUserInfoByField(String telephone, Integer userId, String ownUserName) {
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
