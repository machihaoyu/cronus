package com.fjs.cronus.mappers;

import com.fjs.cronus.dto.MediaCustomerCountDTO;
import com.fjs.cronus.entity.MediaCustomerCountEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface MediaCustomerCountMapper  {


    List<MediaCustomerCountDTO> utmSourceList(HashMap<String, Object> map);

    Integer utmSourceListCount(HashMap<String, Object> map);

    @Update("UPDATE media_customer_count SET purchased_number = purchased_number + 1 WHERE is_deleted = 0 AND id = #{mediaCustomerCountId}")
    Integer updatePurchasedNumber(@Param("mediaCustomerCountId") Integer mediaCustomerCountId);

    MediaCustomerCountDTO getCustomerPrice(@Param("mediaCustomerCountId") Integer mediaCustomerCountId);


    MediaCustomerCountEntity getMediaCustomerCount(@Param("customerSource") String customerSource, @Param("utmSource") String utmSource);

    Integer addMediaCustomerCount(MediaCustomerCountEntity mediaCustomerCount);
}
