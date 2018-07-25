package com.fjs.cronus.mappers;

import com.fjs.cronus.entity.SalesmanCallTime;
import com.fjs.cronus.util.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

public interface SalesmanCallTimeMapper extends MyMapper<SalesmanCallTime> {

    /**
     * 获取指定时间内所有业务员的通话时长.
     */
    List<SalesmanCallTime> findByTime(@Param("start") Date start, @Param("end") Date end, @Param("status") Integer status);

    /**
     * 获取业务员，指定时间内所有业务员的通话时长.
     */
    Long getByTimeAndName(@Param("salemanName") String salemanName, @Param("start") Date start, @Param("end") Date end, @Param("status") Integer status);
}
