package com.fjs.cronus.mappers;

import com.fjs.cronus.model.UserMonthInfo;
import com.fjs.cronus.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by feng on 2017/9/22.
 */
public interface UserMonthInfoMapper extends MyMapper<UserMonthInfo>{

    List<UserMonthInfo> selectByParamsMap(Map<String, Object> map);

    List<UserMonthInfo> findByParams(UserMonthInfo params);

    Integer updateUserMonthInfo(@Param("whereParams") UserMonthInfo whereParams, @Param("valueParams") UserMonthInfo valueParams);

    Integer insertList2(List<UserMonthInfo> userMonthInfos);
}
