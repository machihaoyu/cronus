package com.fjs.cronus.service;

import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonEnum;
import com.fjs.cronus.api.thea.LoanDTO;
import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.api.PHPLoginDto;
import com.fjs.cronus.dto.cronus.CustomerDTO;
import com.fjs.cronus.dto.cronus.CustomerListDTO;
import com.fjs.cronus.dto.cronus.PanParamDTO;
import com.fjs.cronus.dto.cronus.UcUserDTO;
import com.fjs.cronus.dto.loan.TheaApiDTO;
import com.fjs.cronus.dto.uc.UserInfoDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.AllocateLogMapper;
import com.fjs.cronus.mappers.CustomerInfoLogMapper;
import com.fjs.cronus.mappers.CustomerInfoMapper;
import com.fjs.cronus.model.AllocateLog;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.model.CustomerInfoLog;
import com.fjs.cronus.service.client.TheaService;
import com.fjs.cronus.service.thea.TheaClientService;
import com.fjs.cronus.service.uc.UcService;
import com.fjs.cronus.util.DateUtils;
import com.fjs.cronus.util.EntityToDto;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Table;
import java.util.*;

/**
 * Created by msi on 2017/11/30.
 */
@Service
public class PanService {

    @Autowired
    CustomerInfoMapper customerInfoMapper;
    @Autowired
    TheaClientService theaClientService;
    @Autowired
    AllocateLogMapper allocateLogMapper;
    @Autowired
    CustomerInfoService customerInfoService;
    @Autowired
    UcService ucService;
    @Autowired
    CustomerInfoLogMapper customerInfoLogMapper;
    @Autowired
    TheaService theaService;
    public QueryResult<CustomerListDTO> listByOffer(PanParamDTO pan, Integer userId, Integer companyId , String token, String system,
                                                Integer page, Integer size, List<String> mainCitys, List<Integer> subCompanyIds, Integer type,Integer mountLevle,List<String> utmList,List<String>paramsList) {

        QueryResult<CustomerListDTO> result = new QueryResult<>();
        Map<String,Object> map=new HashedMap();
        List<CustomerListDTO> resultDto = new ArrayList<>();
        if (pan != null){
            //客户姓名
            if (StringUtils.isNotEmpty(pan.getCustomerName())) {
                map.put("customerName",pan.getCustomerName());
            }
            //电话
            if (StringUtils.isNotEmpty(pan.getTelephonenumber())) {
                map.put("telephonenumber", pan.getTelephonenumber());
            }
            //合作状态
            if (StringUtils.isNotEmpty(pan.getCustomerClassify())) {
                map.put("cooperation_status", pan.getCustomerClassify());
            }
            //公司
            if (companyId != null) {
                map.put("companyId", companyId);
            }
            if (StringUtils.isNotEmpty(pan.getUtmSource())) {
                map.put("utmSource",pan.getUtmSource());
            }
            if (StringUtils.isNotEmpty(pan.getCity())) {
                map.put("city",pan.getCity());
            }
            if (StringUtils.isNotEmpty(pan.getCustomerSource())){
                map.put("customer_source",pan.getCustomerSource());
            }
            if (mountLevle != null){
                map.put("mountLevle",mountLevle);
            }
            map.put("mainCitys",mainCitys);
            map.put("subCompanyIds",subCompanyIds);
            map.put("type",type);
            map.put("start",(page-1)*size);
            map.put("size",size);
            if (utmList != null && utmList.size() > 0){
                map.put("utmList",utmList);
            }
            if (paramsList != null && paramsList.size() > 0){
                map.put("paramsList",paramsList);
            }
            PHPLoginDto userInfoDTO = ucService.getAllUserInfo(token,CommonConst.SYSTEM_NAME_ENGLISH);
            if (userInfoDTO == null){
                throw new CronusException(CronusException.Type.CRM_CALLBACKCUSTOMER_ERROR);
            }
            Integer lookphone =Integer.parseInt(userInfoDTO.getUser_info().getLook_phone());
            Integer user_Id = Integer.parseInt(userInfoDTO.getUser_info().getUser_id());
            List<CustomerInfo> customerInfoList = customerInfoMapper.publicOfferList(map);
            Integer total = customerInfoMapper.publicOfferCount(map);
            if (customerInfoList != null && customerInfoList.size() > 0) {
                for (CustomerInfo customerInfo : customerInfoList){
                    CustomerListDTO customerDto = new CustomerListDTO();
                    EntityToDto.customerEntityToCustomerListDto(customerInfo,customerDto,lookphone,userId);
                    resultDto.add(customerDto);
                }
            }
            result.setRows(resultDto);
            result.setTotal(total.toString());
        }
        return  result;
    }
    @Transactional
    public boolean pullPan(Integer customerId,Integer userId,String token,String userName){
       //判断清洗中不能领取客户每周日的八点开始进行自动清洗
        Map<String,Object> paramMap = new HashMap<>();
        boolean flag = false;
        Date date = new Date();
        if (DateUtils.dayForWeek(date) == 7 && DateUtils.getHour(date) == 20 && DateUtils.getMinute(date) < 10){
            throw new CronusException(CronusException.Type.MESSAGE_CUSTOMERCLEAN_ERROR);
        }
        //判断是否是自动清洗的状态
        String status = theaClientService.findValueByName(token, CommonConst.AUTO_CLEAN_STATUS);
        if (Integer.parseInt(status) == 1){
            throw new CronusException(CronusException.Type.MESSAGE_CUSTOMERCLEAN_ERROR);
        }
        //下面判断一天之内领取的客户是否超限
        String maxCount = theaClientService.findValueByName(token, CommonConst.CANPUUMAXCOUNT);
        //查询当前业务员领取的个数
        paramMap.put("createUserId",userId);
        paramMap.put("operation",CommonConst.OPERATION);
        String  today = DateUtils.format(date,DateUtils.FORMAT_SHORT);
        paramMap.put("operation",CommonConst.OPERATION);
        paramMap.put("createTime",today);
        Integer count = allocateLogMapper.receiveCountByWhere(paramMap);
        if (count >= Integer.parseInt(maxCount)){
            throw new CronusException(CronusException.Type.MESSAGE_PULLCUSTOMERCOUNT_ERROR);
        }
        //找到客户信息
        CustomerInfo customerInfo = customerInfoService.findCustomerById(customerId);
        if (customerInfo == null){
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        //开始改变装太
        receiveCustomerByType(customerInfo,userId,token);
        //增加分配日志
        AllocateLog allocateLog = new AllocateLog();
        allocateLog.setCreateTime(new Date());
        allocateLog.setCustomerId(customerInfo.getId());
        allocateLog.setOldOwnerId(0);
        allocateLog.setNewOwnerId(userId);
        allocateLog.setCreateUserId(userId);
        allocateLog.setCreateUserName(userName);
        allocateLog.setOperation(CommonEnum.LOAN_OPERATION_TYPE_2.getCodeDesc());
        //开始插入
        allocateLog.setResult(CommonConst.OPERATIONSUCESS);
        Integer insertAllocateLog = allocateLogMapper.insert(allocateLog);
        if (null == insertAllocateLog) {
            throw new CronusException(CronusException.Type.CRM_CUSTOMERLOG_ERROR);
        }
        flag = true;
        return flag;

    }

    /**
     * 领取客户
     * @param customerInfo
     * @param userId
     * @param token
     */
    @Transactional
    public void receiveCustomerByType(CustomerInfo customerInfo,Integer userId,String token){
        UserInfoDTO ucUserDTO = ucService.getUserInfoByID(token,userId);
        Date date = new Date();
        if (customerInfo.getOwnUserId() != 0){
            throw new CronusException(CronusException.Type.MESSAGE_PULLCUSTOMEROWNER_ERROR);
        }
        customerInfo.setLastUpdateUser(userId);
        customerInfo.setLastUpdateTime(date);
        customerInfo.setOwnUserId(userId);
        customerInfo.setOwnUserName(ucUserDTO.getName());
        customerInfo.setReceiveTime(date);
        customerInfo.setSubCompanyId(Integer.valueOf(ucUserDTO.getSub_company_id()));
        //更改领取人
        customerInfo.setReceiveId(userId);
        customerInfoMapper.updateCustomer(customerInfo);

        //开始插入日志
        CustomerInfoLog customerInfoLog = new CustomerInfoLog();
        EntityToDto.customerEntityToCustomerLog(customerInfo,customerInfoLog);
        customerInfoLog.setLogCreateTime(date);
        customerInfoLog.setLogDescription(CommonConst.OPERATION);
        customerInfoLog.setLogUserId(userId);
        customerInfoLog.setIsDeleted(0);
        customerInfoLogMapper.addCustomerLog(customerInfoLog);
    }
}
