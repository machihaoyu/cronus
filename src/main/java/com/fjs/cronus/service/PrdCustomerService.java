package com.fjs.cronus.service;


import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.cronus.CustomerDTO;
import com.fjs.cronus.dto.cronus.PrdCustomerDTO;
import com.fjs.cronus.dto.uc.UserInfoDTO;
import com.fjs.cronus.mappers.CommunicationLogMapper;
import com.fjs.cronus.mappers.PrdCustomerMapper;
import com.fjs.cronus.model.CommunicationLog;
import com.fjs.cronus.model.PrdCustomer;
import com.fjs.cronus.model.PrdOperationLog;
import com.fjs.cronus.service.uc.UcService;
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
        prdOperationLog.setPrdCustomerId(prdCustomer.getCustomerId());
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
        prdCustomer.setSex(prdCustomerDTO.getC_sex());
        prdCustomer.setLoanAmount(prdCustomerDTO.getLoanAmount());
        prdCustomer.setCity(prdCustomerDTO.getCity());
        prdCustomer.setHouseStatus(prdCustomerDTO.getHouseStatus());
        prdCustomer.setCommunitContent(prdCustomerDTO.getContent());

        return prdCustomer;
    }

    @Transactional
    public Integer updatePrdCustomer(PrdCustomerDTO prdCustomerDTO, UserInfoDTO userInfoDTO){
        Integer userId = null;
        if (userInfoDTO !=null){
            if (StringUtils.isNotEmpty(userInfoDTO.getUser_id())) {
                userId = Integer.parseInt(userInfoDTO.getUser_id());
            }
        }
        Date date=new Date();
        PrdCustomer prdCustomer=getByPrimary(prdCustomerDTO.getId());
        if (prdCustomer != null){
            prdCustomer=copyProperty(prdCustomer,prdCustomerDTO);
        }
        if (StringUtils.isNotEmpty(prdCustomer.getCommunitContent())){
            prdCustomer.setCommunitTime(date);
            //加入到沟通日志
            CommunicationLog communicationLog=copyProperty(prdCustomerDTO);
            communicationLog.setCreateUser(userId);
            communicationLog.setCreateTime(date);
            communicationLogMapper.insert(communicationLog);
        }
        prdCustomer.setLastUpdateUser(userId);
        prdCustomer.setLastUpdateTime(date);
        if (prdCustomerDTO.getType() != null && prdCustomerDTO.getType() ==1 ){
          /*  Loan loan=copyProperty2Loan(prdCustomerDTO);
            loan.setTelephonenumber(prdCustomer.getTelephonenumber());
            loanService.add(loan,userInfoDTO);*/
        }

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
     * 拷贝市场推广盘到客户
     * @param customerDTO
     * @param prdCustomerDTO
     * @return
     */
    public CustomerDTO copyProperty(CustomerDTO customerDTO, PrdCustomerDTO prdCustomerDTO){
        customerDTO.setCustomerName(prdCustomerDTO.getCustomerName());
        customerDTO.setCustomerType(prdCustomerDTO.getCustomerType());
        customerDTO.setSex(prdCustomerDTO.getC_sex());
        customerDTO.setCity(prdCustomerDTO.getCity());
        customerDTO.setHouseStatus(prdCustomerDTO.getHouseStatus());

        return customerDTO;
    }

    public CommunicationLog copyProperty(PrdCustomerDTO prdCustomerDTO){
        CommunicationLog communicationLog=new CommunicationLog();
        communicationLog.setCustomerId(prdCustomerDTO.getC_id());
        communicationLog.setContent(prdCustomerDTO.getContent());
        communicationLog.setHouseStatus(prdCustomerDTO.getHouseStatus());
        communicationLog.setLoanAmount(prdCustomerDTO.getLoanAmount());
        return communicationLog;
    }

    /*public Loan copyProperty2Loan(PrdCustomerDTO prdCustomerDTO){
        Loan loan=new Loan();
        loan.setCustomerId(prdCustomerDTO.getC_id());
        loan.setCustomerName(prdCustomerDTO.getCustomerName());
        loan.setTelephonenumber(prdCustomerDTO.getTelephonenumber());
        loan.setLoanAmount(prdCustomerDTO.getLoanAmount());
        loan.setCity(prdCustomerDTO.getCity());
        loan.setHouseStatus(prdCustomerDTO.getHouseStatus());
        loan.setCustomerSource(prdCustomerDTO.getCustomerSource());
        loan.setUtmSource(prdCustomerDTO.getUtmSource());
        loan.setCommunicateTime(new Date());
        return loan;
    }*/

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

    public PrdCustomerDTO copyProperty(PrdCustomer prdCustomer){
        PrdCustomerDTO prdCustomerDTO=new PrdCustomerDTO();
        prdCustomerDTO.setId(prdCustomer.getId());
        prdCustomerDTO.setCustomerName(prdCustomer.getCustomerName());
        prdCustomerDTO.setSex(prdCustomer.getSex());
        prdCustomerDTO.setCustomerType(prdCustomer.getCustomerType());
        prdCustomerDTO.setTelephonenumber(prdCustomer.getTelephonenumber());
        prdCustomerDTO.setLoanAmount(prdCustomer.getLoanAmount());
        prdCustomerDTO.setCity(prdCustomer.getCity());
        prdCustomerDTO.setCustomerSource(prdCustomer.getCustomerSource());
        prdCustomerDTO.setHouseStatus(prdCustomer.getHouseStatus());
        prdCustomerDTO.setLevel(prdCustomer.getLevel());
        prdCustomerDTO.setCommunicateTime(prdCustomer.getCommunitTime());

        return prdCustomerDTO;
    }

    public QueryResult<PrdCustomerDTO> listByCondition(PrdCustomer prdCustomer, UserInfoDTO userInfoDTO, String token, Integer pageNum, Integer size,
                                                       Integer communicationOrder, Integer type) {
        Integer userId = null;
        Integer companyId = null;
        Integer total = null;
        QueryResult<PrdCustomerDTO> prdCustomerQueryResult = null;
        List<PrdCustomer> prdCustomerList = null;
        List<PrdCustomerDTO> prdCustomerDTOList = new ArrayList<>();
        Map<String,Object> map=new HashedMap();
        List<String> idList = new ArrayList<String>();

        if (prdCustomer != null) {
            if (StringUtils.isNotEmpty(prdCustomer.getCustomerName())){
                map.put("customerName",prdCustomer.getCustomerName());
            }
            if (StringUtils.isNotEmpty(prdCustomer.getCustomerType())){
                map.put("customerType",prdCustomer.getCustomerType());
            }
            if (StringUtils.isNotEmpty(prdCustomer.getTelephonenumber())){
                map.put("telephonenumber",prdCustomer.getTelephonenumber());
            }
            if (StringUtils.isNotEmpty(prdCustomer.getHouseStatus())){
                map.put("houseStatus",prdCustomer.getHouseStatus());
            }
            if (StringUtils.isNotEmpty(prdCustomer.getLevel())){
                map.put("level",prdCustomer.getLevel());
            }
            if (type==1){
                map.put("createTimeBegin", getMonthAgo(new Date(),1));
                map.put("createTimeEnd",getMonthAgo(new Date(),null));
            }
            if (type == 2){
                map.put("createTimeBegin", getMonthAgo(new Date(),1));
            }
            if (communicationOrder !=null) {
                if (1 == communicationOrder) {
                    //沟通时间
                    map.put("communicationOrder", new Date());
                }
            }

            map.put("orderFields","last_update_time");
            map.put("order","desc");

            map.put("startNum",(pageNum-1)*size);
            map.put("size",size);
            prdCustomerList = prdCustomerMapper.listByCondition(map);
            for (PrdCustomer prdCustomer1:prdCustomerList){
                PrdCustomerDTO prdCustomerDTO=copyProperty(prdCustomer1);
                prdCustomerDTOList.add(prdCustomerDTO);
            }

            // 总数
            total = prdCustomerMapper.countByCondition(map);

             prdCustomerQueryResult = new QueryResult<PrdCustomerDTO>();
            prdCustomerQueryResult.setRows(prdCustomerDTOList);
            prdCustomerQueryResult.setTotal(total + "");
        }
        return prdCustomerQueryResult;
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
            c.add(Calendar.MONTH, -1);
        }

        Date m = c.getTime();
        String mon = format.format(m);
        return mon;
    }
}
