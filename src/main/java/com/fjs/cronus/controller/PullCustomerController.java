package com.fjs.cronus.controller;
import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonMessage;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.cronus.AddPullCustomerDTO;
import com.fjs.cronus.dto.cronus.PullCustomerDTO;
import com.fjs.cronus.dto.cronus.PullStatusDTO;
import com.fjs.cronus.dto.uc.UserInfoDTO;
import com.fjs.cronus.dto.api.PHPLoginDto;

import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.model.PullCustomer;
import com.fjs.cronus.service.PullCustomerService;
import com.fjs.cronus.service.uc.UcService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by yinzf on 2017/10/24.
 */
@Controller
@Api(description = "海贷魔方盘")
@RequestMapping("/api/v1")
public class PullCustomerController {
    private  static  final Logger logger = LoggerFactory.getLogger(PullCustomerController.class);

    @Autowired
    private PullCustomerService pullCustomerService;
    @Autowired
    private UcService thorUcService;



    @ApiOperation(value="获取原始盘列表", notes="获取原始盘列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "name", value = "姓名", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "telephonenumber", value = "手机号码", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "status", value = "状态", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "city", value = "城市", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "mountLevle", value = "1：0-20万，2：20-50万，3:50-100万，4:100-500万，5：大于五百万 ", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "createTime", value = "创建时间", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "page", value = "查询第几页", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "显示多少", required = true, paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/pullCustomerList", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<QueryResult<PullCustomerDTO>> listPullCustomer(
                                                                    @RequestParam(required = false) String telephonenumber,
                                                                    @RequestParam(required = false) Integer status,
                                                                    @RequestParam(required = false) String name,
                                                                    @RequestParam(required = false) String city,
                                                                    @RequestParam(required = false) Integer mountLevle,
                                                                    @RequestParam(required = false) String createTime,
                                                                    @RequestParam Integer page,
                                                                    @RequestParam Integer size,
                                                                    @RequestHeader("Authorization")String token){
        CronusDto cronusDto=new CronusDto();
        QueryResult<PullCustomerDTO> pullCustomerDTOQueryResult=new QueryResult<PullCustomerDTO>();
        Integer userId = Integer.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        if (userId == null){
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        try{
            pullCustomerDTOQueryResult = pullCustomerService.listByCondition(telephonenumber,status,name, token, CommonConst.SYSTEMNAME,city,mountLevle,createTime, page, size,userId);
            cronusDto.setResult(CommonMessage.SUCCESS.getCode());
            cronusDto.setMessage(CommonMessage.SUCCESS.getCodeDesc());
        }catch (Exception e){
            logger.error("获取原始盘列表",e);
            cronusDto.setResult(CommonMessage.FAIL.getCode());
            cronusDto.setMessage(CommonMessage.FAIL.getCodeDesc());
        }
        cronusDto.setData(pullCustomerDTOQueryResult);
        return cronusDto;
    }

    @ApiOperation(value="根据id获取原始盘信息", notes="根据id获取原始盘信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "id", value = "原始盘id", required = false, paramType = "query",  dataType = "int"),
    })
    @RequestMapping(value = "/selectPullCustomerById", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<PullCustomerDTO> selectPullCustomerById(@RequestParam(required = true) Integer id, HttpServletRequest request){
        CronusDto theaApiDTO=new CronusDto();
        PullCustomer pullCustomer=null;
        PullCustomerDTO pullCustomerDTO=null;
        try{
            String token=request.getHeader("Authorization");
            UserInfoDTO userInfoDTO = thorUcService.getUserIdByToken(token,CommonConst.SYSTEMNAME);
            if (id !=null){
                pullCustomer = pullCustomerService.selectById(id);
                pullCustomerDTO=pullCustomerService.copyProperty(pullCustomer);
                pullCustomerDTO.setOwnUserName(userInfoDTO.getName());
            }else{
                theaApiDTO.setResult(CommonMessage.FAIL.getCode());
                theaApiDTO.setMessage(CommonConst.ID_NULL);
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
            }
            theaApiDTO.setResult(CommonMessage.SUCCESS.getCode());
            theaApiDTO.setMessage(CommonMessage.SUCCESS.getCodeDesc());
        }catch (Exception e){
            logger.error("根据id获取原始盘信息",e);
            theaApiDTO.setResult(CommonMessage.FAIL.getCode());
            theaApiDTO.setMessage(CommonMessage.FAIL.getCodeDesc());
        }
        theaApiDTO.setData(pullCustomerDTO);
        return theaApiDTO;
    }

    @ApiOperation(value="保存海岱魔方原始盘", notes="保存海岱魔方原始盘")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "pullCustomerDTO", value = "pullCustomerDTO", required = true, paramType = "body", dataType = "AddPullCustomerDTO"),
    })

    @RequestMapping(value = "/updatePullCustomer", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto updatePullCustomer(@Valid @RequestBody AddPullCustomerDTO pullCustomerDTO, BindingResult result, @RequestHeader("Authorization")String token){
        logger.info("保存原始盘的数据："+pullCustomerDTO.toString());
        CronusDto theaApiDTO =new CronusDto();
        if(result.hasErrors()){
            throw new CronusException(CronusException.Type.CEM_CUSTOMERINTERVIEW);
        }
        UserInfoDTO userInfoDTO=thorUcService.getUserIdByToken(token, CommonConst.SYSTEMNAME);
        PHPLoginDto resultDto = thorUcService.getAllUserInfo(token,CommonConst.SYSTEMNAME);
        String[] authority=resultDto.getAuthority();
        if(authority.length>0){
            List<String> authList= Arrays.asList(authority);
            if (authList.contains(CommonConst.UPDATE_PULL_CUSTOMER_URL)){
                theaApiDTO.setResult(CommonMessage.UPDATE_FAIL.getCode());
                theaApiDTO.setMessage(CommonConst.NO_AUTHORIZE);
                return theaApiDTO;
            }
        }
        //判断是否全是下属
        try{
            PullCustomer pullCustomer=pullCustomerService.selectById(pullCustomerDTO.getId());
            List<Integer> ids = thorUcService.getSubUserByUserId(token,Integer.valueOf(pullCustomer.getSaleId()));
            if (!ids.contains(userInfoDTO.getUser_id())){
                throw new CronusException(CronusException.Type.CRM_CALLBACKCUSTOMER_ERROR);
            }
            if (pullCustomer != null&& pullCustomer.getStatus()==CommonConst.PULL_CUSTOMER_STASTUS_TRANSFER){
                theaApiDTO.setResult(CommonMessage.UPDATE_FAIL.getCode());
                theaApiDTO.setMessage(CommonConst.UPDATE_TRANSFER);
                return theaApiDTO;
            }
            //
            pullCustomer=pullCustomerService.copyPropertyAdd(pullCustomerDTO);

            if (StringUtils.isNotEmpty(userInfoDTO.getUser_id().toString())){
                Date date=new Date();
                pullCustomer.setLastUpdateTime(date);
                pullCustomer.setLastUpdateUser(Integer.parseInt(userInfoDTO.getUser_id().toString()));
            }


            int createResult = pullCustomerService.update(pullCustomer,userInfoDTO);
            if (createResult >0) {
                theaApiDTO.setResult(CommonMessage.UPDATE_SUCCESS.getCode());
                theaApiDTO.setMessage(CommonMessage.UPDATE_SUCCESS.getCodeDesc());
            } else {
                logger.error("-------------->updatePullCustomer更新原始盘失败");
                theaApiDTO.setResult(CommonMessage.UPDATE_FAIL.getCode());
                theaApiDTO.setMessage(CommonMessage.UPDATE_FAIL.getCodeDesc());
                throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
            }
        }catch (Exception e){
            logger.error("updatePullCustomer更新原始盘失败",e);
            theaApiDTO.setResult(CommonMessage.UPDATE_FAIL.getCode());
            theaApiDTO.setMessage(CommonMessage.UPDATE_FAIL.getCodeDesc());
        }
        return theaApiDTO;
    }

    @ApiOperation(value="将原始盘转入客户表", notes="将原始盘转入客户表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "jsonObject", value = "{'id','12'}", required = true, paramType = "body", dataType = "JSONObject"),
    })
    @RequestMapping(value = "/transferLoan", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto transferLoan(@RequestBody JSONObject jsonObject, BindingResult result, HttpServletRequest request){
        Integer id = jsonObject.getInteger("id");
        CronusDto theaApiDTO =new CronusDto();
        PullCustomer pullCustomer=pullCustomerService.selectById(id);
        if (pullCustomer == null){
            theaApiDTO.setResult(CommonMessage.TRANSFER_FAIL.getCode());
            theaApiDTO.setMessage(CommonConst.OBJECT_NULL);
            return theaApiDTO;
        }
        if(result.hasErrors()){
            throw new CronusException(CronusException.Type.CEM_CUSTOMERINTERVIEW);
        }
        String token=request.getHeader("Authorization");
        UserInfoDTO  userInfoDTO=thorUcService.getUserIdByToken(token, CommonConst.SYSTEMNAME);
        PHPLoginDto resultDto = thorUcService.getAllUserInfo(token,CommonConst.SYSTEMNAME);
        String[] authority=resultDto.getAuthority();
        if(authority.length>0){
            List<String> authList= Arrays.asList(authority);
            if (authList.contains(CommonConst.TRANSFER_PULL_CUSTOMER_URL)){
                theaApiDTO.setResult(CommonMessage.TRANSFER_FAIL.getCode());
                theaApiDTO.setMessage(CommonConst.NO_AUTHORIZE);
                return theaApiDTO;
            }
        }
        //判断是否是其下属
        List<Integer> ids = thorUcService.getSubUserByUserId(token,Integer.valueOf(pullCustomer.getSaleId()));
        if (!ids.contains(userInfoDTO.getUser_id())){
            throw new CronusException(CronusException.Type.CRM_CALLBACKCUSTOMER_ERROR);
        }
        try{
            theaApiDTO = pullCustomerService.transfer(pullCustomer,userInfoDTO,token);
        }catch (Exception e){
            logger.error("-------------->transferPullCustomer更新原始盘失败",e);
            theaApiDTO.setResult(CommonMessage.TRANSFER_FAIL.getCode());
            theaApiDTO.setMessage(CommonMessage.TRANSFER_FAIL.getCodeDesc());
        }
        return theaApiDTO;
    }

    @ApiOperation(value="改变原始盘状态", notes="改变原始盘状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string")})
    @RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto changeStatus(@Valid @RequestBody PullStatusDTO pullCustomerDTO, BindingResult result, HttpServletRequest request){
        CronusDto theaApiDTO=new CronusDto();
        logger.info("改变原始盘状态的数据："+pullCustomerDTO.toString());
        if(result.hasErrors()){
            throw new CronusException(CronusException.Type.CEM_CUSTOMERINTERVIEW);
        }
        String token=request.getHeader("Authorization");
        UserInfoDTO userInfoDTO=thorUcService.getUserIdByToken(token, CommonConst.SYSTEMNAME);
        PHPLoginDto resultDto = thorUcService.getAllUserInfo(token,CommonConst.SYSTEMNAME);
        //判断当前用户能否操作
        //判断是否全是下属
        try{
            if (pullCustomerDTO.getId() == null){
                theaApiDTO.setResult(CommonMessage.CHANGE_FAIL.getCode());
                theaApiDTO.setMessage(CommonConst.ID_NULL);
                return theaApiDTO;
            }
            if (pullCustomerDTO.getToStatus() == null){
                theaApiDTO.setResult(CommonMessage.CHANGE_FAIL.getCode());
                theaApiDTO.setMessage(CommonConst.STATUS_NULL);
                return theaApiDTO;
            }
            List<Integer> statusList=new ArrayList<Integer>();
            statusList.add(CommonConst.PULL_CUSTOMER_STASTUS_UNVALID);
            statusList.add(CommonConst.PULL_CUSTOMER_STASTUS_NORMAL);
            if (!statusList.contains(pullCustomerDTO.getToStatus())){
                theaApiDTO.setResult(CommonMessage.CHANGE_FAIL.getCode());
                theaApiDTO.setMessage(CommonConst.UNVALID_PARA);
                return theaApiDTO;
            }
            PullCustomer pullCustomer=pullCustomerService.selectById(pullCustomerDTO.getId());
            List<Integer> ids = thorUcService.getSubUserByUserId(token,Integer.valueOf(pullCustomer.getSaleId()));
            if (!ids.contains(userInfoDTO.getUser_id())){
                throw new CronusException(CronusException.Type.CRM_CALLBACKCUSTOMER_ERROR);
            }
            if (pullCustomer == null){
                theaApiDTO.setResult(CommonMessage.CHANGE_FAIL.getCode());
                theaApiDTO.setMessage(CommonConst.OBJECT_NULL);
                return theaApiDTO;
            }
            boolean createResult = pullCustomerService.changeStatus(pullCustomer,userInfoDTO,pullCustomerDTO.getToStatus());
            if (createResult == true) {
                theaApiDTO.setResult(CommonMessage.CHANGE_SUCCESS.getCode());
                theaApiDTO.setMessage(CommonMessage.CHANGE_SUCCESS.getCodeDesc());
            } else {
                logger.error("-------------->changeStatus更新状态失败");
                theaApiDTO.setResult(CommonMessage.CHANGE_FAIL.getCode());
                theaApiDTO.setMessage(CommonMessage.CHANGE_FAIL.getCodeDesc());
                throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
            }
        }catch (Exception e){
            logger.error("-------------->changeStatus更新状态失败",e);
            theaApiDTO.setResult(CommonMessage.CHANGE_FAIL.getCode());
            theaApiDTO.setMessage(CommonMessage.CHANGE_FAIL.getCodeDesc());
        }
        return theaApiDTO;
    }
}
