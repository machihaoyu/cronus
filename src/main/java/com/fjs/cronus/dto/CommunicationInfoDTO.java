package com.fjs.cronus.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 沟通记录信息
 * Created by crm on 2017/5/19.
 */
public class CommunicationInfoDTO implements Serializable {

    private List<CommunicationLogDTO> data; //沟通记录信息集合
    private String house_status;// enum('','无','有') NOT NULL DEFAULT '' COMMENT '有无房产',
    private Long loan_amount;// int(11) NOT NULL DEFAULT '0' COMMENT '意向贷款金额',
    private String purpose;  //资金用途
    private String telephonenumber; //电话号码

    public List<CommunicationLogDTO> getData() {
        return data;
    }

    public void setData(List<CommunicationLogDTO> data) {
        this.data = data;
    }

    public String getHouse_status() {
        return house_status;
    }

    public void setHouse_status(String house_status) {
        this.house_status = house_status;
    }

    public Long getLoan_amount() {
        return loan_amount;
    }

    public void setLoan_amount(Long loan_amount) {
        this.loan_amount = loan_amount;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getTelephonenumber() {
        return telephonenumber;
    }

    public void setTelephonenumber(String telephonenumber) {
        this.telephonenumber = telephonenumber;
    }
}
