package com.fjs.cronus.controller;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonMessage;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.api.PHPLoginDto;
import com.fjs.cronus.dto.api.uc.CityDto;
import com.fjs.cronus.dto.cronus.PanParamDTO;
import com.fjs.cronus.dto.cronus.RedisSubUserInfoDTO;
import com.fjs.cronus.dto.uc.CronusSubInfoDTO;
import com.fjs.cronus.dto.uc.UserInfoDTO;
import com.fjs.cronus.dto.cronus.CustomerListDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.service.CustomerInfoService;
import com.fjs.cronus.service.PanService;
import com.fjs.cronus.service.redis.CronusRedisService;
import com.fjs.cronus.service.thea.TheaClientService;
import com.fjs.cronus.service.uc.UcService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by msi on 2017/11/30.
 */
@Controller
@Api(description = "公盘客户控制器")
@RequestMapping("/api/v1")
public class PublicOfferController {


    private static final Logger logger = LoggerFactory.getLogger(PublicOfferController.class);
    @Autowired
    UcService ucService;
    @Autowired
    TheaClientService theaClientService;
    @Autowired
    CustomerInfoService customerInfoService;
    @Autowired
    PanService panService;
    @Autowired
    CronusRedisService cronusRedisService;

    @ApiOperation(value = "获取公盘列表", notes = "获取公盘列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerName", value = "客户姓名", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "telephonenumber", value = "手机号", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "utmSource", value = "渠道来源", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "houseStatus", value = "有 无房产", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "customerClassify", value = "跟进状态(暂未接通 无意向 有意向待跟踪 资质差无法操作 空号 外地 同行 内部员工 其他)", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "customerSource", value = "客户来源", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "mountLevle", value = "1：0-20万，2：20-50万，3:50-100万，4:100-500万，5：大于五百万 ", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "city", value = "城市", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "page", value = "查询第几页", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "显示多少", required = false, paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/Offerlist", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<QueryResult<CustomerListDTO>> Offerlist(@RequestParam(required = false) String customerName,
                                                             @RequestParam(required = false) String telephonenumber,
                                                             @RequestParam(required = false) String utmSource,
                                                             @RequestParam(required = false) String houseStatus,
                                                             @RequestParam(required = false) String customerClassify,
                                                             @RequestParam(required = false) String customerSource,
                                                             @RequestParam(required = false) String city,
                                                             @RequestParam(required = false) Integer mountLevle,
                                                             @RequestParam(required = false) Integer page,
                                                             @RequestParam(required = false) Integer size,
                                                             @RequestHeader("Authorization") String token) {

        CronusDto<QueryResult<CustomerListDTO>> cronusDto = new CronusDto<>();
        List<Integer> subCompanyIds = null;//自己能管理的分公司
        List<String> canMangerMainCity = null;//自己能管理的城市
        QueryResult<CustomerListDTO> queryResult = null;
        //获取配置项
        try {
            //从token中获取用户信息//添加缓存
            PanParamDTO pan = new PanParamDTO();
            UserInfoDTO userInfoDTO = ucService.getUserIdByToken(token, CommonConst.SYSTEMNAME);
            List<String> paramsList = new ArrayList<>();
            List<String> utmList = new ArrayList<>();
            Integer userId = null;
            Integer companyId = null;
            if (StringUtils.isNotEmpty(userInfoDTO.getUser_id())) {
                userId = Integer.parseInt(userInfoDTO.getUser_id());
            }
            //公盘需要踢出三处客户以及过滤掉特殊渠道的
            String result = theaClientService.findValueByName(token, CommonConst.SPECIAL_UTM_SOURCE);
            JSONObject jsonObject = JSONObject.parseObject(result);
            Map<String, String> valueMap = jsonObject.toJavaObject(Map.class);
            for (String str : valueMap.values()) {
                utmList.add(str);
            }
            //需要踢出三无客户盘的
            String unableResult = theaClientService.findValueByName(token, CommonConst.CAN_NOT_ALLOCATE_CUSTOMER_CLASSIFY);
            if (unableResult != null && !"".equals(unableResult)) {
                String[] strArray = null;
                strArray = unableResult.split(",");
                for (int i = 0; i < strArray.length; i++) {
                    paramsList.add(strArray[i]);
                }
            } else {
                throw new CronusException(CronusException.Type.MESSAGE_CONNECTTHEASYSTEM_ERROR);
            }
            if (StringUtils.isNotEmpty(utmSource)) {
                pan.setUtmSource(utmSource);
            }
            //从缓存取得
            RedisSubUserInfoDTO redisSubUserInfoDTO = cronusRedisService.getRedisSubUserInfo(CommonConst.CANMANGERMAINCITY + userId);
            if (redisSubUserInfoDTO != null) {
                subCompanyIds = redisSubUserInfoDTO.getSubCompanyId();
                canMangerMainCity = redisSubUserInfoDTO.getCanMangerMainCity();
            } else {
                canMangerMainCity = new ArrayList<>();
                subCompanyIds = new ArrayList<>();
                String mainCity = theaClientService.findValueByName(token, CommonConst.MAIN_CITY);
                //获取异地城市
                String remoteCity = theaClientService.findValueByName(token, CommonConst.REMOTE_CITY);
                List<CronusSubInfoDTO> cronusSubInfoDTOS = ucService.getSubCompanyToCronus(token, userId, CommonConst.SYSTEM_NAME_ENGLISH);
                if (cronusSubInfoDTOS != null && cronusSubInfoDTOS.size() > 0) {
                    for (CronusSubInfoDTO cronusSubInfoDTO : cronusSubInfoDTOS) {
                        if (!StringUtils.isEmpty(cronusSubInfoDTO.getCityName())) {
                            if (mainCity.contains(cronusSubInfoDTO.getCityName())) {//说明在主要城市内
                                if (!canMangerMainCity.contains(cronusSubInfoDTO.getCityName())) {
                                    canMangerMainCity.add(cronusSubInfoDTO.getCityName());
                                }
                            }
                            if (remoteCity.contains(cronusSubInfoDTO.getCityName())) {//说明是异地城市
                                if (!subCompanyIds.contains(cronusSubInfoDTO.getCityName())) {
                                    subCompanyIds.add(Integer.valueOf(cronusSubInfoDTO.getSubCompanyId()));
                                }
                            }
                        }
                    }
                    RedisSubUserInfoDTO redis = new RedisSubUserInfoDTO();
                    redis.setCanMangerMainCity(canMangerMainCity);
                    redis.setSubCompanyId(subCompanyIds);
                    cronusRedisService.setRedisSubUserInfo(CommonConst.CANMANGERMAINCITY + userId, redis);
                }
            }
            pan.setCustomerName(customerName);
            pan.setTelephonenumber(telephonenumber);
            pan.setHouseStatus(houseStatus);
            pan.setCustomerClassify(customerClassify);
            pan.setCustomerSource(customerSource);
            pan.setCity(city);
            queryResult = panService.listByOffer(pan, userId, companyId, token, CommonConst.SYSTEMNAME, page, size, canMangerMainCity, subCompanyIds, null, mountLevle, utmList, paramsList);
            cronusDto.setData(queryResult);
            cronusDto.setResult(CommonMessage.SUCCESS.getCode());
            cronusDto.setMessage(CommonMessage.SUCCESS.getCodeDesc());
        } catch (Exception e) {
            logger.error("--------------->Offerlist客户信息操作失败", e);
            if (e instanceof CronusException) {
                CronusException cronusException = (CronusException) e;
                throw cronusException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
        return cronusDto;
    }

    @ApiOperation(value = "公盘领取", notes = "公盘领取")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerId", value = "客户id", required = true, paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/pullPan", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto pullPan(@RequestParam(required = true) Integer customerId, HttpServletRequest request) {
        CronusDto theaApiDTO = new CronusDto();
        String token = request.getHeader("Authorization");
        PHPLoginDto resultDto = ucService.getAllUserInfo(token, CommonConst.SYSTEMNAME);
        String[] authority = resultDto.getAuthority();
        if (authority.length > 0) {
            List<String> authList = Arrays.asList(authority);
            if (authList.contains(CommonConst.PULL_LOAN_URL)) {
                theaApiDTO.setResult(CommonMessage.PULL_FAIL.getCode());
                theaApiDTO.setMessage(CommonConst.NO_AUTHORIZE);
                return theaApiDTO;
            }
        }
        UserInfoDTO userInfoDTO = resultDto.getUser_info();
        CustomerInfo customerInfo = null;
        try {
            Integer userId = null;
            if (StringUtils.isNotEmpty(userInfoDTO.getUser_id())) {
                userId = Integer.parseInt(userInfoDTO.getUser_id());
            }
            //领取操作
            boolean updateResult = panService.pullPan(customerId, userId, token, userInfoDTO.getName());
            if (updateResult == true) {
                theaApiDTO.setResult(CommonMessage.PULL_SUCCESS.getCode());
                theaApiDTO.setMessage(CommonMessage.PULL_SUCCESS.getCodeDesc());
            } else {
                logger.error("-------------->pullPan领取失败");
                theaApiDTO.setResult(CommonMessage.PULL_FAIL.getCode());
                theaApiDTO.setMessage(CommonMessage.PULL_FAIL.getCodeDesc());
                throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
            }
        } catch (Exception e) {
            if (e instanceof CronusException) {
                CronusException cronusException = (CronusException) e;
                throw cronusException;
            }
            logger.error("-------------->pullPan领取失败", e);
            theaApiDTO.setResult(CommonMessage.PULL_FAIL.getCode());
            theaApiDTO.setMessage(CommonMessage.PULL_FAIL.getCodeDesc());
        }
        return theaApiDTO;
    }

    @ApiOperation(value = "获取特殊公盘列表", notes = "获取特殊公盘列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerName", value = "客户姓名", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "telephonenumber", value = "手机号", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "utmSource", value = "渠道来源(除外地公盘外的必须传)", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "houseStatus", value = "有 无房产", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "customerClassify", value = "跟进状态(暂未接通 无意向 有意向待跟踪 资质差无法操作 空号 外地 同行 内部员工 其他)", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "customerSource", value = "客户来源", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "mountLevle", value = "1：0-20万，2：20-50万，3:50-100万，4:100-500万，5：大于五百万 ", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "city", value = "城市", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "type", value = "公盘类型（外地公盘填1）", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "page", value = "查询第几页", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "显示多少", required = false, paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/specialOfferlist", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<QueryResult<CustomerListDTO>> specialOfferlist(@RequestParam(required = false) String customerName,
                                                                    @RequestParam(required = false) String telephonenumber,
                                                                    @RequestParam(required = false) String utmSource,
                                                                    @RequestParam(required = false) String houseStatus,
                                                                    @RequestParam(required = false) String customerClassify,
                                                                    @RequestParam(required = false) String customerSource,
                                                                    @RequestParam(required = false) String city,
                                                                    @RequestParam(required = false) Integer mountLevle,
                                                                    @RequestParam(required = false) Integer type,
                                                                    @RequestParam(required = false) Integer page,
                                                                    @RequestParam(required = false) Integer size,
                                                                    @RequestHeader("Authorization") String token) {

        CronusDto<QueryResult<CustomerListDTO>> cronusDto = new CronusDto<>();

        QueryResult<CustomerListDTO> queryResult = null;
        //获取配置项
        try {
            //从token中获取用户信息
            PanParamDTO pan = new PanParamDTO();
            UserInfoDTO userInfoDTO = ucService.getUserIdByToken(token, CommonConst.SYSTEMNAME);
            List<String> paramsList = new ArrayList<>();
            List<String> utmList = new ArrayList<>();
            Integer userId = null;
            Integer companyId = null;
            if (StringUtils.isNotEmpty(userInfoDTO.getUser_id())) {
                userId = Integer.parseInt(userInfoDTO.getUser_id());
            }
            List<String> mainCitys = new ArrayList<String>();//主要城市
            if (type == null) {
                String result = theaClientService.findValueByName(token, CommonConst.SPECIAL_UTM_SOURCE);
                JSONObject jsonObject = JSONObject.parseObject(result);
                String specUtmSource = jsonObject.getString(utmSource);
                pan.setUtmSource(specUtmSource);
                List<CityDto> subsCitys = ucService.getSubcompanyByUserId(token, userId, CommonConst.SYSTEMNAME);
                List<String> subCitys = new ArrayList<String>();
                if (!CollectionUtils.isEmpty(subsCitys)) {
                    for (CityDto cityDto : subsCitys) {
                        if (StringUtils.isNotEmpty(cityDto.getName())) {
                            mainCitys.add(cityDto.getName());
                        }
                    }
                }

            } else {
                //公盘需要踢出三处客户以及过滤掉特殊渠道的
                String result = theaClientService.findValueByName(token, CommonConst.SPECIAL_UTM_SOURCE);
                JSONObject jsonObject = JSONObject.parseObject(result);
                Map<String, String> valueMap = jsonObject.toJavaObject(Map.class);
                for (String str : valueMap.values()) {
                    utmList.add(str);
                }
                //需要踢出三无客户盘的
                String unableResult = theaClientService.findValueByName(token, CommonConst.CAN_NOT_ALLOCATE_CUSTOMER_CLASSIFY);
                if (unableResult != null && !"".equals(unableResult)) {
                    String[] strArray = null;
                    strArray = unableResult.split(",");
                    for (int i = 0; i < strArray.length; i++) {
                        paramsList.add(strArray[i]);
                    }
                } else {
                    throw new CronusException(CronusException.Type.MESSAGE_CONNECTTHEASYSTEM_ERROR);
                }
                if (StringUtils.isNotEmpty(utmSource)) {
                    pan.setUtmSource(utmSource);
                }
                //获取下属的城市
                String mainCity = theaClientService.findValueByName(token, CommonConst.MAIN_CITY);
                //获取异地城市
                String remoteCity = theaClientService.findValueByName(token, CommonConst.REMOTE_CITY);
                //主要城市
                String[] strArray = null;
                strArray = mainCity.split(",");
                for (int i = 0; i < strArray.length; i++) {
                    mainCitys.add(strArray[i]);
                }
                //异地城市
                String[] remoteArray = null;
                remoteArray = remoteCity.split(",");
                for (int i = 0; i < remoteArray.length; i++) {
                    mainCitys.add(remoteArray[i]);
                }

            }
            pan.setCustomerName(customerName);
            pan.setTelephonenumber(telephonenumber);
            pan.setHouseStatus(houseStatus);
            pan.setCustomerClassify(customerClassify);
            pan.setCustomerSource(customerSource);
            pan.setCity(city);
            queryResult = panService.specialListByOffer(pan, userId, companyId, token, CommonConst.SYSTEMNAME, page, size, mainCitys, null, type, mountLevle, utmList, paramsList);
            cronusDto.setData(queryResult);
            cronusDto.setResult(CommonMessage.SUCCESS.getCode());
            cronusDto.setMessage(CommonMessage.SUCCESS.getCodeDesc());
        } catch (Exception e) {
            logger.error("--------------->Offerlist客户信息操作失败", e);
            if (e instanceof CronusException) {
                CronusException cronusException = (CronusException) e;
                throw cronusException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
        return cronusDto;
    }
}
