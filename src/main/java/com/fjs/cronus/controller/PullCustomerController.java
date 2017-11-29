package com.fjs.cronus.controller;
import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonMessage;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.cronus.PullCustomerDTO;
import com.fjs.cronus.dto.uc.UserInfoDTO;
import com.fjs.cronus.dto.api.PHPLoginDto;

import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.model.PullCustomer;
import com.fjs.cronus.service.PullCustomerService;
import com.fjs.cronus.service.uc.UcService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/pullCustomer/v1")
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
            @ApiImplicitParam(name = "createTimeBegin",value =  "创建起始时间",required  = false, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name = "createTimeEnd",value =  "创建结束时间",required  = false, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name = "telephonenumber", value = "手机号码", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "status", value = "状态", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "page", value = "查询第几页", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "显示多少", required = true, paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/pullCustomerList", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<QueryResult<PullCustomerDTO>> listPullCustomer(HttpServletRequest request, @RequestParam(required = false) String createTimeBegin,
                                                                    @RequestParam(required = false) String createTimeEnd,
                                                                    @RequestParam(required = false) String telephonenumber,
                                                                    @RequestParam(required = false) Integer status,
                                                                    @RequestParam(required = false) String name,
                                                                    @RequestParam String page,
                                                                    @RequestParam String size){
        CronusDto cronusDto=new CronusDto();
        QueryResult<PullCustomerDTO> pullCustomerDTOQueryResult=new QueryResult<PullCustomerDTO>();
        try{
            PullCustomer pullCustomer=new PullCustomer();
            String token=request.getHeader("Authorization");
            UserInfoDTO userInfoDTO=thorUcService.getUserIdByToken(token, CommonConst.SYSTEMNAME);
            pullCustomer.setName(name);
            pullCustomer.setCreateTimeBegin(createTimeBegin);
            pullCustomer.setCreateTimeEnd(createTimeEnd);
            pullCustomer.setTelephone(telephonenumber);
            pullCustomer.setStatus(status);
            Integer pageNum=1;
            Integer sizeNum=20;
            if(StringUtils.isNotEmpty(page)){
                pageNum=Integer.parseInt(page);
            }
            if (StringUtils.isNotEmpty(size)){
                sizeNum=Integer.parseInt(size);
            }
            pullCustomerDTOQueryResult = pullCustomerService.listByCondition(pullCustomer,userInfoDTO, token, CommonConst.SYSTEMNAME, pageNum, sizeNum);
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
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string")})
    @RequestMapping(value = "/updatePullCustomer", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto updatePullCustomer(@Valid @RequestBody PullCustomerDTO pullCustomerDTO, BindingResult result, HttpServletRequest request){
        logger.info("保存原始盘的数据："+pullCustomerDTO.toString());
        CronusDto theaApiDTO =new CronusDto();
        if(result.hasErrors()){
            throw new CronusException(CronusException.Type.CEM_CUSTOMERINTERVIEW);
        }
        String token=request.getHeader("Authorization");
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
        try{
            PullCustomer pullCustomer=pullCustomerService.selectById(pullCustomerDTO.getId());
            if (pullCustomer != null&& pullCustomer.getStatus()==CommonConst.PULL_CUSTOMER_STASTUS_TRANSFER){
                theaApiDTO.setResult(CommonMessage.UPDATE_FAIL.getCode());
                theaApiDTO.setMessage(CommonConst.UPDATE_TRANSFER);
                return theaApiDTO;
            }
            pullCustomer=pullCustomerService.copyProperty(pullCustomerDTO);

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

    @ApiOperation(value="将原始盘转入交易", notes="将原始盘转入交易")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string")})
    @RequestMapping(value = "/transferLoan", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto transferLoan(@Valid @RequestBody PullCustomerDTO pullCustomerDTO, BindingResult result, HttpServletRequest request){
        logger.info("转入原始盘的数据："+pullCustomerDTO.toString());
        CronusDto theaApiDTO =new CronusDto();
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
        try{
            if (pullCustomerDTO.getId() == null){
                theaApiDTO.setResult(CommonMessage.TRANSFER_FAIL.getCode());
                theaApiDTO.setMessage(CommonConst.ID_NULL);
                return theaApiDTO;
            }
            PullCustomer pullCustomer=pullCustomerService.selectById(pullCustomerDTO.getId());
            if (pullCustomer == null){
                theaApiDTO.setResult(CommonMessage.TRANSFER_FAIL.getCode());
                theaApiDTO.setMessage(CommonConst.OBJECT_NULL);
                return theaApiDTO;
            }
            int createResult = pullCustomerService.transfer(pullCustomer,userInfoDTO,token);
            if (createResult >0) {
                theaApiDTO.setResult(CommonMessage.TRANSFER_SUCCESS.getCode());
                theaApiDTO.setMessage(CommonMessage.TRANSFER_SUCCESS.getCodeDesc());
            } else {
                logger.error("-------------->transferPullCustomer更新原始盘失败");
                theaApiDTO.setResult(CommonMessage.TRANSFER_FAIL.getCode());
                theaApiDTO.setMessage(CommonMessage.TRANSFER_FAIL.getCodeDesc());
                throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
            }
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
    public CronusDto changeStatus(@Valid @RequestBody PullCustomerDTO pullCustomerDTO, BindingResult result, HttpServletRequest request){
        CronusDto theaApiDTO=new CronusDto();
        logger.info("改变原始盘状态的数据："+pullCustomerDTO.toString());
        if(result.hasErrors()){
            throw new CronusException(CronusException.Type.CEM_CUSTOMERINTERVIEW);
        }
        String token=request.getHeader("Authorization");
        UserInfoDTO userInfoDTO=thorUcService.getUserIdByToken(token, CommonConst.SYSTEMNAME);
        PHPLoginDto resultDto = thorUcService.getAllUserInfo(token,CommonConst.SYSTEMNAME);
        try{
            if (pullCustomerDTO.getId() == null){
                theaApiDTO.setResult(CommonMessage.CHANGE_FAIL.getCode());
                theaApiDTO.setMessage(CommonConst.ID_NULL);
                return theaApiDTO;
            }
            if (pullCustomerDTO.getStatus() == null){
                theaApiDTO.setResult(CommonMessage.CHANGE_FAIL.getCode());
                theaApiDTO.setMessage(CommonConst.STATUS_NULL);
                return theaApiDTO;
            }
            List<Integer> statusList=new ArrayList<Integer>();
            statusList.add(CommonConst.PULL_CUSTOMER_STASTUS_UNVALID);
            statusList.add(CommonConst.PULL_CUSTOMER_STASTUS_NORMAL);
            if (!statusList.contains(pullCustomerDTO.getStatus())){
                theaApiDTO.setResult(CommonMessage.CHANGE_FAIL.getCode());
                theaApiDTO.setMessage(CommonConst.UNVALID_PARA);
                return theaApiDTO;
            }
            PullCustomer pullCustomer=pullCustomerService.selectById(pullCustomerDTO.getId());
            if (pullCustomer == null){
                theaApiDTO.setResult(CommonMessage.CHANGE_FAIL.getCode());
                theaApiDTO.setMessage(CommonConst.OBJECT_NULL);
                return theaApiDTO;
            }
            int createResult = pullCustomerService.changeStatus(pullCustomer,userInfoDTO,pullCustomerDTO.getStatus());
            if (createResult >0) {
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
