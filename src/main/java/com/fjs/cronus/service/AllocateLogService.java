//package com.fjs.cronus.service;
//
//import com.fjs.cronus.Common.CommonConst;
//import com.fjs.cronus.Common.CommonEnum;
//import com.fjs.cronus.api.thea.Loan;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import tk.mybatis.mapper.entity.Example;
//
//import java.util.*;
//
///**
// * Created by feng on 2017/9/20.
// */
//@Service
//public class AllocateLogService {
//
//    @Autowired
//    private AllocateLogMapper allocateLogMapper;
//
//    @Autowired
//    private LoanLogService loanLogService;
//
//
//    /**
//     * 添加记录
//     *
//     * @param allocateLog
//     * @return
//     */
//    public Integer insertOne(AllocateLog allocateLog) {
//        Integer count = allocateLogMapper.insertOne(allocateLog);
//        return count;
//    }
//
//    public List<AllocateLog> selectByParamsMap(Map<String, Object> map) {
//        List<AllocateLog> allocateLogList = new ArrayList<>();
//        allocateLogList = allocateLogMapper.selectByParamsMap(map);
//        return allocateLogList;
//    }
//
//    /**
//     * 添加分配日志
//     *
//     * @param loan
//     * @param newOwnerUserId
//     * @param operationCode
//     * @return
//     */
//    @Transactional
//    public boolean addAllocatelog(Loan loan, Integer newOwnerUserId, Integer operationCode, UserInfoDTO userInfoDTO) {
//        if (null == loan.getId() || 0 == loan.getId()) {
//            return false;
//        }
//        //添加分配日志
//        AllocateLog allocateLog = new AllocateLog();
//        allocateLog.setCreateTime(new Date());
//        allocateLog.setLoanId(loan.getId());
//        allocateLog.setCustomerId(loan.getCustomerId());
//        allocateLog.setOldOwnerId(loan.getOwnUserId());
//        allocateLog.setNewOwnerId(newOwnerUserId);
//
//        //添加交易操作日志
//        LoanLog loanLog = CommonInitObject.initLoanLogByLoan(loan);
//
//        if (null != userInfoDTO) {
//            allocateLog.setCreateUserId(Integer.valueOf(userInfoDTO.getUser_id()));
//            allocateLog.setCreateUserName(userInfoDTO.getName());
//
//            loanLog.setLogUserId(Integer.valueOf(userInfoDTO.getUser_id()));
//            loanLog.setLogCreateTime(new Date());
//        } else {
//            allocateLog.setCreateUserId(CommonConst.SYSTEM_ID);
//            allocateLog.setCreateUserName(CommonConst.SYSTEM_NAME);
//        }
//        switch (operationCode) {
//            case 1:
//                allocateLog.setOperation(CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_1.getCodeDesc());
//                loanLog.setLogDescription(CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_1.getCodeDesc());
//                break;
//            case 2:
//                allocateLog.setOperation(CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_2.getCodeDesc());
//                loanLog.setLogDescription(CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_2.getCodeDesc());
//                break;
//            case 3:
//                allocateLog.setOperation(CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_3.getCodeDesc());
//                loanLog.setLogDescription(CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_3.getCodeDesc());
//                break;
//            case 4:
//                allocateLog.setOperation(CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_4.getCodeDesc());
//                loanLog.setLogDescription(CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_4.getCodeDesc());
//                break;
//            case 5:
//                allocateLog.setOperation(CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_5.getCodeDesc());
//                loanLog.setLogDescription(CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_5.getCodeDesc());
//                break;
//            case 6:
//                allocateLog.setOperation(CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_6.getCodeDesc());
//                loanLog.setLogDescription(CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_6.getCodeDesc());
//                break;
//            default:
//                allocateLog.setOperation(CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_7.getCodeDesc());
//                loanLog.setLogDescription(CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_7.getCodeDesc());
//                break;
//        }
//        Integer insertAllocateLog = allocateLogMapper.insert(allocateLog);
//        if (null != insertAllocateLog) {
//            //发送站内信
//
//
//            Integer insertLoanLog = loanLogService.insertOne(loanLog);
//            if (null != insertLoanLog) {
//                return true;
//            } else {
//                return false;
//            }
//        } else {
//            return false;
//        }
//    }
//
//    public Integer insertBatch(List<AllocateLog> allocateLogs){
//        return allocateLogMapper.insertBatch(allocateLogs);
//    }
//
//    //根据条件查找分配日志
//    public List<AllocateLog> listByCondition(Integer loanId){
//        Example example = new Example(AllocateLog.class);
//        Example.Criteria criteria = example.createCriteria();
//        if (loanId !=null){
//            criteria.andEqualTo("loanId",loanId);
//        }
//        example.setOrderByClause("create_time desc");
//        return allocateLogMapper.selectByExample(example);
//    }
//
//    public AllocateLogDTO copyProperty(AllocateLog allocateLog){
//        AllocateLogDTO allocateLogDTO=new AllocateLogDTO();
//        allocateLogDTO.setOperation(allocateLog.getOperation());
//        allocateLogDTO.setOldOwnerId(allocateLog.getOldOwnerId());
//        allocateLogDTO.setNewOwnerId(allocateLog.getNewOwnerId());
//        allocateLogDTO.setCreateUserName(allocateLog.getCreateUserName());
//        allocateLogDTO.setCreateTime(allocateLog.getCreateTime());
//        return allocateLogDTO;
//    }
//
//
//    /**
//     * 统计今天分配客户数
//     * @param userId
//     * @return
//     */
//    public Integer getTodayData(List<String> userId){
//        Map<String,Object> map=new HashMap<>();
//        map.put("list",userId);
//        Integer todayCount=0;
//        todayCount=allocateLogMapper.selectToday(map);
//        return  todayCount;
//    }
//
//    /**
//     * 统计历史分配客户数
//     * @param userId
//     * @return
//     */
//    public Integer getHistoryData(List<String> userId,String start,String end){
//        Example example = new Example(AllocateLog.class);
//        Example.Criteria criteria = example.createCriteria();
//        criteria.andIn("newOwnerId",userId);
//        criteria.andBetween("createTime",start,end);
//        Integer historyCount=0;
//        historyCount=allocateLogMapper.selectCountByExample(example);
//        return  historyCount;
//    }
//
//    /**
//     * 统计今天分配沟通客户数
//     * @param userId
//     * @return
//     */
//    public Integer getTodayCommunicateData(List<String> userId){
//        Map<String,Object> map=new HashMap<>();
//        map.put("list",userId);
//        Integer todayCount=0;
//        todayCount=allocateLogMapper.selectCommunicateToday(map);
//        return  todayCount;
//    }
//    /**
//     * 统计历史分配客户数
//     * @param userId
//     * @return
//     */
//    public Integer getHistoryCommunicateData(List<String> userId,String start,String end){
//        Map<String,Object> map=new HashMap<>();
//        map.put("newOwnerId",userId);
//        map.put("createTimeBegin", start);
//        map.put("createTimeEnd",end);
//        Integer todayCount=0;
//        todayCount=allocateLogMapper.selectCommunicateHistory(map);
//        return  todayCount;
//    }
//}
