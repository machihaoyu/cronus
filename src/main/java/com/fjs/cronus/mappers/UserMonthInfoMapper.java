package com.fjs.cronus.mappers;

import com.fjs.cronus.model.UserMonthInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by feng on 2017/9/22.
 */
public interface UserMonthInfoMapper {

    List<UserMonthInfo> selectByParamsMap(Map<String, Object> map);

    List<UserMonthInfo> findByParams(UserMonthInfo params);

    Integer updateUserMonthInfo(@Param("whereParams") UserMonthInfo whereParams, @Param("valueParams") UserMonthInfo valueParams);

    Integer insertList(List<UserMonthInfo> userMonthInfos);
}
