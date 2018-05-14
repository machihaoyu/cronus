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

    /**
     * 获取某一级吧，该月已购数.
     */
    Integer getOrderNum(@Param("companyid") Integer subCompanyId, @Param("effectiveDate") String effectiveDate, @Param("status") Integer status);

    /**
     * 更新业务员分配数.
     */
    void update2IncrNum(@Param("id")Integer id);

    /**
     * 获取sum,根据条件.
     */
    Integer selectSum(@Param("companyid")Integer companyid, @Param("sourceid")Integer sourceid, @Param("mediaid")Integer mediaid, @Param("effectiveDate")String effectiveDate, @Param("status")Integer status);

}
