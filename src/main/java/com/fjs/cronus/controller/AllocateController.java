package com.fjs.cronus.controller;

import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonEnum;
import com.fjs.cronus.Common.CommonMessage;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.api.SimpleUserInfoDTO;
import com.fjs.cronus.dto.api.PHPLoginDto;
import com.fjs.cronus.dto.api.PHPUserDto;

import com.fjs.cronus.dto.api.uc.PhpDepartmentModel;

import com.fjs.cronus.dto.thea.AllocateDTO;
import com.fjs.cronus.dto.thea.AllocateLogDTO;
import com.fjs.cronus.dto.uc.PhpQueryResultDTO;
import com.fjs.cronus.dto.uc.ThorQueryDto;
import com.fjs.cronus.dto.uc.UserInfoDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.model.AllocateLog;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.service.AllocateLogService;
import com.fjs.cronus.service.AllocateService;
import com.fjs.cronus.service.CustomerInfoService;
import com.fjs.cronus.service.client.ThorInterfaceService;
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
import java.util.List;

/**
 * Created by msi on 2017/12/2.
 */
@Controller
@Api(description = "批量分配")
@RequestMapping("/api/v1")
public class AllocateController {

    private  static  final Logger logger = LoggerFactory.getLogger(AllocateController.class);

    @Autowired
    AllocateService allocateService;
    @Autowired
    UcService ucService;

    @Autowired
    ThorInterfaceService thorInterfaceService;

    @Autowired
    AllocateLogService allocateLogService;

    @Autowired
    CustomerInfoService customerInfoService;

