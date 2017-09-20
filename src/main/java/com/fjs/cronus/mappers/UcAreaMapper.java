package com.fjs.cronus.mappers;

import com.fjs.cronus.model.UcArea;
import com.fjs.cronus.util.MyMapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UcAreaMapper extends MyMapper<UcArea> {


    List<UcArea> findByProvinceId(@Param(value = "province_Id")Integer province_Id);
    List<UcArea> findCity_id(@Param(value = "cityId") Integer cityId);
}