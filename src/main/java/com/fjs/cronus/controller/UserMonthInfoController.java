package com.fjs.cronus.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonMessage;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.avatar.AvatarApiDTO;
import com.fjs.cronus.dto.avatar.FirstBarConsumeDTO;
import com.fjs.cronus.dto.avatar.FirstBarConsumeDTO2;
import com.fjs.cronus.dto.avatar.FirstBarDTO;
import com.fjs.cronus.dto.cronus.FindCompanyAssignedCustomerNumDTO;
import com.fjs.cronus.dto.cronus.FindCompanyAssignedCustomerNumItmDTO;
import com.fjs.cronus.dto.cronus.FindMediaAssignedCustomerNumDTO;
import com.fjs.cronus.dto.cronus.FindMediaAssignedCustomerNumItmDTO;
import com.fjs.cronus.dto.uc.UserInfoDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.service.UserMonthInfoService;
import com.fjs.cronus.service.client.AvatarClientService;
import com.fjs.cronus.service.client.ThorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Api(description = "月分配队列-控制器")
@RequestMapping("/api/v1/userMonthInfo")
@RestController
public class UserMonthInfoController {

    private static final Logger logger = LoggerFactory.getLogger(ResignCustomerController.class);

    @Autowired
    private UserMonthInfoService userMonthInfoService;

    @Autowired
    private ThorService thorService;

    @Autowired
    private AvatarClientService avatarClientService;

