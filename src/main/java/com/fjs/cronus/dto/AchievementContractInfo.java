package com.fjs.cronus.dto;

/**
 * 业绩下最新合同信息
 * Created by crm on 2017/5/23.
 */
public class AchievementContractInfo {

    private Integer contract_id;//22,
    private Integer status;//2,
    private Integer check_process;//5,
    private Integer check_status;//0,
    private Integer check_num;//2


    public Integer getContract_id() {
        return contract_id;
    }

    public void setContract_id(Integer contract_id) {
        this.contract_id = contract_id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Integer getCheck_num() {
        return check_num;
    }

    public void setCheck_num(Integer check_num) {
        this.check_num = check_num;
    }
}
