package com.fjs.cronus.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonMessage;
import com.fjs.cronus.api.thea.Config;
import com.fjs.cronus.dto.*;
import com.fjs.cronus.dto.cronus.BaseUcDTO;
import com.fjs.cronus.dto.cronus.UcUserDTO;
import com.fjs.cronus.dto.loan.TheaApiDTO;
import com.fjs.cronus.dto.uc.UserInfoDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.model.AutoCleanManage;
import com.fjs.cronus.service.AutoCleanManageService;
import com.fjs.cronus.service.AutoCleanService;
import com.fjs.cronus.service.CleanMangerService;
import com.fjs.cronus.service.client.TheaService;
import com.fjs.cronus.service.client.ThorInterfaceService;
import com.fjs.cronus.util.FastJsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by yinzf on 2017/11/21.
 */
@Controller
@Api(description = "自动清洗控制器")
@RequestMapping("/api/cleanManger/v1")
public class CleanMangerController {
    private  static  final Logger logger = LoggerFactory.getLogger(CleanMangerController.class);

    @Autowired
    private AutoCleanManageService autoCleanManageService;
    @Autowired
    private CleanMangerService cleanMangerService;
    @Autowired
    private ThorInterfaceService thorUcService;
    @Autowired
    private TheaService theaService;
    @Autowired
    private AutoCleanService autoCleanService;

    /*@ApiOperation(value="添加需要屏蔽的公司", notes="添加需要屏蔽的公司")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
    })
    @RequestMapping(value = "/addCompany", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto addCompany(@Valid @RequestBody CleanMangerCompanyDTO cleanMangerCompanyDTO, BindingResult result, HttpServletRequest request){
        logger.info("添加需要屏蔽公司的数据：" + cleanMangerCompanyDTO.toString());
        CronusDto theaApiDTO = new CronusDto();
        if(result.hasErrors()){
            throw new CronusException(CronusException.Type.THEA_SYSTEM_ERROR);
        }
        try{
            //分公司id不能为空
            if (cleanMangerCompanyDTO.getSubCompanyId() == null){
                theaApiDTO.setResult(CommonMessage.ADD_FAIL.getCode());
                theaApiDTO.setMessage(CommonConst.SUB_COMPANY_ID_NULL);
                return theaApiDTO;
            }
            String token=request.getHeader("Authorization");
            CronusDto<UserInfoDTO> thorApiDTO=thorUcService.getUserInfoByToken(token,CommonConst.SYSTEMNAME);
            UserInfoDTO userInfoDTO=thorApiDTO.getData();
            //获取屏蔽的公司
            TheaApiDTO<Config> resultDto = theaService.findByName(token,CommonConst.CAN_NOT_CLEAN_CUSTOMER_COMPANY);
            Config config = resultDto.getData();
            String value =  "";
            if (config != null){
                value = config.getConValue();
            }
            List<String> strCompany = new ArrayList<>();
            if (StringUtils.isNotEmpty(value)){
                JSONObject jsonObject = JSON.parseObject(value);
                for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                    strCompany.add(entry.getValue().toString());
                }
                if (strCompany.contains(cleanMangerCompanyDTO.getSubCompanyId().toString())){
                    theaApiDTO.setResult(CommonMessage.ADD_FAIL.getCode());
                    theaApiDTO.setMessage(CommonConst.SUB_COMPANY_EXIST);
                    return theaApiDTO;
                }else{
                    strCompany.add(cleanMangerCompanyDTO.getSubCompanyId().toString());
                }
            }else{
                strCompany.add(cleanMangerCompanyDTO.getSubCompanyId().toString());
            }

            config.setConValue(value);
            Integer addResult = cleanMangerService.add(cleanMangerCompanyDTO.getSubCompanyId(),strCompany,config,token);
            if (addResult >0) {
                theaApiDTO.setResult(CommonMessage.ADD_SUCCESS.getCode());
                theaApiDTO.setMessage(CommonMessage.ADD_SUCCESS.getCodeDesc());
            } else {
                logger.error("-------------->addCompany创建屏蔽分公司失败");
                theaApiDTO.setResult(CommonMessage.ADD_FAIL.getCode());
                theaApiDTO.setMessage(CommonMessage.ADD_FAIL.getCodeDesc());
            }
        }catch (Exception e){
            logger.error("-------------->addCompany创建屏蔽分公司失败",e);
            theaApiDTO.setResult(CommonMessage.ADD_FAIL.getCode());
            theaApiDTO.setMessage(CommonMessage.ADD_FAIL.getCodeDesc());
        }

        return theaApiDTO;
    }*/


