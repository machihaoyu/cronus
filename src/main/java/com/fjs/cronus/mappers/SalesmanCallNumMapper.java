package com.fjs.cronus.mappers;

import com.fjs.cronus.entity.SalesmanCallNum;
import com.fjs.cronus.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface SalesmanCallNumMapper extends MyMapper<SalesmanCallNum> {

    /**
     * 获取指定时间内所有业务员的通话次数.
     */
    List<SalesmanCallNum> findByTime(@Param("start") Date start, @Param("end") Date end, @Param("status") Integer status);

    /**
     * 获取业务员，指定时间内所有业务员的通话次数.
     */
    Long getByTimeAndName(@Param("salemanName") String salemanName, @Param("start") Date start, @Param("end") Date end, @Param("status") Integer status);

}
