package com.fjs.cronus.service;

import com.fjs.cronus.dto.cronus.PCChrdrenDTO;
import com.fjs.cronus.dto.cronus.ThreePcLinkAgeDTO;
import com.fjs.cronus.mappers.LicensePlateMapper;
import com.fjs.cronus.model.LicensePlate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by msi on 2017/9/19.
 */
@Service
public class LicensePlateService {

    @Autowired
    LicensePlateMapper licensePlateMapper;


    public List<ThreePcLinkAgeDTO>  getCarPlate(){
        List<ThreePcLinkAgeDTO> resultList = new ArrayList<>();
        //查询到所有的parent
        //查到所有
        Map<String,Object> paramMap = new HashMap<>();
        List<String> licensePlates = licensePlateMapper.getAllparent(paramMap);
        //去除重复的parent
        List<String> newList = new ArrayList(new HashSet(licensePlates));

        if (licensePlates.size() > 0){
            for (String str : newList) {
                ThreePcLinkAgeDTO dto = new ThreePcLinkAgeDTO();
                dto.setLabel(str);
                dto.setValue(str);
                //查询所有的子集
                paramMap.put("parentValue",str);
                List<LicensePlate> childreList = licensePlateMapper.getAllPlate(paramMap);
                paramMap.clear();
                List childList = new ArrayList();
                if (childreList != null && childreList.size() > 0){
                    for (LicensePlate  licensePlate1 : childreList ) {
                        PCChrdrenDTO chrdrenDTO = new PCChrdrenDTO();
                        chrdrenDTO.setLabel(licensePlate1.getName());
                        chrdrenDTO.setValue(licensePlate1.getName());
                        childList.add(chrdrenDTO);
                    }
                    dto.setChildren(childList);
                }
                resultList.add(dto);
            }
        }


        return  resultList;

    }
}
