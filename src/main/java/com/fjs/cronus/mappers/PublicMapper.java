package com.fjs.cronus.mappers;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by Administrator on 2018/3/27.
 */
public interface PublicMapper {

    @Select("SELECT id from customer_info c where DATE_ADD(c.view_time, Interval 5 DAY) < NOW() ORDER BY create_time DESC LIMIT 0,500")
    List<Integer> getCustomersFromDiscard();

    @Update("update customer_info c set c.own_user_id = 0,c.view_time = NULL WHERE id in ( #{id})")
    Integer updateCustomersFromDiscard(@Param("ids") String ids);

}
