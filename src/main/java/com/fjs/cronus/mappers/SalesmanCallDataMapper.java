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

}