    /*@ApiOperation(value="添加需要屏蔽的业务员客户", notes="添加需要屏蔽的业务员客户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
    })
    @RequestMapping(value = "/addEmp", method = RequestMethod.POST)
    @ResponseBody
    public TheaApiDTO addEmp(@Valid @RequestBody CleanUserDTO cleanUserDTO, BindingResult result, HttpServletRequest request){
        logger.info("添加需要屏蔽员工的数据：" + cleanUserDTO.toString());
        TheaApiDTO theaApiDTO = new TheaApiDTO();
        if(result.hasErrors()){
            throw new CronusException(CronusException.Type.THEA_SYSTEM_ERROR);
        }
        try{
            //业务员id不能为空
            if (cleanUserDTO.getUserId() == null){
                theaApiDTO.setResult(CommonMessage.ADD_FAIL.getCode());
                theaApiDTO.setMessage(CommonConst.ID_NULL);
                return theaApiDTO;
            }
            String token=request.getHeader("Authorization");
            CronusDto<UserInfoDTO> thorApiDTO=thorUcService.getUserInfoByToken(token,CommonConst.SYSTEMNAME);
            UserInfoDTO userInfoDTO=thorApiDTO.getData();
            //获取屏蔽的业务员
            TheaApiDTO<Config> resultDto = theaService.findByName(token,CommonConst.CAN_NOT_CLEAN_CUSTOMER_USER_ID);
            Config config = resultDto.getData();
            String value =  "";
            if (config != null){
                List<Integer> idList = new ArrayList<>();
                value = config.getConValue();
                if (StringUtils.isNotEmpty(value)){
                    String ids = value.substring(1,value.length()-1);
                    String[] idArray = ids.split(",");
                    if (idArray.length>0){
                        for(int i=0;i<idArray.length;i++){
                            idArray[i] = idArray[i].replace(" ","");
                            Integer id = Integer.parseInt(idArray[i]);
                            idList.add(id);
                        }
                    }
                    if (idList.contains(cleanUserDTO.getUserId())){
                        theaApiDTO.setResult(CommonMessage.ADD_FAIL.getCode());
                        theaApiDTO.setMessage(CommonConst.EXIST);
                        return theaApiDTO;
                    }else{
                        idList.add(cleanUserDTO.getUserId());
                    }
                }else{
                    idList.add(cleanUserDTO.getUserId());
                }
                value = idList.toString();
                config.setConValue(value);
                Integer addResult = cleanMangerService.addEmp(config,token);
                if (addResult >0) {
                    theaApiDTO.setResult(CommonMessage.ADD_SUCCESS.getCode());
                    theaApiDTO.setMessage(CommonMessage.ADD_SUCCESS.getCodeDesc());
                } else {
                    logger.error("-------------->addEmp创建屏蔽业务员失败");
                    theaApiDTO.setResult(CommonMessage.ADD_FAIL.getCode());
                    theaApiDTO.setMessage(CommonMessage.ADD_FAIL.getCodeDesc());
                }
            }
        }catch (Exception e){
            logger.error("-------------->addCompany创建屏蔽业务员失败",e);
            theaApiDTO.setResult(CommonMessage.ADD_FAIL.getCode());
            theaApiDTO.setMessage(CommonMessage.ADD_FAIL.getCodeDesc());
        }

        return theaApiDTO;
    }*/

    @ApiOperation(value="新增自动清洗管理", notes="新增自动清洗管理")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string")})
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public TheaApiDTO insert(@Valid @RequestBody AutoCleanManageDTO autoCleanManageDTO, BindingResult result, HttpServletRequest request){
        TheaApiDTO theaApiDTO = new TheaApiDTO();
        logger.info("新增自动清洗管理的数据：" + autoCleanManageDTO.toString());
        if(result.hasErrors()){
            throw new CronusException(CronusException.Type.THEA_SYSTEM_ERROR);
        }
        String token=request.getHeader("Authorization");
        CronusDto<UserInfoDTO> thorApiDTO=thorUcService.getUserInfoByToken(token,CommonConst.SYSTEMNAME);
        UserInfoDTO userInfoDTO=thorApiDTO.getData();
        if (autoCleanManageDTO.getUserId() == null) {
            theaApiDTO.setResult(CommonMessage.ADD_FAIL.getCode());
            theaApiDTO.setMessage("userId不能为空");
            return theaApiDTO;
        }
        if (autoCleanManageDTO.getType() == null) {
            theaApiDTO.setResult(CommonMessage.ADD_FAIL.getCode());
            theaApiDTO.setMessage("type不能为空");
            return theaApiDTO;
        }
        if (autoCleanManageDTO.getType() == 2 && StringUtils.isEmpty(autoCleanManageDTO.getUtmSource())){
            theaApiDTO.setResult(CommonMessage.ADD_FAIL.getCode());
            theaApiDTO.setMessage("utmSource不能为空");
            return theaApiDTO;
        }
        if (autoCleanManageDTO.getType() == 2 && StringUtils.isEmpty(autoCleanManageDTO.getCustomerSource())){
            theaApiDTO.setResult(CommonMessage.ADD_FAIL.getCode());
            theaApiDTO.setMessage("customerSource不能为空");
            return theaApiDTO;
        }
        if (autoCleanManageDTO.getUserId() != null && StringUtils.isEmpty(autoCleanManageDTO.getUtmSource())
                && StringUtils.isEmpty(autoCleanManageDTO.getCustomerSource())){
            List<AutoCleanManage> autoCleanManageList = autoCleanManageService.selectByUserId(autoCleanManageDTO.getUserId());
            if (!CollectionUtils.isEmpty(autoCleanManageList)){
                theaApiDTO.setResult(CommonMessage.ADD_FAIL.getCode());
                theaApiDTO.setMessage("客户已存在");
                return theaApiDTO;
            }
        }
        try{
            AutoCleanManage autoCleanManage = autoCleanManageService.copyProperty(autoCleanManageDTO);
            Integer userId = null;
            if (StringUtils.isNotEmpty(userInfoDTO.getUser_id())) {
                userId = Integer.parseInt(userInfoDTO.getUser_id());
            }
            int createResult = autoCleanManageService.add(autoCleanManage,userId);
            if (createResult >0) {
                theaApiDTO.setResult(CommonMessage.ADD_SUCCESS.getCode());
                theaApiDTO.setMessage(CommonMessage.ADD_SUCCESS.getCodeDesc());
            } else {
                logger.error("-------------->insert创建自动清洗失败");
                theaApiDTO.setResult(CommonMessage.ADD_FAIL.getCode());
                theaApiDTO.setMessage(CommonMessage.ADD_FAIL.getCodeDesc());
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
            }
        }catch (Exception e){
            logger.error("-------------->insert创建自动清洗失败",e);
            theaApiDTO.setResult(CommonMessage.ADD_FAIL.getCode());
            theaApiDTO.setMessage(CommonMessage.ADD_FAIL.getCodeDesc());
        }

        return theaApiDTO;
    }

