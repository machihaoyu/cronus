package com.fjs.cronus.dto.uc;

/**
 * Created by msi on 2017/7/6.
 */
public class RoleDTO {
    private String role_id;
    private String name;
    private String status;
    private String description;
    private String modules;
    private String actions;
    private String data_type;
    private String auto_authority;
    private String look_phone;
    private String c_role_id;

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModules() {
        return modules;
    }

    public void setModules(String modules) {
        this.modules = modules;
    }

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    public String getData_type() {
        return data_type;
    }

    public void setData_type(String data_type) {
        this.data_type = data_type;
    }

    public String getAuto_authority() {
        return auto_authority;
    }

    public void setAuto_authority(String auto_authority) {
        this.auto_authority = auto_authority;
    }

    public String getLook_phone() {
        return look_phone;
    }

    public void setLook_phone(String look_phone) {
        this.look_phone = look_phone;
    }

    public String getC_role_id() {
        return c_role_id;
    }

    public void setC_role_id(String c_role_id) {
        this.c_role_id = c_role_id;
    }
}