    @ApiOperation(value="sellUser获取可操作城市列表", notes="sellUser获取可操作城市列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customer_ids", value = "客户id，逗号隔开 1,2,3", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "action", value = "removeCustomerAll：离职员工批量转移，allocateAll：批量分配", paramType = "query", dataType = "string"),
    })
    @RequestMapping(value = "/sellUser", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto sellUser(@RequestHeader("Authorization")String token,
                             @RequestParam(value = "customer_ids",required = false)String customer_ids,
                             @RequestParam(value = "action",required = false)String action){
        CronusDto cronusDto = new CronusDto();
        Integer userId = Integer.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        if (userId == null){
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        try{
            cronusDto = allocateService.sellUser(token,customer_ids,action,userId);
        }catch (Exception e){
            logger.error("--------------->sellUser获取可操作城市列表",e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException)e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }

        return  cronusDto;
    }

    @ApiOperation(value = "根据公司获取下面的子公司", notes = "根据公司获取下面的子公司")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string"),
            @ApiImplicitParam(name = "companyId", value = "公司id", required = true, paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/getSubCompanys", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<List<PhpDepartmentModel>> getSubCompanys(HttpServletRequest request, Integer companyId) {
        CronusDto<List<PhpDepartmentModel>> theaApiDTO = new CronusDto<List<PhpDepartmentModel>>();
        List<PhpDepartmentModel> list = null;
        //获取分公司信息
        String token = request.getHeader("Authorization");
        UserInfoDTO userInfoDTO = ucService.getUserIdByToken(token, CommonConst.SYSTEMNAME);
        Integer user_id = null;
        if (userInfoDTO != null && StringUtils.isNotEmpty(userInfoDTO.getUser_id())) {
            user_id = Integer.parseInt(userInfoDTO.getUser_id());
        }
        try {
            list = ucService.getSubCompanys(token, companyId);
            theaApiDTO.setResult(CommonMessage.SUCCESS.getCode());
            theaApiDTO.setMessage(CommonMessage.SUCCESS.getCodeDesc());
        } catch (Exception e) {
            theaApiDTO.setResult(CommonMessage.FAIL.getCode());
            theaApiDTO.setMessage("获取分公司信息出错");
            logger.error("--------->getSubCompanys，获取分公司信息出错", e);
        }
        theaApiDTO.setData(list);
        return theaApiDTO;
    }

    @ApiOperation(value = "得到下属员工", notes = "得到下属员工")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string"),
            @ApiImplicitParam(name = "subCompanyId", value = "分公司id", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "flag", value = "eq或者neq 但是当sub_company_id不为空时必传", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "name", value = "名字", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "page", value = "第几页", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示数", required = false, paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/getSubUserByUserId", method = RequestMethod.GET)
    @ResponseBody
    public PhpQueryResultDTO getSubUserByUserId(HttpServletRequest request, Integer subCompanyId, String flag,
                                        String name, @RequestParam(required = false) Integer page,
                                        @RequestParam(required = false) Integer pageSize) {
        PhpQueryResultDTO resultDto = new PhpQueryResultDTO();
        //获取当前用户信息
        String token = request.getHeader("Authorization");
        UserInfoDTO userInfoDTO = ucService.getUserIdByToken(token, CommonConst.SYSTEMNAME);
        Integer user_id = null;
        Integer dataType = null;
        if (userInfoDTO != null && StringUtils.isNotEmpty(userInfoDTO.getUser_id())) {
            user_id = Integer.parseInt(userInfoDTO.getUser_id());
        }
        if (userInfoDTO != null && StringUtils.isNotEmpty(userInfoDTO.getData_type())) {
            dataType = Integer.parseInt(userInfoDTO.getData_type());
        }
        if (user_id == 0) {
            resultDto.setErrNum(1);
            resultDto.setErrMsg("获取信息出错，请输入user_id");
            logger.error("--------->getSubUserByUserId，获取信息出错，请输入user_id");
            return resultDto;
        }
        ThorQueryDto<List<PHPUserDto>> subThorApiDTO = null;
        try {
            List<String> list = ucService.getSubUserByUserId(token, user_id);
            StringBuffer idList = new StringBuffer();
            int size = list.size();
            for (int i = 0; i < size; i++) {
                if (i != size - 1) {
                    idList.append(list.get(i) + ",");
                } else {
                    idList.append(list.get(i));
                }
            }
            Integer sub_company_id = null;
            if (StringUtils.isNotEmpty(userInfoDTO.getSub_company_id())) {
                sub_company_id = Integer.parseInt(userInfoDTO.getSub_company_id());
            }
            if (pageSize == null) {
                pageSize = 5;
            }
            String department_ids = userInfoDTO.getDepartment_id();
            subThorApiDTO = thorInterfaceService.getUserByIds(token, null, null, subCompanyId,
                    flag, page, pageSize, name, null);
        } catch (Exception e) {
            resultDto.setErrNum(1);
            resultDto.setErrMsg("获取信息出错");
            logger.error("--------->getSubUserByUserId，获取信息出错", e);
        }
        resultDto.setTotal(subThorApiDTO.getTotal());
        resultDto.setRetData(subThorApiDTO.getRetData());
        return resultDto;
    }
    @ApiOperation(value="批量分配", notes="批量分配")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
    })
    @RequestMapping(value = "/allocateLoan", method = RequestMethod.POST)
    @ResponseBody
    public CronusDto allocateLoan(@Valid @RequestBody AllocateDTO allocateDTO, BindingResult result, HttpServletRequest request){
        logger.info("分配的日志："+allocateDTO.toString());
        CronusDto  theaApiDTO=new CronusDto ();
        if(result.hasErrors()){
            throw new CronusException(CronusException.Type.CEM_CUSTOMERINTERVIEW);
        }
        String token=request.getHeader("Authorization");
        PHPLoginDto resultDto = ucService.getAllUserInfo(token, CommonConst.SYSTEMNAME);
        String[] authority=resultDto.getAuthority();
        if(authority.length>0){
            List<String> authList= Arrays.asList(authority);
            if (authList.contains(CommonConst.ALLOCATE_LOAN_URL)){
                theaApiDTO.setResult(CommonMessage.ALLOCATE_FAIL.getCode());
                theaApiDTO.setMessage(CommonConst.NO_AUTHORIZE);
                return theaApiDTO;
            }
        }
        UserInfoDTO userInfoDTO=ucService.getUserIdByToken(token,CommonConst.SYSTEMNAME);
        List<CustomerInfo> customerInfoList=new ArrayList<CustomerInfo>();
        try{
            customerInfoList=customerInfoService.getByIds(allocateDTO.getIds());
            if (customerInfoList.size() == 0){
                theaApiDTO.setResult(CommonMessage.ALLOCATE_FAIL.getCode());
                theaApiDTO.setMessage(CronusException.Type.CEM_CUSTOMERINTERVIEW.toString());
                throw new CronusException(CronusException.Type.CEM_CUSTOMERINTERVIEW);
            }
            //添加分配日志
            for (CustomerInfo customerInfo :customerInfoList){
                allocateLogService.addAllocatelog(customerInfo, customerInfo.getOwnUserId(),
                        CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_2.getCode(), null);
            }
            //开始进行批量分配
            boolean updateResult = allocateService.batchAllocate(allocateDTO.getIds(),allocateDTO.getEmpId(),userInfoDTO,token);
            if (updateResult == true) {
                theaApiDTO.setResult(CommonMessage.ALLOCATE_SUCCESS.getCode());
                theaApiDTO.setMessage(CommonMessage.ALLOCATE_SUCCESS.getCodeDesc());
            } else {
                logger.error("-------------->allocateLoan分配失败");
                theaApiDTO.setResult(CommonMessage.ALLOCATE_FAIL.getCode());
                theaApiDTO.setMessage(CommonMessage.ALLOCATE_FAIL.getCodeDesc());
                throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
            }
        }catch (Exception e){
            logger.error("-------------->allocateLoan分配失败",e);
            theaApiDTO.setResult(CommonMessage.ALLOCATE_FAIL.getCode());
            theaApiDTO.setMessage(CommonMessage.ALLOCATE_FAIL.getCodeDesc());
        }
        return theaApiDTO;
    }
    @ApiOperation(value="根据交易id查看分配日志", notes="根据交易id查看分配日志")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerId", value = "客户id", required = true, paramType = "query",  dataType = "int"),
    })
    @RequestMapping(value = "/allocateLogList", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<List<AllocateLogDTO>> listAllocateLog(HttpServletRequest request, @RequestParam Integer customerId){
        CronusDto theaApiDTO =new CronusDto();
        List<AllocateLog> allocateLogList=new ArrayList<AllocateLog>();
        List<AllocateLogDTO> allocateLogDTOS=new ArrayList<AllocateLogDTO>();

        String token=request.getHeader("Authorization");
        try{
            allocateLogList=allocateLogService.listByCondition(customerId);
            if (allocateLogList.size() > 0){
                for (AllocateLog allocateLog:allocateLogList){
                    AllocateLogDTO allocateLogDTO=new AllocateLogDTO();
                    allocateLogDTO = allocateLogService.copyProperty(allocateLog);
                    //查找旧业务员姓名
                    SimpleUserInfoDTO simpleUserInfoDTO = null;
                    if (allocateLogDTO.getOldOwnerId() == null || allocateLogDTO.getOldOwnerId() == 0){
                        allocateLogDTO.setOldOwnerName(null);
                    }else {
                         simpleUserInfoDTO = ucService.getSystemUserInfo(token, allocateLogDTO.getOldOwnerId());
                        if (simpleUserInfoDTO == null) {
                            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
                        }
                        allocateLogDTO.setOldOwnerName(simpleUserInfoDTO.getName());
                    }
                    if (allocateLogDTO.getNewOwnerId() == null || allocateLogDTO.getNewOwnerId() == 0){
                        allocateLogDTO.setOldOwnerName(null);
                    }else {
                        simpleUserInfoDTO = ucService.getSystemUserInfo(token, allocateLogDTO.getNewOwnerId());
                        if (simpleUserInfoDTO == null) {
                            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
                        }
                        allocateLogDTO.setNewOwnerName(simpleUserInfoDTO.getName());
                    }

                    allocateLogDTOS.add(allocateLogDTO);
                }
            }
            theaApiDTO.setResult(CommonMessage.SUCCESS.getCode());
            theaApiDTO.setMessage(CommonMessage.SUCCESS.getCodeDesc());
        }catch (Exception e){
            logger.error("根据交易id查看分配日志",e);
            theaApiDTO.setResult(CommonMessage.FAIL.getCode());
            theaApiDTO.setMessage(CommonMessage.FAIL.getCodeDesc());
        }
        theaApiDTO.setData(allocateLogDTOS);
        return theaApiDTO;
    }


}
