package com.fjs.cronus.mappers;


import com.fjs.cronus.entity.CustomerPriceEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;


@Mapper
public interface CustomerPriceMapper {


    @Update("UPDATE customer_price SET is_close = 1 WHERE is_deleted = 0 AND is_close = 0 AND customer_info_id = #{customerInfoId}")
    void updateIsClose(@Param("customerInfoId") Integer customerInfoId);

    Integer addCustomerPrice(CustomerPriceEntity customerPriceEntity);
}
