package com.fjs.cronus.service.uc;

import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.UserDTO;
import com.fjs.cronus.dto.UserInfoDTO;
import com.fjs.cronus.dto.cronus.UcUserDto;
import com.fjs.cronus.dto.uc.BaseUcDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.service.client.ThorInterfaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by msi on 2017/9/16.
 */
@Service
public class UcService {

    @Autowired
    ThorInterfaceService thorInterfaceService;


    public CronusDto<List> getSubUserByUserId(String token,Integer user_id){
        CronusDto resultDto = new CronusDto();
        List resultList = new ArrayList();
        if (user_id == null){
            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
        }
        //TODO 从缓存中取数据如果没有在查接口
        List list = new ArrayList();
        if (list.size() > 0 ){

        }else {
            //查接口先查看用户的数据权限
            CronusDto ucDTO = getUserInfoByID(token,user_id);
            if (ucDTO.getData() ==null){
                throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
            }
            UcUserDto ucUserDto = (UcUserDto)ucDTO.getData();
            if (ucUserDto.getData_type() == null){
                throw new CronusException(CronusException.Type.CRM_DATAAUTH_ERROR);
            }
            Integer data_type = Integer.valueOf(ucUserDto.getData_type());
            if (data_type == 1){
                //TODO 只能查看自己并把结果存入缓存并设置好失效时间
                resultList.add(user_id);
                resultDto.setData(resultList);
            }
            BaseUcDTO baseDto = thorInterfaceService.getSubUserByUserId(token,user_id,data_type);
            resultList = (List) baseDto.getRetData();
            resultDto.setData(resultList);
        }
        return  resultDto;
    }

    public CronusDto getUserInfoByID(String token,Integer user_id){
        //TODO 差缓存是否存在用户信息 不存在 插接口
        CronusDto resultDto = new CronusDto();
        BaseUcDTO ucDTO = thorInterfaceService.getUserInfoByField(token,null,user_id,null);
        if (ucDTO.getRetData() !=null){
            UcUserDto ucUserDto = (UcUserDto)ucDTO.getRetData();
            //TODO 信息存入缓存 并设置失效时间
            resultDto.setData(ucUserDto);

        }
        return  resultDto;



    }

}
