package com.fjs.cronus.mappers;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by Administrator on 2018/3/27.
 */
public interface PhoneMapper {

    @Insert("INSERT INTO phone_log set phone = #{phone},content = #{content},create_user = #{userId},is_deleted =0")
    Integer insertPhoneLog(@Param("phone") String phone, @Param("content") String content, @Param("userId") Integer userId);

    @Select("SELECT COUNT(1) FROM phone_log WHERE phone = #{phone} AND DATEDIFF(create_time, NOW()) = 0 and is_deleted = 0")
    Integer getPhoneCountToday(@Param("phone") String phone);

}
