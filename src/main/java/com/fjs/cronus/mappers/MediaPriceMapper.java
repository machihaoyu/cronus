package com.fjs.cronus.mappers;

import com.fjs.cronus.entity.MediaPriceEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MediaPriceMapper {


    MediaPriceEntity getMediaPrice(@Param("mediaCustomerCountId") Integer mediaCustomerCountId);

    @Update("UPDATE media_price SET is_close = 1 WHERE is_deleted = 0 AND is_close = 0 AND media_customer_count_id = #{mediaCustomerCountId}")
    void updateIsClose(@Param("mediaCustomerCountId") Integer mediaCustomerCountId);

    Integer addMediaPrice(MediaPriceEntity mediaPriceEntity);
}
