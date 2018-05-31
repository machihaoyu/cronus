package com.fjs.cronus.mappers;

import com.fjs.cronus.dto.avatar.FirstBarConsumeDTO;
import com.fjs.cronus.dto.avatar.FirstBarConsumeDTO2;
import com.fjs.cronus.entity.UserMonthInfoDetail;
import com.fjs.cronus.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface UserMonthInfoDetailMapper extends MyMapper<UserMonthInfoDetail> {
    List<FirstBarConsumeDTO> findAllocateDataByTimAndMedia(@Param("list") List<FirstBarConsumeDTO2> list);

    List<UserMonthInfoDetail> findPageData(@Param("start")int start, @Param("end")int end, @Param("type")Integer type, @Param("status")Integer status);
}
