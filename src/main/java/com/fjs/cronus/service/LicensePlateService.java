package com.fjs.cronus.service;

import com.fjs.cronus.dto.cronus.ThreePcLinkAge;
import com.fjs.cronus.mappers.LicensePlateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by msi on 2017/9/19.
 */
@Service
public class LicensePlateService {

    @Autowired
    LicensePlateMapper licensePlateMapper;


    public List<ThreePcLinkAge>  getCarPlate(){
        List<ThreePcLinkAge> resultList = new ArrayList<>();
        //查询到所有的parent
        //TODO 车辆三级联动

        return  resultList;

    }
}
