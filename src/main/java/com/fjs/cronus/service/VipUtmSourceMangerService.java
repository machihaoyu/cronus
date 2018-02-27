package com.fjs.cronus.service;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.ResultResource;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.api.PHPLoginDto;
import com.fjs.cronus.dto.api.PHPUserDto;
import com.fjs.cronus.dto.uc.RoleDTO;
import com.fjs.cronus.dto.uc.UserInfoDTO;
import com.fjs.cronus.dto.vipUtm.*;
import com.fjs.cronus.dto.api.uc.AppUserDto;
import com.fjs.cronus.mappers.CustomerInfoMapper;
import com.fjs.cronus.mappers.UtmCustomerMangerMapper;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.model.UtmCustomerManger;
import com.fjs.cronus.service.client.ThorService;
import com.fjs.cronus.service.uc.UcService;
import com.fjs.cronus.util.DEC3Util;
import com.fjs.cronus.util.DateUtils;
import com.fjs.cronus.util.FastJsonUtils;
import com.fjs.cronus.util.MultiThreadedHttpConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Created by msi on 2018/2/26.
 */
@Service
public class VipUtmSourceMangerService {


    @Autowired
    UtmCustomerMangerMapper utmCustomerMangerMapper;
    @Autowired
    CustomerInfoMapper customerInfoMapper;
    @Autowired
    UcService ucService;
    @Value("${OcdcUrl.url}")
    String ocdcUrl;
    @Value("${OcdcUrl.key}")
    String key;
  /*  @Value("${Role.name}")
    String roleName;*/
    public List<VipUtmManListDTO> vipUserManList(String token,Integer page,Integer size){
        //从uc中获取
        List<VipUtmManListDTO> vipUtmManListDTOS = new ArrayList<>();
        PHPLoginDto phpLoginDto = ucService.getAllUserInfo(token, CommonConst.SYSTEM_NAME_ENGLISH);
        UserInfoDTO userInfoDTO = phpLoginDto.getUser_info();
        RoleDTO roleDTO = ucService.getRoleInfo(token, "name", CommonConst.ROLE_NAME, Integer.valueOf(userInfoDTO.getCompany_id()));
        //获取用户
        QueryResult<PHPUserDto> queryResult = ucService.getVipUtmUserInfo(Integer.valueOf(roleDTO.getRole_id()),page,size,token);
        List<PHPUserDto> resultList = queryResult.getRows();
        if (resultList != null && resultList.size() > 0){
            for (PHPUserDto phpUserDto : resultList) {
                VipUtmManListDTO vipUtmManListDTO = new VipUtmManListDTO();
                vipUtmManListDTO.setUserId(phpUserDto.getUser_id());
                //时间戳转为时间
                String time = DateUtils.getDateString(Integer.valueOf(phpUserDto.getCreate_time()));
                vipUtmManListDTO.setCreateTime(time);
                vipUtmManListDTO.setTelephone(phpUserDto.getTelephone());
                vipUtmManListDTO.setName(phpUserDto.getName());
                vipUtmManListDTOS.add(vipUtmManListDTO);
            }
        }
        return  vipUtmManListDTOS;
    }

