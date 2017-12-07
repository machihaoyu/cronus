package com.fjs.cronus.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.api.PHPLoginDto;
import com.fjs.cronus.dto.cronus.AddPrdCustomerDTO;
import com.fjs.cronus.dto.cronus.CustomerDTO;
import com.fjs.cronus.dto.cronus.PrdCustomerDTO;
import com.fjs.cronus.dto.uc.UserInfoDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.CommunicationLogMapper;
import com.fjs.cronus.mappers.PrdCustomerMapper;
import com.fjs.cronus.model.CommunicationLog;
import com.fjs.cronus.model.PrdCustomer;
import com.fjs.cronus.model.PrdOperationLog;
import com.fjs.cronus.service.thea.TheaClientService;
import com.fjs.cronus.service.uc.UcService;
import com.fjs.cronus.util.FastJsonUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by yinzf on 2017/11/1.
 */
@Service
public class PrdCustomerService {
    @Autowired
    private PrdCustomerMapper prdCustomerMapper;
    @Autowired
    private PrdOperationLogService prdOperationLogService;
    @Autowired
    private UcService thorUcService;
    @Autowired
    private CommunicationLogMapper communicationLogMapper;
    @Autowired
    TheaClientService theaClientService;
//    @Autowired
//    private LoanService loanService;

    @Transactional
    public Integer addPrdCustomer(PrdCustomer prdCustomer, UserInfoDTO userInfoDTO){
        Integer userId = null;
        if (userInfoDTO !=null){
            if (StringUtils.isNotEmpty(userInfoDTO.getUser_id())) {
                userId = Integer.parseInt(userInfoDTO.getUser_id());
            }
        }
        Date date=new Date();
        prdCustomer.setCreateUser(userId);
        prdCustomer.setLastUpdateUser(userId);
        prdCustomer.setCreateTime(date);
        prdCustomer.setLastUpdateTime(date);
        prdCustomer.setIsDeleted(CommonConst.DATA_NORMAIL);
        prdCustomer.setStatus(CommonConst.PRD_NORMAIL);
        int prdResult=prdCustomerMapper.insert(prdCustomer);

        PrdOperationLog prdOperationLog=new PrdOperationLog();
        prdOperationLog.setOperation(CommonConst.IMPORT_PRD_CUSTOMER);
        prdOperationLog.setPrdCustomerId(prdCustomer.getId());
        prdOperationLog.setResult(CommonConst.IMPORT_PRD_RESULT);
        prdOperationLog.setCreateUser(userId);
        prdOperationLog.setCreateTime(date);
        prdOperationLogService.addPrdOperationLog(prdOperationLog);
        return prdResult;
    }

    /**
     * 市场推广盘拷贝属性
     * @param prdCustomer
     * @param prdCustomerDTO
     * @return
     */
    public PrdCustomer copyProperty(PrdCustomer prdCustomer,PrdCustomerDTO prdCustomerDTO){
        prdCustomer.setCustomerName(prdCustomerDTO.getCustomerName());
        prdCustomer.setCustomerType(prdCustomerDTO.getCustomerType());
        prdCustomer.setLoanAmount(prdCustomerDTO.getLoanAmount());
        prdCustomer.setCity(prdCustomerDTO.getCity());
        prdCustomer.setHouseStatus(prdCustomerDTO.getHouseStatus());
        return prdCustomer;
    }

    @Transactional
    public Integer updatePrdCustomer(AddPrdCustomerDTO prdCustomerDTO, UserInfoDTO userInfoDTO){
        Integer userId = null;
        if (userInfoDTO !=null){
            if (StringUtils.isNotEmpty(userInfoDTO.getUser_id())) {
                userId = Integer.parseInt(userInfoDTO.getUser_id());
            }
        }
        Date date=new Date();
        PrdCustomer prdCustomer=getByPrimary(prdCustomerDTO.getId());
        //实体转换
        if (StringUtils.isNotEmpty(prdCustomer.getCommunitContent())){

        }
        prdCustomer.setLastUpdateUser(userId);
        prdCustomer.setLastUpdateTime(date);
       /* if (prdCustomerDTO.getType() != null && prdCustomerDTO.getType() ==1 ){
          *//*  Loan loan=copyProperty2Loan(prdCustomerDTO);
            loan.setTelephonenumber(prdCustomer.getTelephonenumber());
            loanService.add(loan,userInfoDTO);*//*
        }*/

        return prdCustomerMapper.update(prdCustomer);
    }

