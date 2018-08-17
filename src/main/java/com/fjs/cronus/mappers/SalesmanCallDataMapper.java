package com.fjs.cronus.mappers;

import com.fjs.cronus.entity.SalesmanCallData;
import com.fjs.cronus.util.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

public interface SalesmanCallDataMapper extends MyMapper<SalesmanCallData> {

    /**
     * 获取通话时长.
     */
    Long getDurationByName(@Param("name") String name, @Param("startTime") long startTime, @Param("endTime") long endTime,  @Param("status") Integer status);

    /**
     * 获取通话次数.
     */
    Long getNumByName(@Param("name") String name, @Param("startTime") long startTime, @Param("endTime") long endTime,  @Param("status") Integer status);

    /**
     * 获取通话时长.
     */
    List<SalesmanCallData> findAllDuration(@Param("startTime") long startTime, @Param("endTime") long endTime, @Param("status") Integer status);

    /**
     * 获取通话次数.
     */
    List<SalesmanCallData> findAllNum(@Param("startTime") long startTime, @Param("endTime") long endTime, @Param("status") Integer status);

    /**
     * 根据业务员id、顾客手机号，查询通话记录（有语音）
     */
    List<SalesmanCallData> findHasRecordingUrlBySalemanidAndCustomerPhone(@Param("salesManId")Long userId, @Param("customerPhoneNum")Long customerPhone, @Param("systype")Integer systypeBAndroid, @Param("status")Integer status);

    /**
     * 根据业务员id，查询通话记录（无语音）
     */
    List<SalesmanCallData> findNotHasRecordingUrlBySalemanid(@Param("salesManId")Long loginUid, @Param("systype")Integer systypeBAndroid, @Param("status")Integer code);
}