    public List<ChildInfoDTO> getChildInfo(String token,String userId){
        List<ChildInfoDTO> resultList = new ArrayList<>();
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("userId",userId);
        List<UtmCustomerManger> utmCustomerMangers = utmCustomerMangerMapper.findListByField(paramsMap);
        if (utmCustomerMangers != null && utmCustomerMangers.size() > 0){
            for (UtmCustomerManger utmCustomerManger : utmCustomerMangers) {
                ChildInfoDTO childInfoDTO = new ChildInfoDTO();
                childInfoDTO.setId(utmCustomerManger.getId());
                childInfoDTO.setCreateTime(utmCustomerManger.getCreateTime());
                childInfoDTO.setUtmSource(utmCustomerManger.getUtmSource());
                resultList.add(childInfoDTO);
            }
        }
        return resultList;
    }
    @Transactional
    public CronusDto addUtmsource(String token,UtmSourceDTO utmSourceDTO){
        //开始更新操作
        CronusDto resultDTO = new CronusDto();
        Map<String,Object> paramsMap = new HashMap<>();
        //判断是否存在
        paramsMap.put("userId",utmSourceDTO.getUserId());
        paramsMap.put("utmSource",utmSourceDTO.getUtmSource());
        List<UtmCustomerManger> utmCustomerMangers = utmCustomerMangerMapper.selectList(paramsMap);
        if (utmCustomerMangers.size() > 0){
            resultDTO.setMessage(ResultResource.EXIXTVIPUTMSOURCE_ERROR);
            resultDTO.setResult(ResultResource.CODE_SUCCESS);
            return resultDTO;
        }
        UtmCustomerManger utmCustomerManger = new UtmCustomerManger();
        Date date = new Date();
        utmCustomerManger.setUserId(utmSourceDTO.getUserId());
        utmCustomerManger.setUtmSource(utmSourceDTO.getUtmSource());
        utmCustomerManger.setStatus(1);
        utmCustomerManger.setCreateTime(date);
        //从uc获取信息
        Integer userIdByToken = ucService.getUserIdByToken(token);
        utmCustomerManger.setCreateUser(userIdByToken);
        utmCustomerManger.setLastUpdateTime(date);
        utmCustomerManger.setLastUpdateUser(userIdByToken);
        utmCustomerManger.setIsDeleted(0);
        //开始插入
        utmCustomerMangerMapper.insert(utmCustomerManger);
        resultDTO.setData("success");
        resultDTO.setMessage(ResultResource.MESSAGE_SUCCESS);
        resultDTO.setResult(ResultResource.CODE_SUCCESS);
        return resultDTO;
    }
    @Transactional
    public CronusDto delOneUtm(String token,Integer id){
        CronusDto resultDTO = new CronusDto();
        Date date = new Date();
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("id",id);
        UtmCustomerManger utmCustomerManger = utmCustomerMangerMapper.findByField(paramsMap);
        if (utmCustomerManger == null){
            resultDTO.setMessage(ResultResource.DELETEVIP_ERROR);
            resultDTO.setResult(ResultResource.CODE_ERROR);
            return resultDTO;
        }
        //开始删除
        Integer userIdByToken = ucService.getUserIdByToken(token);
        Integer userId = Integer.valueOf(userIdByToken);
        utmCustomerManger.setIsDeleted(1);
        utmCustomerManger.setStatus(2);
        utmCustomerManger.setLastUpdateUser(userId);
        utmCustomerManger.setLastUpdateTime(date);
        utmCustomerMangerMapper.updateByPrimaryKey(utmCustomerManger);
        resultDTO.setData("success");
        resultDTO.setMessage(ResultResource.MESSAGE_SUCCESS);
        resultDTO.setResult(ResultResource.CODE_SUCCESS);
        return resultDTO;
    }
    @Transactional
    public CronusDto delOneUser(String token,String userId){
        CronusDto resultDTO = new CronusDto<>();
        Date date = new Date();
        Map<String,Object> parmsMap = new HashMap<>();
        parmsMap.put("userId",userId);
        List<UtmCustomerManger> utmCustomerMangers = utmCustomerMangerMapper.selectList(parmsMap);
        if (utmCustomerMangers == null || utmCustomerMangers.size() == 0){
            resultDTO.setMessage(ResultResource.DELETEVIP_ERROR);
            resultDTO.setResult(ResultResource.CODE_ERROR);
            return resultDTO;
        }
        Integer userIdByToken = ucService.getUserIdByToken(token);
        //开始删除
        for (UtmCustomerManger utmCustomerManger : utmCustomerMangers ){
            utmCustomerManger.setStatus(2);
            utmCustomerManger.setIsDeleted(1);
            utmCustomerManger.setLastUpdateTime(date);
            utmCustomerManger.setLastUpdateUser(userIdByToken);
            //开始删除
            utmCustomerMangerMapper.updateByPrimaryKey(utmCustomerManger);
        }
        resultDTO.setData("success");
        resultDTO.setMessage(ResultResource.MESSAGE_SUCCESS);
        resultDTO.setResult(ResultResource.CODE_SUCCESS);
        return resultDTO;
    }

