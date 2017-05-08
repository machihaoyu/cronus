package com.fjs.cronus.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 获取审核步骤DTO 审核相关
 * Created by crm on 2017/5/2.
 */
public class ApprovalProcessDTO implements Serializable {

    private static final long serialVersionUID = -6113420071408106765L;
    
    private List<ApprovalProcessUserDTO> toUsers;

    private Integer process_id;
    private String process_name;

    public List<ApprovalProcessUserDTO> getToUsers() {
        return toUsers;
    }

    public void setToUsers(List<ApprovalProcessUserDTO> toUsers) {
        this.toUsers = toUsers;
    }

    public Integer getProcess_id() {
        return process_id;
    }

    public void setProcess_id(Integer process_id) {
        this.process_id = process_id;
    }

    public String getProcess_name() {
        return process_name;
    }

    public void setProcess_name(String process_name) {
        this.process_name = process_name;
    }
}
