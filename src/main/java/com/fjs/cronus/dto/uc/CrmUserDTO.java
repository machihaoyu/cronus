package com.fjs.cronus.dto.uc;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/27 0027.
 */
public class CrmUserDTO implements Serializable {

    private static final long serialVersionUID = -4931318250406224775L;

    private Integer user_id; // 425033,
    private String name; // 测试分配,
    private Integer department_id; // 16,
    private Integer sub_company_id; // 2,
    private Integer company_id; // 1,
    private String role_ids; // 15,
    private String role_names; // CRM助理,
    private Integer parent_id; // 15,
    private String phone; // 18288888888

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(Integer department_id) {
        this.department_id = department_id;
    }

    public Integer getSub_company_id() {
        return sub_company_id;
    }

    public void setSub_company_id(Integer sub_company_id) {
        this.sub_company_id = sub_company_id;
    }

    public Integer getCompany_id() {
        return company_id;
    }

    public void setCompany_id(Integer company_id) {
        this.company_id = company_id;
    }

    public String getRole_ids() {
        return role_ids;
    }

    public void setRole_ids(String role_ids) {
        this.role_ids = role_ids;
    }

    public String getRole_names() {
        return role_names;
    }

    public void setRole_names(String role_names) {
        this.role_names = role_names;
    }

    public Integer getParent_id() {
        return parent_id;
    }

    public void setParent_id(Integer parent_id) {
        this.parent_id = parent_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
