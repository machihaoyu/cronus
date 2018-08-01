package com.fjs.cronus.mappers;

import com.fjs.cronus.dto.MediaCustomerCountDTO;
import com.fjs.cronus.entity.MediaCustomerCountEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Mapper
public interface MediaCustomerCountMapper  {


    List<MediaCustomerCountDTO> utmSourceList(HashMap<String, Object> map);

    Integer utmSourceListCount(HashMap<String, Object> map);

    @Update("UPDATE media_customer_count SET purchased_number = purchased_number + 1,customer_stock = customer_stock - 1,gmt_modified=#{date} WHERE is_deleted = 0 AND id = #{mediaCustomerCountId}")
    Integer updatePurchasedNumber(@Param("mediaCustomerCountId") Integer mediaCustomerCountId,@Param("date") Date date);

    MediaCustomerCountDTO getCustomerPrice(@Param("mediaCustomerCountId") Integer mediaCustomerCountId);


    MediaCustomerCountEntity getMediaCustomerCount(@Param("customerSource") String customerSource, @Param("mediaName") String mediaName);

    Integer addMediaCustomerCount(MediaCustomerCountEntity mediaCustomerCount);

    @Update("UPDATE media_customer_count SET customer_stock = customer_stock + 1,gmt_modified=#{date} WHERE is_deleted = 0 AND id = #{mediaCustomerCountId}")
    Integer updateCustomerStock(@Param("mediaCustomerCountId") Integer mediaCustomerCountId,@Param("date") Date date);
}