    @ApiOperation(value="获取自动清洗屏蔽列表", notes="获取屏蔽列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
    })
    @RequestMapping(value = "/getAutoCleanList", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto getAutoCleanList(HttpServletRequest request){
        CronusDto cronusDto = new CronusDto();
        String token=request.getHeader("Authorization");
        List<AutoCleanManageDTO2> autoCleanManageList = null;
        try{
            autoCleanManageList = autoCleanManageService.getList(token);
            cronusDto.setResult(CommonMessage.SUCCESS.getCode());
            cronusDto.setMessage(CommonMessage.SUCCESS.getCodeDesc());
        }catch (Exception e){
            cronusDto.setResult(CommonMessage.FAIL.getCode());
            cronusDto.setMessage(CommonMessage.FAIL.getCodeDesc());
            logger.error("获取屏蔽列表失败：",e);
        }
        cronusDto.setData(autoCleanManageList);

        return cronusDto;
    }

    @ApiOperation(value="列表展开", notes="列表展开")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "userId", value = "业务员id", paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/getChildById", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto getChildById(HttpServletRequest request,@RequestParam(required = true)Integer userId){
        CronusDto cronusDto = new CronusDto();
        String token=request.getHeader("Authorization");
        List<AutoCleanManageDTO> autoCleanManageList = null;
        try{
            autoCleanManageList = autoCleanManageService.getByUserId(userId);
            cronusDto.setResult(CommonMessage.SUCCESS.getCode());
            cronusDto.setMessage(CommonMessage.SUCCESS.getCodeDesc());
        }catch (Exception e){
            cronusDto.setResult(CommonMessage.FAIL.getCode());
            cronusDto.setMessage(CommonMessage.FAIL.getCodeDesc());
            logger.error("展开屏蔽列表失败：",e);
        }
        cronusDto.setData(autoCleanManageList);
        return cronusDto;
    }

    @ApiOperation(value="删除屏蔽", notes="删除屏蔽")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "id", value = "id", paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/deleteById", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto deleteById(HttpServletRequest request,@RequestParam(required = true)Integer id){
        CronusDto cronusDto = new CronusDto();
        String token=request.getHeader("Authorization");
        CronusDto<UserInfoDTO> thorApiDTO=thorUcService.getUserInfoByToken(token,CommonConst.SYSTEMNAME);
        UserInfoDTO userInfoDTO=thorApiDTO.getData();
        Integer userId = null;
        try{
            if (userInfoDTO != null &&StringUtils.isNotEmpty(userInfoDTO.getUser_id())){
                userId = Integer.parseInt(userInfoDTO.getUser_id());
            }
            int num = autoCleanManageService.deleteById(id,userId);
            if (num > 0){
                cronusDto.setResult(CommonMessage.SUCCESS.getCode());
                cronusDto.setMessage(CommonMessage.SUCCESS.getCodeDesc());
            }else{
                cronusDto.setResult(CommonMessage.FAIL.getCode());
                cronusDto.setMessage(CommonMessage.FAIL.getCodeDesc());
                logger.info("删除屏蔽失败");
            }
        }catch (Exception e){
            cronusDto.setResult(CommonMessage.FAIL.getCode());
            cronusDto.setMessage(CommonMessage.FAIL.getCodeDesc());
            logger.error("删除屏蔽失败：",e);
        }
        return cronusDto;
    }
}
