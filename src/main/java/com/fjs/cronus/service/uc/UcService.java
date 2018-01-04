package com.fjs.cronus.service.uc;

import com.fjs.cronus.Common.ResultResource;
import com.fjs.cronus.api.PhpApiDto;
import com.fjs.cronus.dto.CronusDto;

import com.fjs.cronus.dto.api.PHPLoginDto;
import com.fjs.cronus.dto.api.PHPUserDto;
import com.fjs.cronus.dto.api.SimpleUserInfoDTO;
import com.fjs.cronus.dto.api.uc.*;
import com.fjs.cronus.dto.cronus.BaseUcDTO;
import com.fjs.cronus.dto.cronus.SortUserInfoByPhoneDTO;
import com.fjs.cronus.dto.cronus.UcUserDTO;
import com.fjs.cronus.dto.uc.*;
import com.fjs.cronus.dto.uc.SubCompanyCityDto;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.service.client.ThorService;
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
    ThorService thorService;
    @Autowired
    UcRedisService ucRedisService;
//    @Autowired
//    ThorUcService thorUcService;
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
            AppUserDto ucUserDTO = getUserInfoByID(token,user_id);
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
            com.fjs.cronus.dto.uc.BaseUcDTO baseDto = thorService.getSubUserByUserId(token,user_id,ResultResource.SYSTEMNAME);
            if (baseDto.getRetData() != null){
                //转json
                String result = FastJsonUtils.obj2JsonString(baseDto.getRetData());
                idList = FastJsonUtils.getStringList(result);
            }

            ucRedisService.setRedisUcInfo(ResultResource.SUBUSERBYIDS + user_id ,idList);
            return  idList;
        }
    }

    public AppUserDto getUserInfoByID(String token, Integer user_id){
        //TODO 差缓存是否存在用户信息 不存在 插接口
        AppUserDto ucUserDTO = null;
        ucUserDTO = ucRedisService.getRedisUserInfo(ResultResource.USERINFOBYID + user_id);
        if (ucUserDTO != null){
            return ucUserDTO;
        }
        PhpApiDto<AppUserDto> ucDTO = thorService.getUserInfoByFields(null,token,user_id,null);
        if (ucDTO.getRetData() !=null){
            //map 转json
            String result = FastJsonUtils.obj2JsonString(ucDTO.getRetData());
            //把json格式的数据转为对象

            ucUserDTO = FastJsonUtils.getSingleBean(result,AppUserDto.class);
            //TODO 信息存入缓存 并设置失效时间
            ucRedisService.setRedisUserInfo(ResultResource.USERINFOBYID + user_id + ucUserDTO.getUser_id(), ucUserDTO);

        }
        return ucUserDTO;
    }

    public Integer getUserIdByToken(String token){
        Integer user_id = null;
        CronusDto resultDto = new CronusDto();
        //根据token查询当前用户id
        String result = thorService.getCurrentUserInfo(token,null);
        BaseUcDTO dto = FastJsonUtils.getSingleBean(result,BaseUcDTO.class);
        UcUserDTO userDTO = FastJsonUtils.getSingleBean(dto.getData().toString(),UcUserDTO.class);
        if (userDTO != null){
            user_id  = Integer.valueOf(userDTO.getUser_id());
        }
        return  user_id;
    }

    public com.fjs.cronus.dto.uc.UserInfoDTO getUserIdByToken(String token, String systemName){
        Integer user_id = null;
        CronusDto resultDto = new CronusDto();
        //根据token查询当前用户id
        String result = thorService.getCurrentUserInfo(token,systemName);
        BaseUcDTO dto = FastJsonUtils.getSingleBean(result,BaseUcDTO.class);
        com.fjs.cronus.dto.uc.UserInfoDTO userDTO = FastJsonUtils.getSingleBean(dto.getData().toString(),com.fjs.cronus.dto.uc.UserInfoDTO.class);
        return userDTO;
    }


    public PHPLoginDto getAllUserInfo(String token,String systemName ){
        PHPLoginDto phpLoginDto = new PHPLoginDto();
        //先从redis中取到信息
        phpLoginDto  = ucRedisService.getRedisPhpUserInfo(token);
        if (phpLoginDto == null) {
            String result = thorService.getAllUserInfo(token, systemName);
            phpLoginDto = FastJsonUtils.getSingleBean(result, PHPLoginDto.class);
            ucRedisService.setPhpUserInfo(token,phpLoginDto);
        }
        return  phpLoginDto;
    }


    public SimpleUserInfoDTO getSystemUserInfo(String token, Integer userId){
        SimpleUserInfoDTO userInfoDTO = new SimpleUserInfoDTO();
        String result= thorService.getSystemUserInfo(token,userId);
        BaseUcDTO dto = FastJsonUtils.getSingleBean(result,BaseUcDTO.class);
        userInfoDTO = FastJsonUtils.getSingleBean(dto.getData().toString(),SimpleUserInfoDTO.class);
        return  userInfoDTO;
    }

    public List<CityDto> getSubcompanyByUserId( String token,Integer userId,String systemName){

        List<CityDto> resultList = new ArrayList<>();
        PhpApiDto<List<CityDto>> resultDto  = thorService.getSubcompanyByUserId(token,userId,systemName);
        if (resultDto.getRetData() != null){
            resultList = resultDto.getRetData();
        }
        return  resultList;
    }

    public List<CrmCitySubCompanyDto> getSubCompanyByCitys(String token,String citys){

        List<CrmCitySubCompanyDto> resultList = new ArrayList<>();
        CronusDto<List<CrmCitySubCompanyDto>> resultDto  = thorService.getSubCompanyByCitys(token,citys);
        if (resultDto.getData() != null){
            resultList = resultDto.getData();
        }
        return  resultList;
    }

    public List<SubCompanyCityDto> getAllSubCompanyByUserId(String token, Integer userId, String systemName){

        List<SubCompanyCityDto> resultList = new ArrayList<>();
        PhpApiDto<List<SubCompanyCityDto>> resultDto  = thorService.getAllSubCompanyByUserId(token,userId,systemName);
        if (resultDto.getRetData() != null){
            resultList = resultDto.getRetData();
        }
        return  resultList;
    }

    public List<SubCompanyDto> getAllCompanyByUserId(String token, Integer userId, String systemName){
        List<SubCompanyDto> resultList = new ArrayList<>();
        PhpApiDto<List<SubCompanyDto>>resultDto =  thorService.getAllCompanyByUserId(token,userId,systemName);
        if (resultDto.getRetData() != null){
            resultList = resultDto.getRetData();
        }
        return  resultList;
    }

    public List<PHPUserDto>  getUserByIds(String token,  String user_ids,
                                          String department_ids,
                                          Integer sub_company_id,
                                          String flag,
                                          Integer page,
                                          Integer size,
                                          String name,
                                          Integer status){

        List<PHPUserDto> resultList = new ArrayList<>();

        ThorQueryDto< List<PHPUserDto>> resultDto = thorService.getUserByIds(token,user_ids,department_ids,sub_company_id,flag,page,size,name,status);

        if (resultDto.getRetData() != null){
            resultList = resultDto.getRetData();
        }

        return  resultList;
    }

    public List<PhpDepartmentModel> getSubCompanys(String token, Integer companyId) {
        List<PhpDepartmentModel> phpDepartmentModelList = new ArrayList<PhpDepartmentModel>();
        PhpApiDto<List<PhpDepartmentModel>> phpApiDto = thorService.getSubCompany(token, null,1, null,companyId);
        phpDepartmentModelList = phpApiDto.getRetData();
        return phpDepartmentModelList;
    }

    public List<CompanyDto> listAllEnableCompany(String token){

        List<CompanyDto> companyDtos = new ArrayList<>();
        CronusDto<List<CompanyDto>> resultDto  = thorService.listAllEnableCompany(token);

        if (resultDto.getData() != null){
            companyDtos = resultDto.getData();
        }

        return  companyDtos;
    }

   public UserSortInfoDTO getSortUserInfo(String token) {
       UserSortInfoDTO userSortInfoDTO = new UserSortInfoDTO();
       CronusDto<UserSortInfoDTO> result = thorService.getSortUserInfo(token);
       if (result.getData() != null) {
           userSortInfoDTO = result.getData();
       }
       return userSortInfoDTO;
   }

    public SortUserInfoByPhoneDTO getSortUserInfoByPhone(String token,String telephone) {
        SortUserInfoByPhoneDTO userSortInfoDTO = new SortUserInfoByPhoneDTO();
        CronusDto<SortUserInfoByPhoneDTO> result = thorService.getSortUserInfoByPhone(token,telephone,2);
        if (result.getData() != null) {
            userSortInfoDTO = result.getData();
        }
        return userSortInfoDTO;
    }

    public RoleDTO getRoleInfo(String token,String name,String value,Integer compantId){
        PhpApiDto<RoleDTO> resultDto = new PhpApiDto<>();
        RoleDTO roleDTO = new RoleDTO();
        resultDto = thorService.getRole(token,name,value,compantId);
        if (resultDto.getRetData() != null) {
            roleDTO = resultDto.getRetData();
        }
        return roleDTO;
    }
    public List<CronusSubInfoDTO> getSubCompanyToCronus(String token,Integer userId,String system){
        List<CronusSubInfoDTO> cronusSubInfoDTOS = new ArrayList<>();
        PhpApiDto<List<CronusSubInfoDTO>> resultDto = thorInterfaceService.getSubCompanyToCronus(token,userId,system);
        if (resultDto.getRetData() != null) {
            cronusSubInfoDTOS = resultDto.getRetData();
        }
        return  cronusSubInfoDTOS;
    }

}
