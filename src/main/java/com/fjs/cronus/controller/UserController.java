package com.fjs.cronus.controller;

import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonEnum;
import com.fjs.cronus.Common.CommonMessage;
import com.fjs.cronus.Common.ResultDescription;
import com.fjs.cronus.api.PhpApiDto;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.EditAllocateDTO;
import com.fjs.cronus.dto.EditUserMonthInfoDTO;
import com.fjs.cronus.dto.UserMonthInfoDTO;
import com.fjs.cronus.dto.api.PhpQueryResultDto;
import com.fjs.cronus.dto.api.TheaApiDTO;
import com.fjs.cronus.dto.api.uc.PhpDepartmentModel;
import com.fjs.cronus.dto.api.uc.SubCompanyDto;
import com.fjs.cronus.dto.uc.CrmCitySubCompanyDto;
import com.fjs.cronus.dto.uc.ThorQueryDto;
import com.fjs.cronus.dto.uc.UserInfoDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.model.AllocateLog;
import com.fjs.cronus.model.UserMonthInfo;
import com.fjs.cronus.service.AllocateLogService;
import com.fjs.cronus.service.UserMonthInfoService;
import com.fjs.cronus.service.UserService;
import com.fjs.cronus.service.client.ThorService;
import com.fjs.cronus.service.redis.AllocateRedisService;
import com.fjs.cronus.util.DateUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 员工的相关信息
 * Created by yinzf on 2017/10/9.
 */
@Controller
@RequestMapping("/uc/v1")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

