package com.fjs.cronus.dto.api.uc;

import java.util.List;

/**
 * Created by Administrator on 2017/6/22 0022.
 */
public class CompanyDto {
    private Integer id;
    private String name;
    private String short_name;
    private Integer type;
    private Integer status;
    private String description;
    private Integer cityId;
    private String cityName;
    private List<ProvinceDto> provinceDtoList;
    private List<CityDto> cityDtoList;
    private Integer provinceId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public List<ProvinceDto> getProvinceDtoList() {
        return provinceDtoList;
    }

    public void setProvinceDtoList(List<ProvinceDto> provinceDtoList) {
        this.provinceDtoList = provinceDtoList;
    }

    public List<CityDto> getCityDtoList() {
        return cityDtoList;
    }

    public void setCityDtoList(List<CityDto> cityDtoList) {
        this.cityDtoList = cityDtoList;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }
}
