package com.fjs.cronus.mappers;

import com.fjs.cronus.model.UserMonthInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by feng on 2017/9/22.
 */
public interface UserMonthInfoMapper {

    List<UserMonthInfo> selectByParamsMap(Map<String, Object> map);

    Integer saveOne(UserMonthInfo userMonthInfo);

    Integer saveList(List<UserMonthInfo> userMonthInfos);

    Integer insertOne(UserMonthInfo userMonthInfo);

    Integer insertList(List<UserMonthInfo> userMonthInfos);
}
