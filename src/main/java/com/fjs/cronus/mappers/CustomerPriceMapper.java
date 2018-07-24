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

    @Update("UPDATE customer_price SET is_receive = 1,gmt_receive = NOW(),is_close = 1 WHERE is_close = 0 AND is_deleted = 0 AND customer_info_id = #{customerId}")
    Integer receiveSuccess(@Param("customerId") Integer customerId);

    CustomerPriceEntity getCustomerPriceByCustomerId(@Param("customerId") Integer customerId);
}
