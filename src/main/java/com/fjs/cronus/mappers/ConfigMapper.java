package com.fjs.cronus.mappers;

import com.fjs.cronus.model.SysConfig;
import com.fjs.cronus.util.MyMapper;
import org.apache.ibatis.annotations.Param;


/**
 * Created by pc on 2017/9/19.
 */
public interface ConfigMapper extends MyMapper<SysConfig> {
    String getValueByName(@Param("con_name") String name);
    SysConfig findValueByName(@Param("con_name") String name);
    int ModifyValueById(SysConfig config);
    int insertConfig(SysConfig config);
    int update(SysConfig config);
}
