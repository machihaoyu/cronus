package com.fjs.cronus.dto.uc;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/20 0020.
 */
public class UserModelDTO implements Serializable {

    private static final long serialVersionUID = -7034111903874307000L;

    private String user_id;
    private String role_ids;
    private String user_type;
    private String name;
    private String password;
    private String sex;
    private String email;
    private String telephone ;
    private String scale;
    private String level;
    private String residence;
    private String address;
    private String last_login_time;
    private String employment_time;
    private String create_time;
    private String status;
    private String sub_company_id;
    private String department_id;
    private String tq_uin;
    private String tq_uin_pwd;
    private String update_time;
    private String openid;


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getRole_ids() {
        return role_ids;
    }

    public void setRole_ids(String role_ids) {
        this.role_ids = role_ids;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLast_login_time() {
        return last_login_time;
    }

    public void setLast_login_time(String last_login_time) {
        this.last_login_time = last_login_time;
    }

    public String getEmployment_time() {
        return employment_time;
    }

    public void setEmployment_time(String employment_time) {
        this.employment_time = employment_time;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSub_company_id() {
        return sub_company_id;
    }

    public void setSub_company_id(String sub_company_id) {
        this.sub_company_id = sub_company_id;
    }

    public String getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
    }

    public String getTq_uin() {
        return tq_uin;
    }

    public void setTq_uin(String tq_uin) {
        this.tq_uin = tq_uin;
    }

    public String getTq_uin_pwd() {
        return tq_uin_pwd;
    }

    public void setTq_uin_pwd(String tq_uin_pwd) {
        this.tq_uin_pwd = tq_uin_pwd;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