    /**
     * 主键查找
     * @param id
     * @return
     */
    public PrdCustomer getByPrimary(Integer id){
        PrdCustomer prdCustomer=new PrdCustomer();
        prdCustomer.setId(id);
        return prdCustomerMapper.selectOne(prdCustomer);
    }
    /**
     * 删除
     * @param id
     */
    public Integer delete(Integer id,UserInfoDTO userInfoDTO){
        PrdCustomer prdCustomer=getByPrimary(id);
        prdCustomer.setIsDeleted(CommonConst.DATA__DELETE);
        Date date = new Date();
        prdCustomer.setLastUpdateTime(date);
        Integer userId = null;
        if (StringUtils.isNotEmpty(userInfoDTO.getUser_id())) {
            userId = Integer.parseInt(userInfoDTO.getUser_id());
        }
        prdCustomer.setLastUpdateUser(userId);
        return prdCustomerMapper.update(prdCustomer);
    }

    public PrdCustomerDTO copyProperty(PrdCustomer prdCustomer,String token){
        PrdCustomerDTO prdCustomerDTO=new PrdCustomerDTO();
        prdCustomerDTO.setId(prdCustomer.getId());
        prdCustomerDTO.setCustomerName(prdCustomer.getCustomerName());
        prdCustomerDTO.setSex(prdCustomer.getSex());
        prdCustomerDTO.setCustomerType(prdCustomer.getCustomerType());
        prdCustomerDTO.setTelephonenumber(prdCustomer.getTelephonenumber());
        prdCustomerDTO.setLoanAmount(prdCustomer.getLoanAmount());
        prdCustomerDTO.setCity(prdCustomer.getCity());
        prdCustomerDTO.setCustomerSource(prdCustomer.getCustomerSource());
        prdCustomerDTO.setUtmSource(prdCustomer.getUtmSource());
        prdCustomerDTO.setHouseStatus(prdCustomer.getHouseStatus());
        prdCustomerDTO.setLevel(prdCustomer.getLevel());
        prdCustomerDTO.setCreateTime(prdCustomer.getCreateTime());
        String result = prdCustomer.getCommunitContent();
        if (!StringUtils.isEmpty(result)){
           JSONArray jsonArray=  FastJsonUtils.stringToJsonArray(result);
           //遍历
            for (int i = 0; i < jsonArray.size();i++){
                JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                //加入姓名
                UserInfoDTO userInfoDTO = thorUcService.getUserIdByToken(token,CommonConst.SYSTEM_NAME_ENGLISH);
                jsonObject.put("create_user_name",userInfoDTO.getName());
            }
            prdCustomerDTO.setComunication(jsonArray.toJSONString());
        }
        return prdCustomerDTO;
    }

    public QueryResult<PrdCustomerDTO> listByCondition(String customerName,String telephonenumber,String customerType,String level,String houseStatus,
                                                       String citySearch,Integer type,Integer mountLevle,Integer page,Integer size,String token) {
        Integer total = null;
        QueryResult<PrdCustomerDTO> prdCustomerQueryResult = null;
        List<PrdCustomer> prdCustomerList = null;
        List<PrdCustomerDTO> prdCustomerDTOList = new ArrayList<>();
        Map<String,Object> map=new HashedMap();
        List<String> citys = new ArrayList<>();
        //判断权限
        PHPLoginDto phpLoginDto = thorUcService.getAllUserInfo(token,CommonConst.SYSTEM_NAME_ENGLISH);
        //判断data_type

        Integer date_type =Integer.valueOf(phpLoginDto.getUser_info().getData_type());
        if (date_type != 4){
           String city = phpLoginDto.getUser_info().getRedis_city();
           if (!StringUtils.isEmpty(city)) {
               map.put("city", city);
           }else {
               //查询出异地城市和只要城市以外的
               String mainCity= theaClientService.findValueByName(token,CommonConst.MAIN_CITY);
               String remoteCity = theaClientService.findValueByName(token,CommonConst.REMOTE_CITY);
               String[] mainCityArray=mainCity.split(",");
               int mainCitySize=mainCityArray.length;
               for (int i=0;i<mainCitySize;i++){
                   citys.add(mainCityArray[i]);
               }
               String [] remoteCityArray = remoteCity.split(",");
               for (int i=0;i<remoteCityArray.length;i++){
                   citys.add(remoteCityArray[i]);
               }
               map.put("citys", citys);
           }
        }else {
            if (!StringUtils.isEmpty(citySearch)) {
                map.put("city", citySearch);
            }
        }
            if (StringUtils.isNotEmpty(customerName)){
                map.put("customerName",customerName);
            }
            if (StringUtils.isNotEmpty(customerType)){
                map.put("customerType",customerType);
            }
            if (StringUtils.isNotEmpty(telephonenumber)){
                map.put("telephonenumber",telephonenumber);
            }
            if (StringUtils.isNotEmpty(houseStatus)){
                map.put("houseStatus",houseStatus);
            }
            if (StringUtils.isNotEmpty(level)){
                map.put("level",level);
            }
            if (type==1){
                map.put("createTimeBegin", getMonthAgo(new Date(),1));
                map.put("createTimeEnd",getMonthAgo(new Date(),null));
            }
            if (type == 2){
                map.put("createTimeBegin", getMonthAgo(new Date(),1));
            }
            if (mountLevle != null){
                map.put("mountLevle",mountLevle);
            }
            map.put("start",(page-1)*size);
            map.put("size",size);
            prdCustomerList = prdCustomerMapper.listByCondition(map);
            for (PrdCustomer prdCustomer1:prdCustomerList){
                PrdCustomerDTO prdCustomerDTO=copyProperty(prdCustomer1,token);
                prdCustomerDTOList.add(prdCustomerDTO);
            }
            // 总数
            total = prdCustomerMapper.countByCondition(map);

             prdCustomerQueryResult = new QueryResult<PrdCustomerDTO>();
            prdCustomerQueryResult.setRows(prdCustomerDTOList);
            prdCustomerQueryResult.setTotal(total + "");
        return prdCustomerQueryResult;
    }

