package com.fjs.cronus.controller;

import com.fjs.cronus.Common.CommonMessage;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.cronus.FindCompanyAssignedCustomerNumDTO;
import com.fjs.cronus.dto.cronus.FindCompanyAssignedCustomerNumItmDTO;
import com.fjs.cronus.dto.cronus.FindMediaAssignedCustomerNumDTO;
import com.fjs.cronus.dto.cronus.FindMediaAssignedCustomerNumItmDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.service.UserMonthInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Api(description = "月分配队列-控制器")
@RequestMapping("/api/v1/userMonthInfo")
@RestController
public class UserMonthInfoController {

    private static final Logger logger = LoggerFactory.getLogger(ResignCustomerController.class);

    @Autowired
    private UserMonthInfoService userMonthInfoService;

    @ApiOperation(value = "查询一级吧、某媒体实购数（分配数）", notes = "查询一级吧、某媒体实购数（分配数） api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string"),
            @ApiImplicitParam(name = "params", value = "提交数据,{'list':[{'companyid':1,'sourceid':2,'mediaid':3,'month':'2018-05'}]}", required = true, dataType = "JSONObject"),
    })
    @PostMapping(value = "/findMediaAssignedCustomerNum")
    public CronusDto findMediaAssignedCustomerNum(@RequestHeader(name = "Authorization") String token, @RequestBody FindMediaAssignedCustomerNumDTO params) {
        CronusDto result = new CronusDto();
        try {
            // 参加校验
            if (params == null ||CollectionUtils.isEmpty(params.getList())) {
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

    @ApiOperation(value = "查询一级吧实购数（分配数）", notes = "查询一级吧实购数（分配数） api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string"),
            @ApiImplicitParam(name = "params", value = "提交数据,{'list':[{'companyid':1,'month':'2018-05'}]}", required = true, dataType = "JSONObject"),
    })
    @PostMapping(value = "/findCompanyAssignedCustomerNum")
    public CronusDto findAssignedCustomerNum(@RequestHeader(name = "Authorization") String token, @RequestBody FindCompanyAssignedCustomerNumDTO params) {
        CronusDto result = new CronusDto();
        try {
            // 参加校验
            if (params == null ||CollectionUtils.isEmpty(params.getList())) {
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


}