    @Transactional
    public CronusDto addOneUser(String token,String userId){
        //查询是否有此用户
        CronusDto resultDTO = new CronusDto<>();
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("userId",userId);
        List<UtmCustomerManger> utmCustomerMangers = utmCustomerMangerMapper.selectList(paramsMap);
        if (utmCustomerMangers.size() > 0){
            resultDTO.setMessage(ResultResource.EXIXTVIPUSER_ERROR);
            resultDTO.setResult(ResultResource.CODE_SUCCESS);
            return resultDTO;
        }
        //开始添加
        UtmCustomerManger utmCustomerManger = new UtmCustomerManger();
        Date date = new Date();
        utmCustomerManger.setUserId(userId);
        utmCustomerManger.setStatus(1);
        utmCustomerManger.setCreateTime(date);
        //从uc获取信息
        Integer userIdByToken = ucService.getUserIdByToken(token);
        utmCustomerManger.setCreateUser(userIdByToken);
        utmCustomerManger.setLastUpdateTime(date);
        utmCustomerManger.setLastUpdateUser(userIdByToken);
        utmCustomerManger.setIsDeleted(0);
        //开始插入
        utmCustomerMangerMapper.insert(utmCustomerManger);
        resultDTO.setData("success");
        resultDTO.setMessage(ResultResource.MESSAGE_SUCCESS);
        resultDTO.setResult(ResultResource.CODE_SUCCESS);
        return  resultDTO;
    }


    public CronusDto canMangerUtm(String token,String userId){
        CronusDto resultDTO = new CronusDto<>();
        List<String> list = new ArrayList<>();
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("userId",userId);
        List<UtmCustomerManger> utmCustomerMangers = utmCustomerMangerMapper.findListByField(paramsMap);
        if (utmCustomerMangers == null || utmCustomerMangers.size() == 0){
            resultDTO.setMessage(ResultResource.CODE_REMOVE_MESSAGE);
            resultDTO.setResult(ResultResource.CODE_SUCCESS);
            return resultDTO;
        }
        for (UtmCustomerManger utmCustomerManger : utmCustomerMangers){
            list.add(utmCustomerManger.getUtmSource());
        }
        resultDTO.setData(list);
        resultDTO.setMessage(ResultResource.MESSAGE_SUCCESS);
        resultDTO.setResult(ResultResource.CODE_SUCCESS);
        return resultDTO;
    }

    public CronusDto getOcdcCustomerList(String token,String userId,String utmSource,Integer p){
        CronusDto resultDTO = new CronusDto<>();
        //判断当前用户是否有此权限
        CronusDto cronusDto = canMangerUtm(token,userId);
        if (cronusDto.getData() != null){
            List<String> list = (List<String>) cronusDto.getData();
            //判断是有这个渠道的权限
            if (!list.contains(utmSource)){//没有权限
                resultDTO.setMessage(ResultResource.CODE_REMOVE_MESSAGE);
                resultDTO.setResult(ResultResource.CODE_SUCCESS);
                return resultDTO;
            }
        }
        //开始调用ocdc
        //拼接url
        String url = ocdcUrl + "?key=" + key + "&utm_source=" + utmSource + "&p=" + p;
        CronusDto<String> result  = MultiThreadedHttpConnection.getInstance().sendDataByGetReturnString(url);
        //转为json
        if (result.getData() == null){
            resultDTO.setMessage(ResultResource.VIP_MANGER_ERROR);
            resultDTO.setResult(ResultResource.CODE_ERROR);
            return resultDTO;
        }
        String reult = result.getData();
        JSONObject jsonObject = JSONObject.parseObject(reult);
        //获取返回并转为json
        String str = jsonObject.getString("retData");
        OcdcReturnDTO ocdcReturnDTO = FastJsonUtils.getSingleBean(str, OcdcReturnDTO.class);
        resultDTO.setResult(jsonObject.getInteger("errNum"));
        resultDTO.setMessage(jsonObject.getString("errMsg"));
        resultDTO.setData(ocdcReturnDTO);
        return resultDTO;
    }

