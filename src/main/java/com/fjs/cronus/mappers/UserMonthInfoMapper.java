package com.fjs.cronus.mappers;

import com.fjs.cronus.model.UserMonthInfo;
import com.fjs.cronus.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by feng on 2017/9/22.
 */
public interface UserMonthInfoMapper extends MyMapper<UserMonthInfo>{

    List<UserMonthInfo> selectByParamsMap(Map<String, Object> map);

    /**
     * 获取某一级吧，该月已购数.
     */
    Integer getOrderNum(@Param("companyid") Integer subCompanyId, @Param("effectiveDate") String effectiveDate, @Param("status") Integer status);

    /**
     * 更新业务员分配数.
     */
    void update2IncrNumForAssignedCustomerNum(@Param("ids") Set<Integer> ids, @Param("currentMonth") String currentMonth);

    /**
     * 更新业务员确认数.
     */
    void update2IncrNumForEffectiveCustomerNum(@Param("ids")Set<Integer> id);

    /**
     * 获取sum,根据条件.
     */
    Integer selectSum(@Param("companyid")Integer companyid, @Param("mediaid")Integer mediaid, @Param("effectiveDate")String effectiveDate, @Param("status")Integer status);

    /**
     * 加悲观锁查.
     */
    List<UserMonthInfo> findByParamsForUpdate(@Param("subCompanyId")Integer subCompanyId, @Param("mediaid")Integer mediaid, @Param("countMediaid")Integer countMediaid, @Param("salesmanId")Integer salesmanId, @Param("currentMonth")String currentMonth, @Param("status")Integer status);
}
