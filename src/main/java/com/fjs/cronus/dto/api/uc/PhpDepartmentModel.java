package com.fjs.cronus.dto.api.uc;

/**
 * Created by msi on 2017/7/1.
 */
public class PhpDepartmentModel {
    private String department_id;
    private String parent_id;
    private String department_type;
    private String sub_company_id;
    private String name;
    private String description;
    private String status;
    private String redis;

    public String getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getDepartment_type() {
        return department_type;
    }

    public void setDepartment_type(String department_type) {
        this.department_type = department_type;
    }

    public String getSub_company_id() {
        return sub_company_id;
    }

    public void setSub_company_id(String sub_company_id) {
        this.sub_company_id = sub_company_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRedis() {
        return redis;
    }

    public void setRedis(String redis) {
        this.redis = redis;
    }
}
