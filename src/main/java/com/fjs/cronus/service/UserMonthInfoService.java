package com.fjs.cronus.service;

import com.fjs.cronus.mappers.UserMonthInfoMapper;
import com.fjs.cronus.model.UserMonthInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by feng on 2017/9/22.
 */
@Service
public class UserMonthInfoService {

    @Autowired
    private UserMonthInfoMapper userMonthInfoMapper;

    public List<UserMonthInfo> selectByParamsMap(Map<String, Object> map) {
        return userMonthInfoMapper.selectByParamsMap(map);
    }

    public Integer saveOne(UserMonthInfo userMonthInfo) {
        return userMonthInfoMapper.saveOne(userMonthInfo);
    }

    public Integer saveList(List<UserMonthInfo> userMonthInfos) {
        return userMonthInfoMapper.saveList(userMonthInfos);
    }

    public Integer insertOne(UserMonthInfo userMonthInfo) {
        return userMonthInfoMapper.insertOne(userMonthInfo);
    }

    public Integer insertList(List<UserMonthInfo> userMonthInfoList) {
        return userMonthInfoMapper.insertList(userMonthInfoList);
    }
}
