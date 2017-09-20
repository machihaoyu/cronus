package com.fjs.cronus.mappers;


import com.fjs.cronus.model.UcCity;
import com.fjs.cronus.util.MyMapper;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface UcCityMapper extends MyMapper<UcCity> {

    List<UcCity> findByProviceId(@Param(value = "province_Id") Integer province_Id);
}