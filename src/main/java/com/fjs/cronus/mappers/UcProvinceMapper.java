package com.fjs.cronus.mappers;

import com.fjs.cronus.model.UcProvince;
import com.fjs.cronus.util.MyMapper;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.method.P;

public interface UcProvinceMapper extends MyMapper<UcProvince> {

    UcProvince findById(@Param(value = "id") Integer id);
}