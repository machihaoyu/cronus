package com.fjs.cronus.mappers;

import com.fjs.cronus.entity.SalesmanCallData;
import com.fjs.cronus.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SalesmanCallDataMapper extends MyMapper<SalesmanCallData> {

    Long getDurationByName(@Param("name") String name, @Param("startTime") long startTime, @Param("endTime") long endTime,  @Param("status") Integer status);

    List<SalesmanCallData> findAllDuration(@Param("startTime") long startTime, @Param("endTime") long endTime, @Param("status") Integer status);
}
