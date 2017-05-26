package com.fjs.cronus.dto;

import java.io.Serializable;

/**
 * Created by chenjie on 2017/5/26.
 * 获取合同的审核记录DTO
 */
public class ContractCheckDTO implements Serializable{

    private static final long serialVersionUID = 1286137104539622500L;

    private Integer check_id;
    private Integer contract_id;
    private Integer check_user_id;
    private Integer to_user_id;
    private Integer check_status;
    private String suggestion;
    private Long create_time;
    private String check_process;
    private Integer next_process;
    private Integer is_valid;
    private Integer achievement_id;
    private String check_user_name;

    public Integer getCheck_id() {
        return check_id;
    }

    public void setCheck_id(Integer check_id) {
        this.check_id = check_id;
    }

    public Integer getContract_id() {
        return contract_id;
    }

    public void setContract_id(Integer contract_id) {
        this.contract_id = contract_id;
    }

    public Integer getCheck_user_id() {
        return check_user_id;
    }

    public void setCheck_user_id(Integer check_user_id) {
        this.check_user_id = check_user_id;
    }

    public Integer getTo_user_id() {
        return to_user_id;
    }

    public void setTo_user_id(Integer to_user_id) {
        this.to_user_id = to_user_id;
    }

    public Integer getCheck_status() {
        return check_status;
    }

    public void setCheck_status(Integer check_status) {
        this.check_status = check_status;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public Long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Long create_time) {
        this.create_time = create_time;
    }

    public String getCheck_process() {
        return check_process;
    }

    public void setCheck_process(String check_process) {
        this.check_process = check_process;
    }

    public Integer getNext_process() {
        return next_process;
    }

    public void setNext_process(Integer next_process) {
        this.next_process = next_process;
    }

    public Integer getIs_valid() {
        return is_valid;
    }

    public void setIs_valid(Integer is_valid) {
        this.is_valid = is_valid;
    }

    public Integer getAchievement_id() {
        return achievement_id;
    }

    public void setAchievement_id(Integer achievement_id) {
        this.achievement_id = achievement_id;
    }

    public String getCheck_user_name() {
        return check_user_name;
    }

    public void setCheck_user_name(String check_user_name) {
        this.check_user_name = check_user_name;
    }
}
