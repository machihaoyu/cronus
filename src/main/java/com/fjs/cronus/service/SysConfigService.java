package com.fjs.cronus.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.ConfigMapper;
import com.fjs.cronus.model.SysConfig;
import com.fjs.cronus.util.ConfigTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by feng on 2017/9/13.
 */
@Service
public class SysConfigService {
    @Autowired
    private ConfigMapper configMapper;

    public String findValueByName(String name) {
        String value = null;
        SysConfig config;
        try {
            config = configMapper.findValueByName(name);
            if (config != null && config.getConValue() != null) {
                if (ConfigTypeEnum.TYPE_ONE.getType().equals(config.getConType()) || ConfigTypeEnum.TYPE_THREE.getType().equals(config.getConType())) {
                    value = config.getConValue();
                }
                if (ConfigTypeEnum.TYPE_TWO.getType().equals(config.getConType())) {
                    JSONArray jsonArray = JSON.parseArray(config.getConValue());
                    value = jsonArray.toJSONString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CronusException(CronusException.Type.CRM_CALLBAXKCONFIG_ERROR);
        }

        return value;
    }

    public SysConfig getConfigByName(String name) {
        SysConfig config = configMapper.findValueByName(name);
        return config;
    }

    public Integer addConfig(SysConfig config) {
        int num = 0;
        try {
            config.setCreateTime(new Date());
            config.setLastUpdateTime(new Date());
            config.setIsDeleted(0);
            num = configMapper.insertConfig(config);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CronusException(CronusException.Type.CEM_CUSTOMERINTERVIEW);

        }
        return num;
    }

    /**
     * author:
     *
     * @param config
     * @return
     */
    public Integer update(SysConfig config) {
        Map<String, Object> map = new HashMap<String, Object>();
        Date date = new Date();
        config.setLastUpdateTime(date);
        return configMapper.update(config);
    }

}
