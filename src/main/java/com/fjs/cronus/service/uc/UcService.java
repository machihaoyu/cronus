package com.fjs.cronus.service.uc;

import com.fjs.cronus.Common.ResultResource;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.cronus.BaseUcDTO;
import com.fjs.cronus.dto.cronus.UcUserDTO;
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
            UcUserDTO ucUserDTO = getUserInfoByID(token,user_id);
            if (ucUserDTO ==null){
                throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
            }
            if (ucUserDTO.getData_type() == null){
                throw new CronusException(CronusException.Type.CRM_DATAAUTH_ERROR);
            }
            Integer data_type = Integer.valueOf(ucUserDTO.getData_type());
            if (data_type == 1){
                //TODO 只能查看自己并把结果存入缓存并设置好失效时间
                idList.add(user_id);
                //插入缓存
                ucRedisService.setRedisUcInfo(ResultResource.SUBUSERBYIDS + user_id,idList);
                return  idList;
            }
            com.fjs.cronus.dto.uc.BaseUcDTO baseDto = thorInterfaceService.getSubUserByUserId(token,user_id,ResultResource.SYSTEMNAME);
            if (baseDto.getRetData() != null){
                //转json
                String result = FastJsonUtils.obj2JsonString(baseDto.getRetData());
                idList = FastJsonUtils.getStringList(result);
            }

            ucRedisService.setRedisUcInfo(ResultResource.SUBUSERBYIDS + user_id ,idList);
            return  idList;
        }
    }

    public UcUserDTO getUserInfoByID(String token, Integer user_id){
        //TODO 差缓存是否存在用户信息 不存在 插接口
        UcUserDTO ucUserDTO = null;
        ucUserDTO = ucRedisService.getRedisUserInfo(ResultResource.USERINFOBYID + user_id);
        if (ucUserDTO != null){
            return ucUserDTO;
        }
        com.fjs.cronus.dto.uc.BaseUcDTO ucDTO = thorInterfaceService.getUserInfoByField(token,null,user_id,null);
        if (ucDTO.getRetData() !=null){
            //map 转json
            String result = FastJsonUtils.obj2JsonString(ucDTO.getRetData());
            //把json格式的数据转为对象

            ucUserDTO = FastJsonUtils.getSingleBean(result,UcUserDTO.class);
            //TODO 信息存入缓存 并设置失效时间
            ucRedisService.setRedisUserInfo(ResultResource.USERINFOBYID + user_id + ucUserDTO.getUser_id(), ucUserDTO);

        }
        return ucUserDTO;
    }

    //校验是否有权限进行编辑
    public Integer getUserIdByToken(String token){
        Integer user_id = null;
        CronusDto resultDto = new CronusDto();
        //根据token查询当前用户id
        String result = thorInterfaceService.getCurrentUserInfo(token,null);
        BaseUcDTO dto = FastJsonUtils.getSingleBean(result,BaseUcDTO.class);
        System.out.println(dto.getData().toString());
        UcUserDTO userDTO = FastJsonUtils.getSingleBean(dto.getData().toString(),UcUserDTO.class);
        if (userDTO != null){
            user_id  = Integer.valueOf(userDTO.getUser_id());
        }
        return  user_id;
    }
}
