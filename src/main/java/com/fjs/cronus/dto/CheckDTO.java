package com.fjs.cronus.dto;

import java.io.Serializable;

/**
 * 审核DTO
 * Created by crm on 2017/5/2.
 */
public class CheckDTO implements Serializable {

    private static final long serialVersionUID = -4058194366282416529L;

    private Integer check_id; //1751,
    private Integer contract_id; //27484,
    private Integer achievement_id; //925,
    private Integer check_user_id; //14,
    private Integer to_user_id; //15,
    private Integer check_status; //0,
    private Integer create_time; //1491450574,
    private String check_process; //1,
    private Integer next_process; //2,
    private Integer is_valid; //1,
    private Integer user_id; //14,
    private Integer status; //-1,
    private Integer agreement_id; //27569,
    private String customer_name; //萌萌,
    private String contract_type; //1,
    private String check_user_name; //业务员测试,
    private String to_user_name; //团队长测试,
    private String check_name; //结案申请,
    private String next_check_name; //团队长确认,
    private String create_user_name; //业务员测试
    private String suggestion;

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

    public Integer getAchievement_id() {
        return achievement_id;
    }

    public void setAchievement_id(Integer achievement_id) {
        this.achievement_id = achievement_id;
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

    public Integer getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Integer create_time) {
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

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getAgreement_id() {
        return agreement_id;
    }

    public void setAgreement_id(Integer agreement_id) {
        this.agreement_id = agreement_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getContract_type() {
        return contract_type;
    }

    public void setContract_type(String contract_type) {
        this.contract_type = contract_type;
    }

    public String getCheck_user_name() {
        return check_user_name;
    }

    public void setCheck_user_name(String check_user_name) {
        this.check_user_name = check_user_name;
    }

    public String getTo_user_name() {
        return to_user_name;
    }

    public void setTo_user_name(String to_user_name) {
        this.to_user_name = to_user_name;
    }

    public String getCheck_name() {
        return check_name;
    }

    public void setCheck_name(String check_name) {
        this.check_name = check_name;
    }

    public String getNext_check_name() {
        return next_check_name;
    }

    public void setNext_check_name(String next_check_name) {
        this.next_check_name = next_check_name;
    }

    public String getCreate_user_name() {
        return create_user_name;
    }

    public void setCreate_user_name(String create_user_name) {
        this.create_user_name = create_user_name;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }
}
