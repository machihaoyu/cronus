package com.fjs.cronus.service.customerallocate.v2;

import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonEnum;
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
import com.fjs.cronus.mappers.UserMonthInfoMapper;
import com.fjs.cronus.model.UserMonthInfo;
import com.fjs.cronus.service.AllocateLogService;
import com.fjs.cronus.service.CompanyMediaQueueService;
import com.fjs.cronus.service.CustomerUsefulService;
import com.fjs.cronus.service.client.ThorService;
import com.fjs.cronus.service.redis.AllocateRedisServiceV2;
import com.fjs.cronus.service.redis.UserInfoRedisService;
import com.fjs.cronus.service.thea.TheaClientService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * Created by yinzf on 2017/10/20.
 */

@Service
public class UserServiceV2 {

    @Value("${token.current}")
    private String publicToken;

//    @Autowired
//    private ThorUcService thorUcService;

    @Autowired
    private ThorService thorService;

    @Autowired
    private RedisConfig redisConfig;

    @Autowired
    private UserMonthInfoServiceV2 userMonthInfoService;

    @Autowired
    private UserInfoRedisService userInfoRedisService;

    @Autowired
    private AllocateLogService allocateLogService;

    @Autowired
    private CustomerUsefulService customerUsefulService;

    @Autowired
    private AllocateRedisServiceV2 allocateRedisService;

//    @Autowired
//    private ConfigRedisService configRedisService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private TheaClientService theaClientService;

    @Autowired
    private UserMonthInfoMapper userMonthInfoMapper;

    @Autowired
    private CompanyMediaQueueService companyMediaQueueService;

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


