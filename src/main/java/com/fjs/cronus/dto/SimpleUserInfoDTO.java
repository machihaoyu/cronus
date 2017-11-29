package com.fjs.cronus.dto;

import java.io.Serializable;

/**
 * 简单用户信息
 *
 * Created by feng on 2017/10/10.
 */
public class SimpleUserInfoDTO implements Serializable{

    private static final long serialVersionUID = 1L;

    private String sex;

    private String department_id;

    private String address;

    private String name;

    private String user_id;

    private String sub_company_id;

    private String user_type;

    private String company_id;

    private String telephone;

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSub_company_id() {
        return sub_company_id;
    }

    public void setSub_company_id(String sub_company_id) {
        this.sub_company_id = sub_company_id;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
