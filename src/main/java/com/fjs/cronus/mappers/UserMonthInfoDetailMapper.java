package com.fjs.cronus.mappers;

import com.fjs.cronus.entity.UserMonthInfoDetail;
import com.fjs.cronus.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface UserMonthInfoDetailMapper extends MyMapper<UserMonthInfoDetail> {

    List<Map<Integer,Object>> findAllocateDataByTimAndMedia(@Param("starttime") Date starttime, @Param("endstart") Date endstart, @Param("mediaid") Integer mediaid);
}
