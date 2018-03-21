package com.fjs.cronus.dto.uc;

/**
 * Created by Administrator on 2017/6/20 0020.
 */
public class DepartmentDto {
    private Integer id;
    private Integer departmentType;
    private String parentDepartment;
    private String description;
    private String name;
    private Integer cityId;
    private String cityName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDepartmentType() {
        return departmentType;
    }

    public void setDepartmentType(Integer departmentType) {
        this.departmentType = departmentType;
    }

    public String getParentDepartment() {
        return parentDepartment;
    }

    public void setParentDepartment(String parentDepartment) {
        this.parentDepartment = parentDepartment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
