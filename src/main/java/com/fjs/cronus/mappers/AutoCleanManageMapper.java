package com.fjs.cronus.mappers;

import com.fjs.cronus.model.AutoCleanManage;
import com.fjs.cronus.util.MyMapper;

import java.util.List;
import java.util.Map;

/**
 * Created by feng on 2017/10/12.
 */
public interface AutoCleanManageMapper extends MyMapper<AutoCleanManage>{

    public List<AutoCleanManage> selectByParamsMap(Map<String, Object> map);

}
