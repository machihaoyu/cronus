package com.fjs.cronus.mappers;

import com.fjs.cronus.entity.SalesmanCallNum;
import com.fjs.cronus.entity.SalesmanMeetNum;
import com.fjs.cronus.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface SalesmanMeetNumMapper extends MyMapper<SalesmanMeetNum> {
    /**
     * 获取指定时间内所有业务员的面见次数.
     */
    List<SalesmanMeetNum> findByTime(@Param("start") Date start, @Param("end") Date end, @Param("status") Integer status);

    /**
     * 获取业务员，指定时间内所有业务员的面见次数.
     */
    Long getByTimeAndName(@Param("salemanName") String salemanName, @Param("start") Date start, @Param("end") Date end, @Param("status") Integer status);
}
