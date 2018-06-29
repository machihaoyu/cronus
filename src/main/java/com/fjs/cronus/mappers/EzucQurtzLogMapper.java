package com.fjs.cronus.mappers;

import com.fjs.cronus.entity.EzucQurtzLog;
import com.fjs.cronus.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EzucQurtzLogMapper extends MyMapper<EzucQurtzLog>{

    List<EzucQurtzLog> findEzucSyncLog(@Param("data") EzucQurtzLog data, @Param("start") Integer start, @Param("pageSize") Integer pageSize);
}
