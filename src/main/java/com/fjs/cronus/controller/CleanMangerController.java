package com.fjs.cronus.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonMessage;
import com.fjs.cronus.api.thea.Config;
import com.fjs.cronus.dto.CleanMangerCompanyDTO;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.api.crius.CriusApiDTO;
import com.fjs.cronus.dto.loan.TheaApiDTO;
import com.fjs.cronus.dto.uc.UserInfoDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.service.CleanMangerService;
import com.fjs.cronus.service.client.TheaService;
import com.fjs.cronus.service.client.ThorInterfaceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
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

//    @Autowired
//    private AutoCleanService autoCleanService;
    @Autowired
    private CleanMangerService cleanMangerService;
    @Autowired
    private ThorInterfaceService thorUcService;
    @Autowired
    private TheaService theaService;

    @ApiOperation(value="添加需要屏蔽的公司", notes="添加需要屏蔽的公司")
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
    }


    /*@ApiOperation(value="添加需要屏蔽的业务员客户", notes="添加需要屏蔽的业务员客户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
    })
    @RequestMapping(value = "/addCustomer", method = RequestMethod.POST)
    @ResponseBody
    public TheaApiDTO addCustomer(@Valid @RequestBody CleanMangerCompanyDTO cleanMangerCompanyDTO, BindingResult result, HttpServletRequest request){
        logger.info("添加需要屏蔽公司的数据：" + cleanMangerCompanyDTO.toString());
        TheaApiDTO theaApiDTO = new TheaApiDTO();
        if(result.hasErrors()){
            throw new TheaException(TheaException.Type.MESSAGE_PARAM_ERROR);
        }
        try{
//            //分公司id不能为空
//            if (cleanMangerCompanyDTO.getSubCompanyId() == null){
//                theaApiDTO.setResult(CommonMessage.ADD_FAIL.getCode());
//                theaApiDTO.setMessage(CommonConst.SUB_COMPANY_ID_NULL);
//                return theaApiDTO;
//            }
//            //获取屏蔽的公司
//            Config config = configMapper.findValueByName(CommonConst.CAN_NOT_CLEAN_CUSTOMER_COMPANY);
//            if(config == null){
//                config = new Config();
//            }
//            if (config != null){
//                String value = config.getConValue();
//                List<String> strCompany = new ArrayList<>();
//                if (StringUtils.isNotEmpty(value)){
//                    JSONObject jsonObject = JSON.parseObject(value);
//                    for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
//                        strCompany.add(entry.getValue().toString());
//                    }
//                    if (strCompany.contains(cleanMangerCompanyDTO.getSubCompanyId().toString())){
//                        theaApiDTO.setResult(CommonMessage.ADD_FAIL.getCode());
//                        theaApiDTO.setMessage(CommonConst.SUB_COMPANY_EXIST);
//                        return theaApiDTO;
//                    }else{
//                        strCompany.add(cleanMangerCompanyDTO.getSubCompanyId().toString());
//                    }
//                }else{
//                    strCompany.add(cleanMangerCompanyDTO.getSubCompanyId().toString());
//                }
//                String token=request.getHeader("Authorization");
//                ThorApiDTO<UserInfoDTO> thorApiDTO=thorUcService.getUserInfoByToken(token,CommonConst.SYSTEMNAME);
//                UserInfoDTO userInfoDTO=thorApiDTO.getData();
//
//                config.setConValue(value);
//                Integer addResult = cleanMangerService.add(cleanMangerCompanyDTO.getSubCompanyId(),strCompany,config,userInfoDTO);
//                if (addResult >0) {
//                    theaApiDTO.setResult(CommonMessage.ADD_SUCCESS.getCode());
//                    theaApiDTO.setMessage(CommonMessage.ADD_SUCCESS.getCodeDesc());
//                } else {
//                    logger.error("-------------->addCompany创建屏蔽分公司失败");
//                    theaApiDTO.setResult(CommonMessage.ADD_FAIL.getCode());
//                    theaApiDTO.setMessage(CommonMessage.ADD_FAIL.getCodeDesc());
//                }
//            }
        }catch (Exception e){
            logger.error("-------------->addCompany创建屏蔽分公司失败",e);
            theaApiDTO.setResult(CommonMessage.ADD_FAIL.getCode());
            theaApiDTO.setMessage(CommonMessage.ADD_FAIL.getCodeDesc());
        }

        return theaApiDTO;
    }*/
}