//    @Autowired
//    private ThorUcService thorUcService;

    @Autowired
    private ThorService thorService;

    @Autowired
    private UserService userService;


    @Autowired
    private AllocateRedisService allocateRedisService;

    @Autowired
    private AllocateLogService allocateLogService;

    @Autowired
    private UserMonthInfoService userMonthInfoService;

    @ApiOperation(value="得到下属员工", notes="得到下属员工")
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
    public PhpQueryResultDto getSubUserByUserId(HttpServletRequest request, Integer subCompanyId, String flag,
                                                String name, @RequestParam("page") Integer page,
                                                @RequestParam("pageSize") Integer pageSize) {
        PhpQueryResultDto resultDto = new PhpQueryResultDto();
        //获取当前用户信息
        String token = request.getHeader("Authorization");
        CronusDto<UserInfoDTO> thorApiDTO = thorService.getUserInfoByToken(token, CommonConst.SYSTEMNAME);
        UserInfoDTO userInfoDTO = thorApiDTO.getData();
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
        ThorQueryDto subThorApiDTO = null;
        try {
            PhpApiDto<List<String>> phpApiDto = thorService.getSubUserByUserId(token, user_id, CommonConst.SYSTEMNAME, dataType);
            List<String> list = phpApiDto.getRetData();
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
            subThorApiDTO = thorService.getUserByIds(token, null, null, subCompanyId,
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

    @ApiOperation(value = "获取公司组织架构", notes = "获取公司组织架构")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string"),
    })
    @RequestMapping(value = "/getDepartmentByWhere", method = RequestMethod.GET)
    @ResponseBody
    public TheaApiDTO<List<SubCompanyDto>> getDepartmentByWhere(HttpServletRequest request) {
        TheaApiDTO<List<SubCompanyDto>> theaApiDTO = new TheaApiDTO<List<SubCompanyDto>>();
        List<SubCompanyDto> subCompanyDtos = new ArrayList<>();
        //获取公司信息
        String token = request.getHeader("Authorization");
        CronusDto<UserInfoDTO> thorApiDTO = thorService.getUserInfoByToken(token, CommonConst.SYSTEMNAME);
        UserInfoDTO userInfoDTO = thorApiDTO.getData();
        Integer user_id = null;
        Integer dataType = null;
        if (userInfoDTO != null && StringUtils.isNotEmpty(userInfoDTO.getUser_id())) {
            user_id = Integer.parseInt(userInfoDTO.getUser_id());
        }
        if (userInfoDTO != null && StringUtils.isNotEmpty(userInfoDTO.getData_type())) {
            dataType = Integer.parseInt(userInfoDTO.getData_type());
        }
        if (user_id == 0) {
            theaApiDTO.setResult(CommonMessage.FAIL.getCode());
            theaApiDTO.setMessage("获取部门信息出错");
            logger.error("--------->getDepartmentByWhere，获取部门信息出错");
            return theaApiDTO;
        }
        try {
            subCompanyDtos = userService.getDepartmentByWhere(token, user_id);
            theaApiDTO.setResult(CommonMessage.SUCCESS.getCode());
            theaApiDTO.setMessage(CommonMessage.SUCCESS.getCodeDesc());
        } catch (Exception e) {
            theaApiDTO.setResult(CommonMessage.FAIL.getCode());
            theaApiDTO.setMessage("获取公司信息出错");
            logger.error("--------->getDepartmentByWhere，获取部门信息出错", e);
        }
        theaApiDTO.setData(subCompanyDtos);
        return theaApiDTO;
    }

    @ApiOperation(value = "根据公司获取下面的子公司", notes = "根据公司获取下面的子公司")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string"),
            @ApiImplicitParam(name = "companyId", value = "公司id", required = true, paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/getSubCompanys", method = RequestMethod.GET)
    @ResponseBody
    public TheaApiDTO<List<PhpDepartmentModel>> getSubCompanys(HttpServletRequest request, Integer companyId) {
        TheaApiDTO<List<PhpDepartmentModel>> theaApiDTO = new TheaApiDTO<List<PhpDepartmentModel>>();
        List<PhpDepartmentModel> list = null;
        //获取分公司信息
        String token = request.getHeader("Authorization");
        CronusDto<UserInfoDTO> thorApiDTO = thorService.getUserInfoByToken(token, CommonConst.SYSTEMNAME);
        UserInfoDTO userInfoDTO = thorApiDTO.getData();
        Integer user_id = null;
        if (userInfoDTO != null && StringUtils.isNotEmpty(userInfoDTO.getUser_id())) {
            user_id = Integer.parseInt(userInfoDTO.getUser_id());
        }
        try {
            list = userService.getSubCompanys(token, companyId);
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

    @ApiOperation(value = "获取分配队列", notes = "根据城市获取分配队列")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string"),
            @ApiImplicitParam(name = "city", value = "城市", required = true, paramType = "query", dataType = "String", defaultValue = "上海")
    })
    @RequestMapping(value = "/getAllocateQueue", method = RequestMethod.GET)
    @ResponseBody
    public TheaApiDTO<List<Map<String, String>>> getAllocateQueue(@RequestParam("city") String city) {
        TheaApiDTO<List<Map<String, String>>> resultDTO = new TheaApiDTO<>();
        try {
            List<Map<String, String>> map = userService.getAllocateQueue(city);
            resultDTO.setData(map);
            resultDTO.setResult(ResultDescription.CODE_SUCCESS);
            resultDTO.setMessage(ResultDescription.MESSAGE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("-----------查询城市分配队列失败！！--------" + e);
            resultDTO.setData(null);
            resultDTO.setResult(ResultDescription.CODE_FAIL);
            resultDTO.setMessage(ResultDescription.MESSAGE_FAIL);
        }
        return resultDTO;
    }

    @ApiOperation(value = "根据分公司ID获取所有业务员分配列表", notes = "根据分公司ID获取所有业务员分配列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string"),
            @ApiImplicitParam(name = "companyId", value = "公司Id", required = true, paramType = "query", dataType = "Integer", defaultValue = "1"),
            @ApiImplicitParam(name = "effectiveDate", value = "查询时间", required = true, paramType = "query", dataType = "String", defaultValue = "201710"),
            @ApiImplicitParam(name = "city", value = "城市", required = true, paramType = "query", dataType = "String", defaultValue = "上海")
    })
    @RequestMapping(value = "/getUsersByCompanyId", method = RequestMethod.GET)
    @ResponseBody
    public TheaApiDTO<Map<String, List<UserMonthInfoDTO>>> getUsersByConpamyId(
            @RequestParam(required = true) String city,
            @RequestParam(required = true) Integer companyId,
            @RequestParam(required = true) String effectiveDate
    ) {
        TheaApiDTO<Map<String, List<UserMonthInfoDTO>>> resultDTO = new TheaApiDTO<>();
        //判断输入的查询时间是否为
        //"201701"
        if (StringUtils.isBlank(effectiveDate) || !effectiveDate.matches("[0-9]{6}")) {
            throw new CronusException(CronusException.Type.CEM_CUSTOMERINTERVIEW);
        }
        try {
            Map<String, List<UserMonthInfoDTO>> map = new HashMap<>();
            Integer userId = Integer.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
            map = userService.getUserMonthInfoList(city, companyId, effectiveDate, userId);
            resultDTO.setData(map);
            resultDTO.setResult(ResultDescription.CODE_SUCCESS);
            resultDTO.setMessage(ResultDescription.MESSAGE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("-----------查询业务员分配列表失败！！--------" + e);
            resultDTO.setData(null);
            resultDTO.setResult(ResultDescription.CODE_FAIL);
            resultDTO.setMessage(ResultDescription.MESSAGE_FAIL);
        }
        return resultDTO;
    }


    @ApiOperation(value = "用户可操作的分公司", notes = "获取用户可操作的分公司")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string")
    })
    @RequestMapping(value = "/getOperateCompany", method = RequestMethod.GET)
    @ResponseBody
    public TheaApiDTO<List<CrmCitySubCompanyDto>> getOperateCompany(
            HttpServletRequest request
    ) {
        TheaApiDTO<List<CrmCitySubCompanyDto>> resultDTO = new TheaApiDTO<>();
        String token = request.getHeader("Authorization");
        Integer userId = Integer.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        try {
            List<CrmCitySubCompanyDto> crmCitySubCompanyDtoList = userService.getOperateCompany(token, userId);
            resultDTO.setData(crmCitySubCompanyDtoList);
            resultDTO.setResult(ResultDescription.CODE_SUCCESS);
            resultDTO.setMessage(ResultDescription.MESSAGE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("-----------查询用户可操作的分公司失败！！--------" + e);
            resultDTO.setData(null);
            resultDTO.setResult(ResultDescription.CODE_FAIL);
            resultDTO.setMessage(e.getMessage());
        }
        return resultDTO;
    }

    @ApiOperation(value = "添加用户至自动分配队列", notes = "根据城市添加用户至自动分配队列")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string")
    })
    @RequestMapping(value = "/addUserToAllocate", method = RequestMethod.POST)
    @ResponseBody
    public TheaApiDTO addUserToAllocate(@RequestBody EditAllocateDTO editAllocateDTO) {
        TheaApiDTO resultDto = new TheaApiDTO();
        if (StringUtils.isBlank(editAllocateDTO.getCity()) ||
                null == editAllocateDTO.getUserId()) {
            throw new CronusException(CronusException.Type.CEM_CUSTOMERINTERVIEW);
        }
        Integer userId = editAllocateDTO.getUserId();
        String city = editAllocateDTO.getCity();
        try {
            String userIds = allocateRedisService.addUserToAllocateTemplete(userId, city);
            resultDto.setResult(CommonMessage.ADD_SUCCESS.getCode());
            resultDto.setMessage(CommonMessage.ADD_SUCCESS.getCodeDesc());
        } catch (Exception e) {
            logger.error("------------添加用户至分配队列失败-----------" + e);
            resultDto.setResult(CommonMessage.ADD_FAIL.getCode());
            resultDto.setMessage(CommonMessage.ADD_FAIL.getCodeDesc());
        }
        return resultDto;
    }

    @ApiOperation(value = "删除用户至自动分配队列", notes = "根据城市删除用户至自动分配队列")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string")
    })
    @RequestMapping(value = "/delUserToAllocate", method = RequestMethod.POST)
    @ResponseBody
    public TheaApiDTO delUserToAllocate(@RequestBody EditAllocateDTO editAllocateDTO) {
        TheaApiDTO resultDto = new TheaApiDTO();
        if (StringUtils.isBlank(editAllocateDTO.getCity()) ||
                null == editAllocateDTO.getUserId()) {
            throw new CronusException(CronusException.Type.CEM_CUSTOMERINTERVIEW);
        }
        Integer userId = editAllocateDTO.getUserId();
        String city = editAllocateDTO.getCity();
        try {
            String userIds = allocateRedisService.delUserToAllocateTemplete(userId, city);
            resultDto.setResult(CommonMessage.DELETE_SUCCESS.getCode());
            resultDto.setMessage(CommonMessage.DELETE_SUCCESS.getCodeDesc());
        } catch (Exception e) {
            logger.error("------------删除用户至分配队列失败-----------" + e);
            resultDto.setResult(CommonMessage.DELETE_SUCCESS.getCode());
            resultDto.setMessage(CommonMessage.DELETE_SUCCESS.getCodeDesc());
        }
        return resultDto;
    }

    @ApiOperation(value = "修改业务员月度分配数据", notes = "修改业务员月度分配数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string")
    })
    @RequestMapping(value = "/editUserMonthInfo", method = RequestMethod.POST)
    @ResponseBody
    public TheaApiDTO editUserMonthInfo(@RequestBody EditUserMonthInfoDTO editUserMonthInfoDTO) throws Exception {
        TheaApiDTO resultDto = new TheaApiDTO();
        Integer userId = editUserMonthInfoDTO.getUserId();
        String effectiveDate = editUserMonthInfoDTO.getEffectiveDate();
        Integer baseCustomerNum = editUserMonthInfoDTO.getBaseCustomerNum();
        Integer rewardCustomerNum = editUserMonthInfoDTO.getRewardCustomerNum();
        if (baseCustomerNum < 0 || rewardCustomerNum < 0) {
            resultDto.setResult(CommonMessage.UPDATE_FAIL.getCode());
            resultDto.setMessage(CronusException.Type.CEM_CUSTOMERINTERVIEW.getError());
        }
        else {
            //获取用户的已分配数
            //获取这些业务员的自动分配数和自动确认数
            Map<String, Object> allocateMap = new HashMap<>();
            allocateMap.put("inOperation", CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_1.getCodeDesc() +
                    "," + CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_3.getCodeDesc());
            List<Integer> userIds = new ArrayList<>();
            userIds.add(userId);
            allocateMap.put("newOwnerIds", userIds);
            allocateMap.put("createBeginDate", DateUtils.getBeginDateByStr(effectiveDate));
            allocateMap.put("operationsStr", CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_1.getCodeDesc() +
                    "," + CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_3.getCodeDesc());
            allocateMap.put("createEndDate", DateUtils.getEndDateByStr(effectiveDate));
            List<AllocateLog> allocateLogList = allocateLogService.selectByParamsMap(allocateMap);
            if (allocateLogList.size() >= (baseCustomerNum + rewardCustomerNum)) {
                resultDto.setResult(CommonMessage.UPDATE_FAIL.getCode());
                resultDto.setMessage(CronusException.Type.ALLOCATE_NUM_ERROR.getError());
            } else {
                Integer updateUserId = Integer.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
                try {
                    UserMonthInfo userMonthInfo = new UserMonthInfo();
                    userMonthInfo.setLastUpdateUser(updateUserId);
                    userMonthInfo.setBaseCustomerNum(baseCustomerNum);
                    userMonthInfo.setRewardCustomerNum(rewardCustomerNum);
                    userMonthInfo.setLastUpdateTime(new Date());
                    userMonthInfo.setUserId(userId);
                    userMonthInfo.setEffectiveDate(effectiveDate);
                    userMonthInfoService.saveOne(userMonthInfo);
                    resultDto.setResult(CommonMessage.UPDATE_SUCCESS.getCode());
                    resultDto.setMessage(CommonMessage.UPDATE_SUCCESS.getCodeDesc());
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("------------更新业务员月度分配信息失败-------" + e);
                    resultDto.setResult(CommonMessage.UPDATE_FAIL.getCode());
                    resultDto.setMessage(CommonMessage.UPDATE_FAIL.getCodeDesc());
                }
            }
        }
        return resultDto;
    }

}
