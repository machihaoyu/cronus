package com.fjs.cronus.service;

import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.UserDTO;
import com.fjs.cronus.dto.uc.BaseUcDTO;
import com.fjs.cronus.dto.uc.UserInfoDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.CustomerInterviewBaseInfoMapper;
import com.fjs.cronus.model.CustomerInterviewBaseInfo;
import com.fjs.cronus.service.client.ThorInterfaceService;
import com.fjs.cronus.util.EntityToDto;
import com.fjs.cronus.util.FastJsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by msi on 2017/9/15.
 */
@Service
public class CustomerInterviewService {
    @Autowired
    CustomerInterviewBaseInfoMapper customerInterviewBaseInfoMapper;
    @Autowired
    ThorInterfaceService thorInterfaceService;
    public CronusDto customerInterviewList(String token,String name,String loanAmount,String industry,String feeChannelName,String productName,String ownerUserName,
                                           String telephonenumber,String householdRegister,Integer page,Integer size){
        CronusDto resultDto = new CronusDto();
        Map<String,Object> paramsMap =  new HashMap<>();
        //TODO 通过token查询到用户的id 查询自己以及下属员工
        String  result = thorInterfaceService.getCurrentUserInfo(token,null);
        try {
            BaseUcDTO<UserInfoDTO> dto = FastJsonUtils.getSingleBean(result,BaseUcDTO.class);
            UserInfoDTO userDTO = dto.getRetData();
            List idList = new ArrayList();
            if (userDTO.getData_type() == null || !"".equals(userDTO.getData_type())){
                throw new CronusException(CronusException.Type.CRM_DATAAUTH_ERROR);
            }
            Integer data_type = Integer.valueOf(userDTO.getData_type());
            if (data_type == 1){
                //只能查看自己的权限
                idList.add(userDTO.getUser_id());
                //TODO 加入缓存并设置好缓存时间
            }else{
                BaseUcDTO ucDTO = thorInterfaceService.getSubUserByUserId(token,Integer.valueOf(userDTO.getUser_id()),data_type);
                //获取到下属员工
                idList = (List) ucDTO.getRetData();
                //TODO 加入缓存并设置好缓存时间
            }
            //拼装参数
            if (!StringUtils.isEmpty(name)){
                paramsMap.put("name",name);
            }
            if (!StringUtils.isEmpty(loanAmount)){
                paramsMap.put("loanAmount",loanAmount);
            }
            if (!StringUtils.isEmpty(industry)){
                paramsMap.put("industry",industry);
            }
            if (!StringUtils.isEmpty(feeChannelName)){
                paramsMap.put("feeChannelName",feeChannelName);
            }
            if (!StringUtils.isEmpty(productName)){
                paramsMap.put("productName",productName);
            }
            if (!StringUtils.isEmpty(ownerUserName)){
                paramsMap.put("ownerUserName",ownerUserName);
            }
            if (!StringUtils.isEmpty(telephonenumber)){
                //TODO 手机号需要经过加密处理
                String encrptTelephonenumber = "";
                List paramsList = new ArrayList();
                paramsList.add(encrptTelephonenumber);
                paramsList.add(telephonenumber);
                paramsMap.put("paramsList",paramsList);

            }
            if (!StringUtils.isEmpty(householdRegister)){
                paramsMap.put("householdRegister",householdRegister);
            }
            paramsMap.put("start",(page-1) * size);
            paramsMap.put("size",size);
            List<CustomerInterviewBaseInfo> customerInterviewBaseInfos = customerInterviewBaseInfoMapper.customerInterviewList(paramsMap);
            if (customerInterviewBaseInfos.size() > 0){
                for (CustomerInterviewBaseInfo customerInterviewBaseInfo : customerInterviewBaseInfos) {
                    //EntityToDto.customerEntityToCustomerDto();
                }
            }
        }catch (Exception e){

        }
        return  resultDto;
    }
}
