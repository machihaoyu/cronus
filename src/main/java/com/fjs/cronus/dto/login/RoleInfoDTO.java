package com.fjs.cronus.dto.login;

import java.util.List;

/**
 * Created by crm on 2017/5/9.
 */
public class RoleInfoDTO {

    private Integer role_id;
    private String name;
    private String look_phone;

    private AutoAuthorityDTO auto_authority;

    private Integer data_type;

    public Integer getRole_id() {
        return role_id;
    }

    public void setRole_id(Integer role_id) {
        this.role_id = role_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLook_phone() {
        return look_phone;
    }

    public void setLook_phone(String look_phone) {
        this.look_phone = look_phone;
    }

    public AutoAuthorityDTO getAuto_authority() {
        return auto_authority;
    }

    public void setAuto_authority(AutoAuthorityDTO auto_authority) {
        this.auto_authority = auto_authority;
    }

    public Integer getData_type() {
        return data_type;
    }

    public void setData_type(Integer data_type) {
        this.data_type = data_type;
    }
}