    @Deprecated
    @ApiOperation(value = "商机系统：查询一级吧、某媒体实购数（分配数）", notes = "查询一级吧、某媒体实购数（分配数） api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string"),
            @ApiImplicitParam(name = "params", value = "提交数据,{'list':[{'companyid':1,'sourceid':2,'mediaid':3,'month':'2018-05'}]}", required = true, dataType = "JSONObject"),
    })
    @PostMapping(value = "/findMediaAssignedCustomerNum")
    public CronusDto findMediaAssignedCustomerNum(@RequestHeader(name = "Authorization") String token, @RequestBody FindMediaAssignedCustomerNumDTO params) {
        CronusDto result = new CronusDto();
        try {
            // 参加校验
            if (params == null || CollectionUtils.isEmpty(params.getList())) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "参数不能为 空");
            }
            List<FindMediaAssignedCustomerNumItmDTO> list = params.getList();
            for (FindMediaAssignedCustomerNumItmDTO temp : list) {
                if (temp == null) {
                    throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "list内数据不能为空");
                }
                if (temp.getCompanyid() == null) {
                    throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "list内数据 Companyid 不能为空");
                }
                if (temp.getSourceid() == null) {
                    throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "list内数据 Sourceid 不能为空");
                }
                if (temp.getMediaid() == null) {
                    throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "list内数据 Mediaid 不能为空");
                }
                if (StringUtils.isBlank(temp.getMonth())) {
                    throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "list内数据 Month 不能为空");
                } else {
                    // 转换成crm这边格式yyyyMM
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMM");
                    Date parse = sdf.parse(temp.getMonth());
                    temp.setMonth(sdf2.format(parse));
                }
            }

            result.setData(userMonthInfoService.findAssignedCustomerNum(params));
            result.setResult(CommonMessage.SUCCESS.getCode());
        } catch (Exception e) {
            if (e instanceof CronusException) {
                // 已知异常
                CronusException temp = (CronusException) e;
                result.setResult(Integer.valueOf(temp.getResponseError().getStatus()));
                result.setMessage(temp.getResponseError().getMessage());
            } else {
                // 未知异常
                logger.error("查询一级吧、某月、某来源、某媒体实购数（分配数）:", e);
                result.setResult(CommonMessage.FAIL.getCode());
                result.setMessage(e.getMessage());
            }
        }
        return result;
    }

    @Deprecated
    @ApiOperation(value = "商机系统：查询一级吧实购数（分配数）", notes = "查询一级吧实购数（分配数） api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string"),
            @ApiImplicitParam(name = "params", value = "提交数据,{'list':[{'companyid':1,'month':'2018-05'}]}", required = true, dataType = "JSONObject"),
    })
    @PostMapping(value = "/findCompanyAssignedCustomerNum")
    public CronusDto findAssignedCustomerNum(@RequestHeader(name = "Authorization") String token, @RequestBody FindCompanyAssignedCustomerNumDTO params) {
        CronusDto result = new CronusDto();
        try {
            // 参加校验
            if (params == null || CollectionUtils.isEmpty(params.getList())) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "参数不能为 空");
            }
            List<FindCompanyAssignedCustomerNumItmDTO> list = params.getList();
            for (FindCompanyAssignedCustomerNumItmDTO temp : list) {
                if (temp == null) {
                    throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "list内数据不能为空");
                }
                if (temp.getCompanyid() == null) {
                    throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "list内数据 Companyid 不能为空");
                }
                if (StringUtils.isBlank(temp.getMonth())) {
                    throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "list内数据 Month 不能为空");
                } else {
                    // 转换成crm这边格式yyyyMM
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMM");
                    Date parse = sdf.parse(temp.getMonth());
                    temp.setMonth(sdf2.format(parse));
                }
            }

            result.setData(userMonthInfoService.findAssignedCustomerNum(params));
            result.setResult(CommonMessage.SUCCESS.getCode());
        } catch (Exception e) {
            if (e instanceof CronusException) {
                // 已知异常
                CronusException temp = (CronusException) e;
                result.setResult(Integer.valueOf(temp.getResponseError().getStatus()));
                result.setMessage(temp.getResponseError().getMessage());
            } else {
                // 未知异常
                logger.error("查询一级吧、某月、某来源、某媒体实购数（分配数）:", e);
                result.setResult(CommonMessage.FAIL.getCode());
                result.setMessage(e.getMessage());
            }
        }
        return result;
    }

    @ApiOperation(value = "获取登录用户一级吧", notes = "获取登录用户一级吧 api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string"),
    })
    @PostMapping(value = "/findSubCompany")
    public CronusDto findSubCompany(@RequestHeader(name = "Authorization") String token) {
        CronusDto result = new CronusDto();
        try {
            CronusDto<UserInfoDTO> cronusDto = thorService.getUserInfoByToken(token, null);
            AvatarApiDTO<List<FirstBarDTO>> allSubCompany = avatarClientService.findAllSubCompany(token);

            Map<String, Object> resultMap = new HashMap<>();
            if (cronusDto != null
                    && cronusDto.getResult() == 0
                    && cronusDto.getData() != null
                    && allSubCompany != null
                    && allSubCompany.getResult() == 0
                    && CollectionUtils.isNotEmpty(allSubCompany.getData())
                    ) {

                List<FirstBarDTO> data = allSubCompany.getData();
                UserInfoDTO data1 = cronusDto.getData();

                for (FirstBarDTO datum : data) {
                    if (datum != null && datum.getId() != null && datum.getId().toString().equals(data1.getSub_company_id())) {
                        resultMap.put("id", datum.getId());
                        resultMap.put("firstBar", datum.getFirstBar());
                    }
                }

            }

            result.setData(resultMap);
            result.setResult(CommonMessage.SUCCESS.getCode());
        } catch (Exception e) {
            if (e instanceof CronusException) {
                // 已知异常
                CronusException temp = (CronusException) e;
                result.setResult(Integer.valueOf(temp.getResponseError().getStatus()));
                result.setMessage(temp.getResponseError().getMessage());
            } else {
                // 未知异常
                logger.error("获取登录用户一级吧:", e);
                result.setResult(CommonMessage.FAIL.getCode());
                result.setMessage(e.getMessage());
            }
        }
        return result;
    }

    @ApiOperation(value = "总分配队列获取一级吧各媒体（除去总分配队列）月分配数详情", notes = "总分配队列获取一级吧各媒体（除去总分配队列）月分配数详情 api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string"),
            @ApiImplicitParam(name = "params", value = "提交数据,{" +
                    "\"monthFlag\":\"" + CommonConst.USER_MONTH_INFO_MONTH_CURRENT + "、" + CommonConst.USER_MONTH_INFO_MONTH_NEXT + "\"," +
                    "\"companyid\":123," +
                    "\"mediaid\":123," +
                    "\"salemanid\":123," +
                    "}", required = true, dataType = "JSONObject"),
    })
    @PostMapping(value = "/findMonthAllocateData")
    public CronusDto findMonthAllocateData(@RequestHeader(name = "Authorization") String token, @RequestBody JSONObject params) {
        CronusDto result = new CronusDto();
        try {

            String monthFlag = params.getString("monthFlag");
            Integer companyid = params.getInteger("companyid");
            Integer mediaid = params.getInteger("mediaid");
            Integer salemanid = params.getInteger("salemanid");

            if (companyid == null) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "companyid 不能为空");
            }
            if (mediaid == null) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "mediaid 不能为空");
            }
            if (salemanid == null) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "salemanid 不能为空");
            }

            List<Map<String, Object>> resultdata = userMonthInfoService.findMonthAllocateData(monthFlag, companyid, mediaid, salemanid, token);
            result.setData(resultdata);
            result.setResult(CommonMessage.SUCCESS.getCode());
        } catch (Exception e) {
            if (e instanceof CronusException) {
                // 已知异常
                CronusException temp = (CronusException) e;
                result.setResult(Integer.valueOf(temp.getResponseError().getStatus()));
                result.setMessage(temp.getResponseError().getMessage());
            } else {
                // 未知异常
                logger.error("总分配队列获取一级吧各媒体（除去总分配队列）月分配数详情:", e);
                result.setResult(CommonMessage.FAIL.getCode());
                result.setMessage(e.getMessage());
            }
        }
        return result;
    }

    @ApiOperation(value = "队列获取一级吧媒体已分配数详情", notes = "队列获取一级吧媒体已分配数详情 api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string"),
            @ApiImplicitParam(name = "params", value = "提交数据,{" +
                    "\"monthFlag\":\"" + CommonConst.USER_MONTH_INFO_MONTH_CURRENT + "、" + CommonConst.USER_MONTH_INFO_MONTH_NEXT + "\"," +
                    "\"companyid\":123," +
                    "\"mediaid\":123," +
                    "\"salemanid\":123," +
                    "}", required = true, dataType = "JSONObject"),
    })
    @PostMapping(value = "/findAllocateData")
    public CronusDto findAllocateData(@RequestHeader(name = "Authorization") String token, @RequestBody JSONObject params) {
        CronusDto result = new CronusDto();
        try {

            String monthFlag = params.getString("monthFlag");
            Integer companyid = params.getInteger("companyid");
            Integer mediaid = params.getInteger("mediaid");
            Integer salemanid = params.getInteger("salemanid");

            if (companyid == null) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "companyid 不能为空");
            }
            if (mediaid == null) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "mediaid 不能为空");
            }
            if (salemanid == null) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "salemanid 不能为空");
            }

            List<Map<String, Object>> resultdata = userMonthInfoService.findAllocateData(monthFlag, companyid, mediaid, salemanid, token);
            result.setData(resultdata);
            result.setResult(CommonMessage.SUCCESS.getCode());
        } catch (Exception e) {
            if (e instanceof CronusException) {
                // 已知异常
                CronusException temp = (CronusException) e;
                result.setResult(Integer.valueOf(temp.getResponseError().getStatus()));
                result.setMessage(temp.getResponseError().getMessage());
            } else {
                // 未知异常
                logger.error("获取一级吧媒体已分配数详情:", e);
                result.setResult(CommonMessage.FAIL.getCode());
                result.setMessage(e.getMessage());
            }
        }
        return result;
    }

    @ApiOperation(value = "商机系统：获取某月所有一级巴的实购数量", notes = "商机系统：获取某月所有一级巴的实购数量 api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string"),
            @ApiImplicitParam(name = "params", value = "提交数据,{\"mothstr\":\"2018-05\",\"companyids\":[1,2,3,4]}", required = true, dataType = "JSONObject"),
    })
    @PostMapping(value = "/findAllocateDataByMonthAndCompanyids")
    public CronusDto findAllocateDataByMonthAndCompanyids(@RequestHeader(name = "Authorization") String token, @RequestBody JSONObject params) {
        CronusDto result = new CronusDto();
        try {

            String mothstr = params.getString("mothstr");
            JSONArray companyids = params.getJSONArray("companyids");

            if (CollectionUtils.isEmpty(companyids)) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "companyid 不能为空");
            }
            if (StringUtils.isBlank(mothstr)) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "mothstr 不能为空");
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            Date parse = sdf.parse(mothstr);
            if (parse == null) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "mothstr 时间格式错误");
            }
            List<Integer> integers = companyids.toJavaList(Integer.class);

            List<Map<String, Object>> resultdata = userMonthInfoService.findAllocateDataByMonthAndCompanyids(parse, new HashSet<>(integers), token);
            result.setData(resultdata);
            result.setResult(CommonMessage.SUCCESS.getCode());
        } catch (Exception e) {
            if (e instanceof CronusException) {
                // 已知异常
                CronusException temp = (CronusException) e;
                result.setResult(Integer.valueOf(temp.getResponseError().getStatus()));
                result.setMessage(temp.getResponseError().getMessage());
            } else {
                // 未知异常
                logger.error("商机系统：获取某月所有一级巴的实购数量:", e);
                result.setResult(CommonMessage.FAIL.getCode());
                result.setMessage(e.getMessage());
            }
        }
        return result;
    }

    @ApiOperation(value = "商机系统：获取某月一级巴下面所有的实购数(来源媒体实购数)", notes = "商机系统：获取某月一级巴下面所有的实购数(来源媒体实购数) api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string"),
            @ApiImplicitParam(name = "params", value = "提交数据,{\"mothstr\":\"2018-05\",\"companyid\":123}", required = true, dataType = "JSONObject"),
    })
    @PostMapping(value = "/findAllocateDataByMonthAndCompanyid")
    public CronusDto findAllocateDataByMonthAndCompanyid(@RequestHeader(name = "Authorization") String token, @RequestBody JSONObject params) {
        CronusDto result = new CronusDto();
        try {

            String mothstr = params.getString("mothstr");
            Integer companyid = params.getInteger("companyid");

            if (companyid == null) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "companyid 不能为空");
            }
            if (StringUtils.isBlank(mothstr)) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "mothstr 不能为空");
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            Date parse = sdf.parse(mothstr);
            if (parse == null) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "mothstr 时间格式错误");
            }

            List<Map<String, Object>> resultdata = userMonthInfoService.findAllocateDataByMonthAndCompanyid(parse, companyid, token);
            result.setData(resultdata);
            result.setResult(CommonMessage.SUCCESS.getCode());
        } catch (Exception e) {
            if (e instanceof CronusException) {
                // 已知异常
                CronusException temp = (CronusException) e;
                result.setResult(Integer.valueOf(temp.getResponseError().getStatus()));
                result.setMessage(temp.getResponseError().getMessage());
            } else {
                // 未知异常
                logger.error("商机系统：获取某月一级巴下面所有的实购数(来源媒体实购数):", e);
                result.setResult(CommonMessage.FAIL.getCode());
                result.setMessage(e.getMessage());
            }
        }
        return result;
    }

    @ApiOperation(value = "商机系统：根据时间(精确到日),来源媒体,查出所有一级巴的实购数", notes = "商机系统：根据时间(精确到日),来源媒体,查出所有一级巴的实购数 api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string"),
            @ApiImplicitParam(name = "params", value = "提交数据,{\"starttime\":时间戳,\"endstart\":时间戳,\"mediaid\":123}", required = true, dataType = "JSONObject"),
    })
    @PostMapping(value = "/findAllocateDataByTimAndMedia")
    public CronusDto findAllocateDataByTimAndMedia(@RequestHeader(name = "Authorization") String token, @RequestBody JSONArray params) {
        CronusDto result = new CronusDto();
        try {

            List<FirstBarConsumeDTO> list = params.toJavaList(FirstBarConsumeDTO.class);
            if (CollectionUtils.isEmpty(list)) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "list 不能为空");
            }
            System.out.println(list);

            // 时间戳转时间对象
            List<FirstBarConsumeDTO2> list2 = new ArrayList<>(list.size());
            for (FirstBarConsumeDTO item : list) {
                FirstBarConsumeDTO2 e = new FirstBarConsumeDTO2();
                e.setFirstBarId(item.getFirstBarId());
                e.setMedia(item.getMedia());
                e.setEndTimeParse(new Date(item.getEndTime()));
                e.setStartTimeParse(new Date(item.getStartTime()));
                list2.add(e);
            }

            List<FirstBarConsumeDTO> allocateDataByTimAndMedia = userMonthInfoService.findAllocateDataByTimAndMedia(list2);
            Map<String, Integer> collect = allocateDataByTimAndMedia.stream().collect(Collectors.toMap((i) -> {
                        return i.getFirstBarId() + "$" + i.getMedia();
                    }
                    , FirstBarConsumeDTO::getAllocate
                    , (x, y) -> x)
            );

            for (FirstBarConsumeDTO item : list) {
                String s = item.getFirstBarId() + "$" + item.getMedia();
                Integer integer = collect.get(s);
                item.setAllocate(integer == null ? 0 : integer);
            }

            result.setData(list);
            result.setResult(CommonMessage.SUCCESS.getCode());
        } catch (Exception e) {
            if (e instanceof CronusException) {
                // 已知异常
                CronusException temp = (CronusException) e;
                result.setResult(Integer.valueOf(temp.getResponseError().getStatus()));
                result.setMessage(temp.getResponseError().getMessage());
            } else {
                // 未知异常
                logger.error("商机系统：根据时间(精确到日),来源媒体,查出所有一级巴的实购数:", e);
                result.setResult(CommonMessage.FAIL.getCode());
                result.setMessage(e.getMessage());
            }
        }
        return result;
    }

    @ApiOperation(value = "商机系统：查看分配log", notes = "商机系统：查看分配log api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string"),
            //@ApiImplicitParam(name = "params", value = "提交数据,{\"starttime\":时间戳,\"endstart\":时间戳,\"mediaid\":123}", required = true, dataType = "JSONObject"),
    })
    @PostMapping(value = "/findAllocatelog")
    public CronusDto findAllocatelog(@RequestHeader(name = "Authorization") String token) {
        CronusDto result = new CronusDto();
        try {

            List<Map<Integer, Object>> resultdata = userMonthInfoService.findAllocatelog(1, 0);
            result.setData(resultdata);
            result.setResult(CommonMessage.SUCCESS.getCode());
        } catch (Exception e) {
            if (e instanceof CronusException) {
                // 已知异常
                CronusException temp = (CronusException) e;
                result.setResult(Integer.valueOf(temp.getResponseError().getStatus()));
                result.setMessage(temp.getResponseError().getMessage());
            } else {
                // 未知异常
                logger.error("商机系统：查看分配log:", e);
                result.setResult(CommonMessage.FAIL.getCode());
                result.setMessage(e.getMessage());
            }
        }
        return result;
    }


}
