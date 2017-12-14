package com.fjs.cronus.service;

import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.dto.AutoCleanManageDTO;
import com.fjs.cronus.mappers.AutoCleanManageMapper;
import com.fjs.cronus.model.AutoCleanManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by feng on 2017/10/12.
 */
@Service
public class AutoCleanManageService {

    @Autowired
    private AutoCleanManageMapper autoCleanManageMapper;

    /**
     * 获取集合
     * @param map
     * @return
     */
    public List<AutoCleanManage> selectByParamsMap(Map<String, Object> map){
        return autoCleanManageMapper.selectByParamsMap(map);
    }

    public Integer add(AutoCleanManage autoCleanManage,Integer userId){
        Date date=new Date();
        autoCleanManage.setUserId(userId);
        autoCleanManage.setCreateUser(userId);
        autoCleanManage.setCreateTime(date);
        autoCleanManage.setLastUpdateUser(userId);
        autoCleanManage.setLastUpdateTime(date);
        autoCleanManage.setIsDeleted(1);
        return autoCleanManageMapper.insert(autoCleanManage);
    }

    public AutoCleanManage copyProperty(AutoCleanManageDTO autoCleanManageDTO){
        AutoCleanManage autoCleanManage=new AutoCleanManage();
        autoCleanManage.setUtmSource(autoCleanManageDTO.getUtmSource());
        autoCleanManage.setCustomerSource(autoCleanManageDTO.getCustomerSource());
        return autoCleanManage;
    }
}
