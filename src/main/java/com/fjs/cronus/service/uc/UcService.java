package com.fjs.cronus.service.uc;

import com.fjs.cronus.Common.ResultResource;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.UserDTO;
import com.fjs.cronus.dto.UserInfoDTO;
import com.fjs.cronus.dto.cronus.BaseUcDto;
import com.fjs.cronus.dto.cronus.UcUserDto;
import com.fjs.cronus.dto.uc.BaseUcDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.service.client.ThorInterfaceService;
import com.fjs.cronus.service.redis.UcRedisService;
import com.fjs.cronus.util.FastJsonUtils;
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
    @Autowired
    UcRedisService ucRedisService;

    public List getSubUserByUserId(String token,Integer user_id){
        CronusDto resultDto = new CronusDto();
        if (user_id == null){
            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
        }
        //TODO 从缓存中取数据如果没有在查接口
        List  resultList = ucRedisService.getRedisUcInfo(ResultResource.SUBUSERBYIDS + user_id);
        if (resultList!=null && resultList.size() > 0 ){
            return  resultList;
        }else {
            //查接口先查看用户的数据权限
            List idList = new ArrayList();
            UcUserDto ucUserDto = getUserInfoByID(token,user_id);
            if (ucUserDto ==null){
                throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
            }
            Integer data_type = Integer.valueOf(ucUserDto.getData_type());
            if (data_type == 1){
                //TODO 只能查看自己并把结果存入缓存并设置好失效时间
                idList.add(user_id);
                //插入缓存
                ucRedisService.setRedisUcInfo(ResultResource.SUBUSERBYIDS + user_id,idList);
                return  idList;
            }
            BaseUcDTO baseDto = thorInterfaceService.getSubUserByUserId(token,user_id,data_type);
            if (baseDto.getRetData() != null){
                //转json
                String result = FastJsonUtils.obj2JsonString(baseDto.getRetData());
                idList = FastJsonUtils.getStringList(result);
            }

            ucRedisService.setRedisUcInfo(ResultResource.SUBUSERBYIDS + user_id ,idList);
            return  idList;
        }
    }

    public UcUserDto getUserInfoByID(String token,Integer user_id){
        //TODO 差缓存是否存在用户信息 不存在 插接口
        UcUserDto ucUserDto = null;
        ucUserDto = ucRedisService.getRedisUserInfo(ResultResource.USERINFOBYID + user_id);
        if (ucUserDto != null){
            return ucUserDto;
        }
        BaseUcDTO ucDTO = thorInterfaceService.getUserInfoByField(token,null,user_id,null);
        if (ucDTO.getRetData() !=null){
            //map 转json
            String result = FastJsonUtils.obj2JsonString(ucDTO.getRetData());
            //把json格式的数据转为对象

            ucUserDto  = FastJsonUtils.getSingleBean(result,UcUserDto.class);
            //TODO 信息存入缓存 并设置失效时间
            ucRedisService.setRedisUserInfo(ResultResource.USERINFOBYID + user_id + ucUserDto.getUser_id(),ucUserDto);

        }
        return  ucUserDto;
    }

    //校验是否有权限进行编辑
    public Integer getUserIdByToken(String token){
        Integer user_id = null;
        CronusDto resultDto = new CronusDto();
        //根据token查询当前用户id
        String result = thorInterfaceService.getCurrentUserInfo(token,null);
        BaseUcDto dto = FastJsonUtils.getSingleBean(result,BaseUcDto.class);
        System.out.println(dto.getData().toString());
        UcUserDto userDTO = FastJsonUtils.getSingleBean(dto.getData().toString(),UcUserDto.class);
        if (userDTO != null){
            user_id  = Integer.valueOf(userDTO.getUser_id());
        }
        return  user_id;
    }

}
