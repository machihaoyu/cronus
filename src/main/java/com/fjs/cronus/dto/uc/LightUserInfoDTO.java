package com.fjs.cronus.dto.uc;

public class LightUserInfoDTO {
    private Integer id;
    private String sub_company;
    private String department;
    private String sex;
    private String telephone;
    private String entry_time;
    private String status;
    private String name;
    private int departmentId;
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getId() {
        return id;
    }

    public void setSub_company(String sub_company) {
        this.sub_company = sub_company;
    }
    public String getSub_company() {
        return sub_company;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
    public String getDepartment() {
        return department;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getSex() {
        return sex;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    public String getTelephone() {
        return telephone;
    }

    public void setEntry_time(String entry_time) {
        this.entry_time = entry_time;
    }
    public String getEntry_time() {
        return entry_time;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }
    public int getDepartmentId() {
        return departmentId;
    }

}
