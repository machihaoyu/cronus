package com.fjs.cronus.service;

import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonEnum;
import com.fjs.cronus.Common.ResultResource;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.api.uc.AppUserDto;
import com.fjs.cronus.dto.api.uc.SubCompanyDto;
import com.fjs.cronus.dto.cronus.SellUserDTO;
import com.fjs.cronus.dto.thea.AllLoanDTO;
import com.fjs.cronus.dto.uc.UserInfoDTO;
import com.fjs.cronus.exception.CronusException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.w3c.dom.ls.LSInput;

import java.util.*;

/**
 * Created by msi on 2017/12/4.
 */
@Service
public class AllocateService {

    @Autowired
    CustomerInfoMapper customerInfoMapper;
    @Autowired
    AllocateLogService allocateLogService;
    @Autowired
    UcService ucService;
    @Autowired
    CustomerInfoLogMapper customerInfoLogMapper;
    @Autowired
    TheaClientService theaClientService;
    @Autowired
    TheaService theaService;
    public CronusDto sellUser(String token,String customer_ids,String action,Integer userId){
        CronusDto resultDto = new CronusDto();
        SellUserDTO sellUserDTO = new SellUserDTO();
        String urlComeFrom =null;
        boolean flag = validCustomerAllIsOperate(customer_ids);
        if (CommonConst.REMOVECUSTOMERALL.equals(action)){
            //判断客户存在不存在首次分配未处理的的
            if (flag == true){
                 urlComeFrom = CommonConst.REMOVECUSTOMERALL;
            }
        }else if (CommonConst.ALLOCATEALL.equals(action)){
             urlComeFrom = CommonConst.ALLOCATEALL;
        }
       //获取可以操作的城市
        List<SubCompanyDto> companyList= ucService.getAllCompanyByUserId(token,userId,CommonConst.SYSTEM_NAME_ENGLISH);
        sellUserDTO.setCompany(companyList);
        sellUserDTO.setUrlComeFrom(urlComeFrom);
        resultDto.setData(sellUserDTO);
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        return  resultDto;
    }
    public boolean validCustomerAllIsOperate(String customer_ids){
        boolean flag = false;
        Map<String,Object> paramsMap = new HashMap<>();
        if (StringUtils.isEmpty(customer_ids)){
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        //ids 转成list
        List<Integer> ids = new ArrayList<>();
        String[] strArray = null;
        strArray = customer_ids.split(",");
        for (int i = 0; i < strArray.length; i++) {
            ids.add(Integer.parseInt(strArray[i]));
        }
        paramsMap.put("paramsList", ids);
        List<CustomerInfo> customerInfoList = customerInfoMapper.findCustomerListByFeild(paramsMap);
        //下面查询这些客户最近的分配记录
        Map<Integer,AllocateLog> allocateLogs = allocateLogService.getNewestAllocateLogByCustomerIds(customer_ids);
        //检测存不存在未沟通的新分配客户,如果存在就报错
        if (allocateLogs != null && !allocateLogs.isEmpty()) {
            for (Integer id : ids) {
                AllocateLog allocateLog = allocateLogs.get(id);
                if (CommonConst.OPERARIONAll.equals(allocateLog.getOperation()) || CommonConst.OPERARIONNO.equals(allocateLog.getOperation())) {
                    throw new CronusException(CronusException.Type.CRM_CUSOMERALLACATE_ERROR);
                }
            }
        }
        flag = true;
        return  flag;
    }

    @Transactional
    public boolean batchAllocate(String ids, Integer empId,UserInfoDTO userInfoDTO,String token) {
        boolean flag = false;
        Integer userId = null;
        //ids 转为list
        List<String> nameList = new ArrayList<>();
        Date date = new Date();
        Map<String,Object> map=new HashMap<>();
        List<Integer> paramsList = new ArrayList<>();
        if (ids != null && !"".equals(ids)) {
            String[] strArray = null;
            strArray = ids.split(",");
            for (int i = 0; i < strArray.length; i++) {
                paramsList.add(Integer.parseInt(strArray[i]));
            }
        }
        //去重复操作
        List<Integer> uniqueList = new ArrayList<Integer>(new HashSet<>(paramsList));
        if (StringUtils.isEmpty(ids)){
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        AppUserDto userInfoByID= ucService.getUserInfoByID(token,empId);
        //判断客户存在不存在首次分配未处理的的
        boolean result= validCustomerAllIsOperate(ids);
        if (result == true){
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(userInfoDTO.getUser_id())) {
                userId = Integer.parseInt(userInfoDTO.getUser_id());
            }
            map.put("ownUserId",empId);
            map.put("idList",uniqueList);
            map.put("lastUpdateUser",userId);
            map.put("lastUpdateTime",date);
            map.put("paramsList",uniqueList);
            map.put("own_user_name",userInfoByID.getName());
            //判断是否是首次分配
            Map<String,Object> idMap=new HashMap<>();
            idMap.put("paramsList",uniqueList);
            StringBuffer stringBuffer = new StringBuffer();
            List<CustomerInfo> customerInfoList = customerInfoMapper.findCustomerListByFeild(idMap);
            if (customerInfoList.size() > 0){
                for (CustomerInfo customerInfo: customerInfoList){
                    nameList.add(customerInfo.getCustomerName());
                    if (customerInfo.getFirstAllocateTime() == null){
                        customerInfo.setFirstAllocateTime(date);
                        customerInfo.setReceiveTime(date);
                        //开始更新客户
                        customerInfo.setLastUpdateUser(userId);
                        customerInfo.setLastUpdateTime(date);
                        //插入日志
                        CustomerInfoLog customerInfoLog = new CustomerInfoLog();
                        EntityToDto.customerEntityToCustomerLog(customerInfo,customerInfoLog);
                        customerInfoLog.setLogCreateTime(date);
                        customerInfoLog.setLogDescription(CommonEnum.LOAN_OPERATION_TYPE_7.getCodeDesc());
                        customerInfoLog.setLogUserId(userId);
                        customerInfoLog.setIsDeleted(0);
                        customerInfoLogMapper.addCustomerLog(customerInfoLog);
                        //开始更新客户
                        customerInfoMapper.updateCustomer(customerInfo);
                    }
                }
                //开始分配
                customerInfoMapper.batchAllocate(map);
                flag = true;
                try {
                    //TODO 开始修改交易
                    AllLoanDTO allLoanDTO = new AllLoanDTO();
                    allLoanDTO.setIds(ids);
                    allLoanDTO.setNewOwnnerId(empId);
                    theaService.deleteLoanByCustomerId(token,allLoanDTO);
                    String names = listToString(nameList);
                    String content = userInfoDTO.getName() +"分配给了你"+ customerInfoList.size()+"个客户,"+"客户名分别是"+names+ "请注意跟进。";
                    theaClientService.sendMail(token,content,userId,userId,null,empId);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return  flag;
    }

    public String listToString(List list){

        StringBuilder str = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1)//当循环到最后一个的时候 就不添加逗号,
            {
                str.append(list.get(i));
            } else {
                str.append(list.get(i));
                str.append(",");
            }

        }
        return  str.toString();
    }

}
