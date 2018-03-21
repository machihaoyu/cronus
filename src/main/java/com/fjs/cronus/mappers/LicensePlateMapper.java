package com.fjs.cronus.mappers;


import com.fjs.cronus.model.LicensePlate;
import com.fjs.cronus.util.MyMapper;

import java.util.List;
import java.util.Map;

public interface LicensePlateMapper extends MyMapper<LicensePlate> {

    List<LicensePlate> getAllPlate(Map<String,Object> paramMap);

    List<String> getAllparent(Map<String,Object> paramMap);
}