    public Map<String, List<UserMonthInfoDTO>> getUserMonthInfoList(Integer companyId, String monthFlag, Integer userIdByOption, Integer mediaid) throws Exception {

        // 获取公司员工列表
        ThorApiDTO<List<LightUserInfoDTO>> baseUcDTO = thorService.getUserlistByCompanyId(publicToken, companyId);
        if (baseUcDTO.getResult().equals(1) || baseUcDTO.getData().size() == 0) {
            return null;
        }

        // 校验
        Set<Integer> mediaidAll = this.companyMediaQueueService.findFollowMediaidAll(companyId);
        if (!mediaidAll.contains(mediaid)) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "参数错误，该一级吧未关注此媒体（id="+mediaid+"）");
        }

        // 获取时间
        String effectiveDate = this.allocateRedisService.getMonthStr(monthFlag);

        // 员工ids
        List<Integer> companyUserIds = new ArrayList<>();
        // 设置初始化列表项
        List<UserMonthInfoDTO> userMonthInfoDTOList = new ArrayList<>();
        for (LightUserInfoDTO lightUserInfoDTO : baseUcDTO.getData()) {

            companyUserIds.add(lightUserInfoDTO.getId());

            UserMonthInfoDTO userMonthInfoDTO = new UserMonthInfoDTO();
            userMonthInfoDTO.setUserId(lightUserInfoDTO.getId());
            userMonthInfoDTO.setDepartmentId(lightUserInfoDTO.getDepartmentId());
            if (StringUtils.isNotBlank(lightUserInfoDTO.getName())) userMonthInfoDTO.setName(lightUserInfoDTO.getName());
            if (StringUtils.isNotBlank(lightUserInfoDTO.getDepartment())) userMonthInfoDTO.setDepartmentName(lightUserInfoDTO.getDepartment());
            userMonthInfoDTOList.add(userMonthInfoDTO);
        }

        // 获取员工分配信息
        Example example = new Example(UserMonthInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("effectiveDate", effectiveDate);
        criteria.andIn("userId", companyUserIds);
        criteria.andEqualTo("companyid", companyId);
        criteria.andEqualTo("mediaid", mediaid);
        criteria.andEqualTo("status", CommonEnum.entity_status1.getCode());
        List<UserMonthInfo> userMonthInfoList = userMonthInfoMapper.selectByExample(example);
        userMonthInfoList = CollectionUtils.isEmpty(userMonthInfoList) ? new ArrayList<>() : userMonthInfoList;

        // ===== 下面循环，做2件事 =====
        // 1、db中的数据 ----赋值---> userMonthInfoDTOList
        // 2、未设置值的数据需要入库db
        List<UserMonthInfo> toAddUserMonthInfoList = new ArrayList<>();
        Map<Integer, List<UserMonthInfo>> userIdMappingData = userMonthInfoList.stream().collect(groupingBy(UserMonthInfo::getUserId));
        Date now = new Date();

        for (UserMonthInfoDTO userMonthInfoDTO : userMonthInfoDTOList) {
            List<UserMonthInfo> userMonthInfos = userIdMappingData.get(userMonthInfoDTO.getUserId());
            if (CollectionUtils.isNotEmpty(userMonthInfos) && userMonthInfos.get(0) != null) {
                // db中有，则赋值给vo
                UserMonthInfo userMonthInfo = userMonthInfos.get(0);

                userMonthInfoDTO.setAssignedCustomerNum(userMonthInfo.getAssignedCustomerNum());
                userMonthInfoDTO.setEffectiveCustomerNum(userMonthInfo.getEffectiveCustomerNum());
                userMonthInfoDTO.setBaseCustomerNum(userMonthInfo.getBaseCustomerNum());
                userMonthInfoDTO.setLastUpdateTime(userMonthInfo.getLastUpdateTime());
                userMonthInfoDTO.setRewardCustomerNum(userMonthInfo.getRewardCustomerNum());
                userMonthInfoDTO.setLastUpdateTime(userMonthInfo.getLastUpdateTime());
                userMonthInfoDTO.setLastUpdateUser(userMonthInfo.getLastUpdateUser());
                userMonthInfoDTO.setEffectiveDate(userMonthInfo.getEffectiveDate());

            } else {
                // db中无，则需要db新增，vo设置初始化值
                userMonthInfoDTO.setBaseCustomerNum(0);
                userMonthInfoDTO.setRewardCustomerNum(0);
                userMonthInfoDTO.setAssignedCustomerNum(0);
                userMonthInfoDTO.setEffectiveCustomerNum(0);
                userMonthInfoDTO.setLastUpdateTime(now);
                userMonthInfoDTO.setCreateTime(now);
                userMonthInfoDTO.setEffectiveDate(effectiveDate);

                UserMonthInfo userMonthInfoTemp = new UserMonthInfo();
                userMonthInfoTemp.setBaseCustomerNum(0);
                userMonthInfoTemp.setRewardCustomerNum(0);
                userMonthInfoTemp.setAssignedCustomerNum(0);
                userMonthInfoTemp.setEffectiveCustomerNum(0);
                userMonthInfoTemp.setEffectiveDate(effectiveDate);
                userMonthInfoTemp.setLastUpdateTime(now);
                userMonthInfoTemp.setCreateTime(now);
                userMonthInfoTemp.setUserId(userMonthInfoDTO.getUserId());
                userMonthInfoTemp.setCreateUserId(userIdByOption);
                userMonthInfoTemp.setLastUpdateUser(userIdByOption);
                userMonthInfoTemp.setCompanyid(companyId);
                userMonthInfoTemp.setMediaid(mediaid);
                userMonthInfoTemp.setStatus(CommonEnum.entity_status1.getCode());
                toAddUserMonthInfoList.add(userMonthInfoTemp);
            }
        }

        // 未分配数据数据入库
        if (CollectionUtils.isNotEmpty(toAddUserMonthInfoList)) {
            userMonthInfoService.insertList(toAddUserMonthInfoList);
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
    public List<Map<String, String>> getAllocateQueue(Integer companyid, Integer media, String monthFlag) {

        String monthStr = this.allocateRedisService.getMonthStr(monthFlag);

        List<Map<String, String>> allocateQueue = new ArrayList<>();
        List<Integer> ids = allocateRedisService.finaAllFromQueue(companyid, media, monthStr);
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