    public  PrdCustomerDTO decayPrdCustomer(Integer id,Integer userId,String token){
        PrdCustomerDTO prdCustomerDTO = new PrdCustomerDTO();
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("id",id);
        PrdCustomer prdCustomer = prdCustomerMapper.findById(paramsMap);
        //判断客户有没有查看权限
        boolean flag= validUserAndTime(prdCustomer,userId);
        if (false == false){
            throw new CronusException(CronusException.Type.MESSAGE_PRDCUSTOMER_ERROR);
        }
        prdCustomerDTO = copyProperty(prdCustomer,token);
        return  prdCustomerDTO;
    }


    @Transactional
    public boolean validUserAndTime(PrdCustomer prdCustomer,Integer userId){
           boolean flag = false;
           //需要判这个人十五分钟被人沟通了没
           Date date = new Date();
           Integer time =Integer.parseInt(String.valueOf(Calendar.getInstance().getTimeInMillis()));
           Integer hasTime =prdCustomer.getViewTime() + 15 * 60 -  time;
           if ((prdCustomer.getViewUid() != 0) && (!((prdCustomer.getViewUid() == userId) && hasTime > 0))){
               if (hasTime > 0) {
                   throw new CronusException(CronusException.Type.MESSAGE_PRDCUSTOMER_ERROR);
               }
           }
           //可以操作

           prdCustomer.setViewTime(time);
           prdCustomer.setViewUid(userId);
           prdCustomer.setLastUpdateTime(date);
           prdCustomer.setLastUpdateUser(userId);

           prdCustomerMapper.update(prdCustomer);
           flag = true;
        return  flag;

    }

    /**
     * @param addPrdCustomerDTO
     * @param prdCustomer
     */
    public void dtoTOEntity(AddPrdCustomerDTO addPrdCustomerDTO,PrdCustomer prdCustomer){
        if (!StringUtils.isEmpty(addPrdCustomerDTO.getCustomerName())){
            prdCustomer.setCustomerName(addPrdCustomerDTO.getCustomerName());
        }
        if (!StringUtils.isEmpty(addPrdCustomerDTO.getCustomerType())){
            prdCustomer.setCustomerType(addPrdCustomerDTO.getCustomerType());
        }
        if (!StringUtils.isEmpty(addPrdCustomerDTO.getSex())){
            prdCustomer.setSex(addPrdCustomerDTO.getSex());
        }
        if (!StringUtils.isEmpty(addPrdCustomerDTO.getHouseStatus())){
            prdCustomer.setHouseStatus(addPrdCustomerDTO.getHouseStatus());
        }
        if (!StringUtils.isEmpty(addPrdCustomerDTO.getCity())){
            prdCustomer.setCity(addPrdCustomerDTO.getCity());
        }
        if (addPrdCustomerDTO.getLoanAmount() != null){
            prdCustomer.setLoanAmount(addPrdCustomerDTO.getLoanAmount());
        }
        if (!StringUtils.isEmpty(addPrdCustomerDTO.getContent())){
            //查询当前是否有沟通记录
            String result = prdCustomer.getCommunitContent();

            if (!StringUtils.isEmpty(result)){
            JSONArray jsonArray = FastJsonUtils.stringToJsonArray(result);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("","");
            //jsonArray

            }


        }


    }
    /**
     * 日期转为String
     * @param date
     * @param type
     * @return
     */
    public String getMonthAgo(Date date,Integer type) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();

        //过去一月
        if (type !=null){
            c.setTime(date);
            c.add(Calendar.MONTH, -3);
        }

        Date m = c.getTime();
        String mon = format.format(m);
        return mon;
    }
}
