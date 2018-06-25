package com.fjs.cronus.mappers;

import com.fjs.cronus.entity.EzucDataDetail;
import com.fjs.cronus.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EzucDataDetailMapper extends MyMapper<EzucDataDetail> {

    Long getDurationByName(@Param("name") String name, @Param("startTime") long startTime, @Param("endTime") long endTime,  @Param("status") Integer status);

    List<EzucDataDetail> findAllDuration(@Param("startTime") long startTime, @Param("endTime") long endTime, @Param("status") Integer status);
}
