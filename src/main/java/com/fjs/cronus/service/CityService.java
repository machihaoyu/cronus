package com.fjs.cronus.service;

import com.fjs.cronus.Common.ResultResource;
import com.fjs.cronus.dto.cronus.PCChrdrenDTO;
import com.fjs.cronus.dto.cronus.ThreePcLinkAgeDTO;
import com.fjs.cronus.dto.cronus.ThreelinkageDTO;
import com.fjs.cronus.mappers.UcAreaMapper;
import com.fjs.cronus.mappers.UcCityMapper;
import com.fjs.cronus.mappers.UcProvinceMapper;
import com.fjs.cronus.model.UcArea;
import com.fjs.cronus.model.UcCity;
import com.fjs.cronus.model.UcProvince;
import com.fjs.cronus.service.redis.LevallinkAgeRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by msi on 2017/9/19.
 */
@Service
public class CityService {


    @Autowired
    UcProvinceMapper ucProvinceMapper;
    @Autowired
    UcCityMapper ucCityMapper;
    @Autowired
    UcAreaMapper ucAreaMapper;
    @Autowired
    LevallinkAgeRedisService levallinkAgeRedisService;

    public List<ThreePcLinkAgeDTO> getPCCityTwoLinkAge() {
        List<ThreePcLinkAgeDTO> resultList = new ArrayList<>();
        //查询到所有的省份
        List<UcProvince> provinceList = ucProvinceMapper.selectAll();
        //遍历省份得到所属城市
        //所有的省份集合
        for (UcProvince province : provinceList) {
            ThreePcLinkAgeDTO dto = new ThreePcLinkAgeDTO();
            //判断是否是直辖市
            if (province.getIsDirectly() != 1) {
                    dto.setValue(province.getName());
                    dto.setLabel(province.getName());
                //根据城市找到当前的市区
                List<PCChrdrenDTO> cityListDTO = new ArrayList<>();
                List<UcCity> citylist = ucCityMapper.findByProviceId(province.getId());

                for (UcCity city : citylist) {
                    //判断是否是直辖市
                    PCChrdrenDTO dtocity = new PCChrdrenDTO();
                    dtocity.setLabel(city.getName());
                    dtocity.setValue(city.getName());
                    //获取所有的
                     cityListDTO.add(dtocity);
                }
                dto.setChildren(cityListDTO);
            }else {
                List cityListDTO = new ArrayList<>();
                    dto.setValue(province.getName());
                    dto.setLabel(province.getName());
                List<UcArea> listArea = ucAreaMapper.findByProvinceId(province.getId());
                for (UcArea area : listArea ) {
                    PCChrdrenDTO dtoArea = new PCChrdrenDTO();
                    dtoArea.setLabel(area.getAreaName());
                    dtoArea.setValue(area.getAreaName());
                     cityListDTO.add(dtoArea);
                }
                  dto.setChildren(cityListDTO);

            }
            resultList.add(dto);
        }

        return  resultList;
    }

    public List<ThreePcLinkAgeDTO> getPCCityThreeLinkAge(){

        List<ThreePcLinkAgeDTO> resultList = new ArrayList<>();
       //从缓存中取数据
        List pcCityThreeLinkAgeList = levallinkAgeRedisService.getLevallinkAgeInfo(ResultResource.LEAVELLINKAGE_KEY);
        if ( pcCityThreeLinkAgeList != null){
            return pcCityThreeLinkAgeList;
        }
        List<UcProvince> provinceList = ucProvinceMapper.selectAll();
        if (provinceList.size() > 0) {
            for (UcProvince province : provinceList) {
                //判断是否是直辖市
                ThreePcLinkAgeDTO dto = new ThreePcLinkAgeDTO();
                dto.setValue(province.getName());
                dto.setLabel(province.getName());
                //根据城市找到当前的市区
                List<UcCity> citylist = ucCityMapper.findByProviceId(province.getId());
                List cityListDTO = new ArrayList<>();
                for (UcCity city :citylist ) {
                    //判断是否是直辖市
                    UcProvince province2 = ucProvinceMapper.findById(city.getProvinceId());
                    if (province2.getIsDirectly() != 1) {
                        ThreePcLinkAgeDTO cityDto = new ThreePcLinkAgeDTO();
                        cityDto.setLabel(city.getName());
                        cityDto.setValue(city.getName());
                        //获取所有的
                        List<UcArea> arList = ucAreaMapper.findCity_id(city.getId());
                        List<PCChrdrenDTO> areaList = new ArrayList<>();
                        for (UcArea area : arList) {
                            PCChrdrenDTO areaDto = new PCChrdrenDTO();
                            areaDto.setValue(area.getAreaName());
                            areaDto.setLabel(area.getAreaName());
                            areaList.add(areaDto);
                        }
                        cityDto.setChildren(areaList);
                        cityListDTO.add(cityDto);

                    }else {

                        //从area表中查询数据
                        List<UcArea> areaList = ucAreaMapper.findCity_id(city.getId());

                        for (UcArea area: areaList) {
                            List<PCChrdrenDTO> dtoList = new ArrayList<>();
                            ThreePcLinkAgeDTO cityDto = new ThreePcLinkAgeDTO();
                            cityDto.setLabel(area.getAreaName());
                            cityDto.setValue(area.getAreaName());
                            PCChrdrenDTO dto1 = new PCChrdrenDTO();
                            dto1.setValue("城区");
                            dto1.setLabel("城区");
                            PCChrdrenDTO dto2 = new PCChrdrenDTO();
                            dto2.setValue("非城区 ");
                            dto2.setLabel("非城区");
                            dtoList.add(dto1);
                            dtoList.add(dto2);
                            cityDto.setChildren(dtoList);
                            cityListDTO.add(cityDto);
                        }

                    }
                }
                dto.setChildren(cityListDTO);
                resultList.add(dto);
            }

        }
        //此处开始添加缓存
        levallinkAgeRedisService.setLevallinkAgeInfo(ResultResource.LEAVELLINKAGE_KEY,resultList);
        return  resultList;
    }
}
