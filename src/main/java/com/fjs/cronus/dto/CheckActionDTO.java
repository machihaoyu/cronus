package com.fjs.cronus.dto;

import java.io.Serializable;

/**
 * 审核操作参数对象
 * Created by crm on 2017/5/2.
 */
public class CheckActionDTO implements Serializable {

    private static final long serialVersionUID = 3542084728337670929L;

    private Integer achievement_id ;// ---> 38560
    private Integer contract_id;// ---> 27025
    private Integer check_process;// ---> 1
    private Integer check_status;// ---> 0
    private String suggestion;   // ---> 11
    private Integer next_process;// ---> 3
    private Integer to_user_id;// ---> 16
    private Integer user_id;// ---> 15

    public Integer getAchievement_id() {
        return achievement_id;
    }

    public void setAchievement_id(Integer achievement_id) {
        this.achievement_id = achievement_id;
    }

    public Integer getContract_id() {
        return contract_id;
    }

    public void setContract_id(Integer contract_id) {
        this.contract_id = contract_id;
    }

    public Integer getCheck_process() {
        return check_process;
    }

    public void setCheck_process(Integer check_process) {
        this.check_process = check_process;
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

    public Integer getNext_process() {
        return next_process;
    }

    public void setNext_process(Integer next_process) {
        this.next_process = next_process;
    }

    public Integer getTo_user_id() {
        return to_user_id;
    }

    public void setTo_user_id(Integer to_user_id) {
        this.to_user_id = to_user_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }
}
