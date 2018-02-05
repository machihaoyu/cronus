package com.fjs.cronus.dto.uc;

import java.io.Serializable;

/**
 * Created by msi on 2018/2/5.
 */
public class CronusUserInfoDto implements Serializable {

    private String user_ids;

    private String department_ids;

    private Integer sub_company_id;

    private String flag;

    private Integer page;

    private Integer size;

    private String name;

    private Integer status;

    public String getUser_ids() {
        return user_ids;
    }

    public void setUser_ids(String user_ids) {
        this.user_ids = user_ids;
    }

    public String getDepartment_ids() {
        return department_ids;
    }

    public void setDepartment_ids(String department_ids) {
        this.department_ids = department_ids;
    }

    public Integer getSub_company_id() {
        return sub_company_id;
    }

    public void setSub_company_id(Integer sub_company_id) {
        this.sub_company_id = sub_company_id;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
