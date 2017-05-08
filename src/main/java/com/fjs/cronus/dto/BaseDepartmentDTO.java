package com.fjs.cronus.dto;

import java.io.Serializable;

/**
 * Created by chenjie on 2017/5/3.
 */
public class BaseDepartmentDTO implements Serializable {

    private static final long serialVersionUID = 6241767444851702547L;

    private Integer department_id;// int(10) unsigned NOT NULL AUTO_INCREMENT,
    private Integer parent_id;// int(10) unsigned NOT NULL COMMENT '上级部门id',
    private String department_type;// char(33) NOT NULL COMMENT '部门类别',
    private Integer sub_company_id;// int(10) unsigned NOT NULL COMMENT '分公司',
    private String  name;// varchar(20) NOT NULL COMMENT '部门名',
    private String description;// varchar(200) DEFAULT NULL COMMENT '描述',
    private Integer status;// tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '状态 0:正常 1:删除',
    private String redis;// varchar(100) DEFAULT NULL COMMENT '分公司自动分配队列',

    public Integer getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(Integer department_id) {
        this.department_id = department_id;
    }

    public Integer getParent_id() {
        return parent_id;
    }

    public void setParent_id(Integer parent_id) {
        this.parent_id = parent_id;
    }

    public String getDepartment_type() {
        return department_type;
    }

    public void setDepartment_type(String department_type) {
        this.department_type = department_type;
    }

    public Integer getSub_company_id() {
        return sub_company_id;
    }

    public void setSub_company_id(Integer sub_company_id) {
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRedis() {
        return redis;
    }

    public void setRedis(String redis) {
        this.redis = redis;
    }
}