    public CronusDto<QueryResult<UtmCustomerDTO>> utmCustomerList(String token, String userId, String utmSource, String customerName, String startTime, String endTime,
                                                       String cooperationStatus, String telephonenumber, Integer page, Integer size){
        CronusDto<QueryResult<UtmCustomerDTO>> resultDTO = new CronusDto<>();
        QueryResult<UtmCustomerDTO> queryResult = new QueryResult<>();
        List<UtmCustomerDTO> resultList = new ArrayList<>();
        Map<String,Object> paramsMap = new HashMap<>();
        CronusDto cronusDto = canMangerUtm(token,userId);
        if (cronusDto.getData() != null){
            List<String> list = (List<String>) cronusDto.getData();
            //判断是有这个渠道的权限
            if (!list.contains(utmSource)){//没有权限
                resultDTO.setMessage(ResultResource.CODE_REMOVE_MESSAGE);
                resultDTO.setResult(ResultResource.CODE_SUCCESS);
                return resultDTO;
            }
        }
        paramsMap.put("utmSource",utmSource);
        //拼接参数
        if (!StringUtils.isEmpty(customerName)){
            paramsMap.put("customerName",customerName);
        }
        if (!StringUtils.isEmpty(startTime)){
            Date startDate = DateUtils.parse(startTime,DateUtils.FORMAT_LONG);
            paramsMap.put("createTimeStart",startDate);
        }
        if (!StringUtils.isEmpty(endTime)){
            Date endDate = DateUtils.parse(endTime,DateUtils.FORMAT_LONG);
            paramsMap.put("createTimeEnd",endDate);
        }
        if (!StringUtils.isEmpty(cooperationStatus)){
            paramsMap.put("cooperationStatus",cooperationStatus);
        }
        if (!StringUtils.isEmpty(telephonenumber)){
            paramsMap.put("telephonenumber", DEC3Util.des3EncodeCBC(telephonenumber));
        }
        paramsMap.put("start", (page - 1) * size);
        paramsMap.put("size", size);
        List<CustomerInfo> customerInfoList = customerInfoMapper.utmCustomerList(paramsMap);
        Integer count = customerInfoMapper.utmCustomerListCount(paramsMap);
        if (customerInfoList != null && customerInfoList.size() > 0){
            for (CustomerInfo customerInfo : customerInfoList) {
                UtmCustomerDTO utmCustomerDTO = new UtmCustomerDTO();
                utmCustomerDTO.setId(customerInfo.getId());
                utmCustomerDTO.setCity(customerInfo.getCity());
                utmCustomerDTO.setCommunicateTime(customerInfo.getCommunicateTime());
                utmCustomerDTO.setConfirm(customerInfo.getConfirm());
                utmCustomerDTO.setCooptionstatus(customerInfo.getCooperationStatus());
                utmCustomerDTO.setCreateTime(customerInfo.getCreateTime());
                utmCustomerDTO.setCustomerName(customerInfo.getCustomerName());
                utmCustomerDTO.setCustomerSource(customerInfo.getCustomerSource());
                utmCustomerDTO.setHouseStatus(customerInfo.getHouseStatus());
                utmCustomerDTO.setLast_update_time(customerInfo.getLastUpdateTime());
                utmCustomerDTO.setLevel(customerInfo.getCustomerType());
                utmCustomerDTO.setLoanAmount(customerInfo.getLoanAmount());
                utmCustomerDTO.setOwnUserName(customerInfo.getOwnUserName());
                utmCustomerDTO.setRemain(customerInfo.getRemain());
                utmCustomerDTO.setSex(customerInfo.getSex());
                utmCustomerDTO.setUtmSource(customerInfo.getUtmSource());
                String telephone = DEC3Util.des3DecodeCBC(customerInfo.getTelephonenumber());
                String phoneNumber = telephone.substring(0, 7) + "****";
                utmCustomerDTO.setTelephonenumber(phoneNumber);
                resultList.add(utmCustomerDTO);
            }
        }
        queryResult.setTotal(count.toString());
        queryResult.setRows(resultList);
        resultDTO.setData(queryResult);
        resultDTO.setMessage(ResultResource.MESSAGE_SUCCESS);
        resultDTO.setResult(ResultResource.CODE_SUCCESS);
        return resultDTO;

    }
}
