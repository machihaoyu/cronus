package com.fjs.cronus.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 我已提交结案的
 * Created by crm on 2017/5/2.
 */
public class CheckClosedDTO implements Serializable {

    private static final long serialVersionUID = 305239903173010703L;

    private Integer achievement_id;//33,
    private Integer parent_id;//0,
    private Integer contract_id;//84,
    private Integer agreement_id;//185,
    private Integer user_id;//15,
    private BigDecimal commission_total;//12.00,
    private BigDecimal channel_money;//0.00,
    private BigDecimal principal;//0.00,
    private BigDecimal rate;//0.00,
    private BigDecimal packing;//0.00,
    private BigDecimal return_fee;//0.00,
    private BigDecimal deposit;//0.00,
    private BigDecimal commission_net;//12.00,
    private Integer status;//0,
    private Integer create_time;//1481010489,
    private Integer check_time;//1487827947,
    private Integer confirm_time;//0,
    private String customer_name;//客户221257,
    private Integer contract_type;//2,
    private String contract_status;//已结束,
    private String check_num_name;//第1轮,
    private String check_user_name;//上海分公司财务,
    private String check_name;//财务确认,
    private Integer check_status;//0,
    private Integer check_create_time;//1487827947

    private Integer check_process;
    private String check_process_name;
    private String next_check_name;


    public Integer getAchievement_id() {
        return achievement_id;
    }

    public void setAchievement_id(Integer achievement_id) {
        this.achievement_id = achievement_id;
    }

    public Integer getParent_id() {
        return parent_id;
    }

    public void setParent_id(Integer parent_id) {
        this.parent_id = parent_id;
    }

    public Integer getContract_id() {
        return contract_id;
    }

    public void setContract_id(Integer contract_id) {
        this.contract_id = contract_id;
    }

    public Integer getAgreement_id() {
        return agreement_id;
    }

    public void setAgreement_id(Integer agreement_id) {
        this.agreement_id = agreement_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public BigDecimal getCommission_total() {
        return commission_total;
    }

    public void setCommission_total(BigDecimal commission_total) {
        this.commission_total = commission_total;
    }

    public BigDecimal getChannel_money() {
        return channel_money;
    }

    public void setChannel_money(BigDecimal channel_money) {
        this.channel_money = channel_money;
    }

    public BigDecimal getPrincipal() {
        return principal;
    }

    public void setPrincipal(BigDecimal principal) {
        this.principal = principal;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getPacking() {
        return packing;
    }

    public void setPacking(BigDecimal packing) {
        this.packing = packing;
    }

    public BigDecimal getReturn_fee() {
        return return_fee;
    }

    public void setReturn_fee(BigDecimal return_fee) {
        this.return_fee = return_fee;
    }

    public BigDecimal getDeposit() {
        return deposit;
    }

    public void setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
    }

    public BigDecimal getCommission_net() {
        return commission_net;
    }

    public void setCommission_net(BigDecimal commission_net) {
        this.commission_net = commission_net;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Integer create_time) {
        this.create_time = create_time;
    }

    public Integer getCheck_time() {
        return check_time;
    }

    public void setCheck_time(Integer check_time) {
        this.check_time = check_time;
    }

    public Integer getConfirm_time() {
        return confirm_time;
    }

    public void setConfirm_time(Integer confirm_time) {
        this.confirm_time = confirm_time;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public Integer getContract_type() {
        return contract_type;
    }

    public void setContract_type(Integer contract_type) {
        this.contract_type = contract_type;
    }

    public String getContract_status() {
        return contract_status;
    }

    public void setContract_status(String contract_status) {
        this.contract_status = contract_status;
    }

    public String getCheck_num_name() {
        return check_num_name;
    }

    public void setCheck_num_name(String check_num_name) {
        this.check_num_name = check_num_name;
    }

    public String getCheck_user_name() {
        return check_user_name;
    }

    public void setCheck_user_name(String check_user_name) {
        this.check_user_name = check_user_name;
    }

    public String getCheck_name() {
        return check_name;
    }

    public void setCheck_name(String check_name) {
        this.check_name = check_name;
    }

    public Integer getCheck_status() {
        return check_status;
    }

    public void setCheck_status(Integer check_status) {
        this.check_status = check_status;
    }

    public Integer getCheck_create_time() {
        return check_create_time;
    }

    public void setCheck_create_time(Integer check_create_time) {
        this.check_create_time = check_create_time;
    }

    public Integer getCheck_process() {
        return check_process;
    }

    public void setCheck_process(Integer check_process) {
        this.check_process = check_process;
    }

    public String getCheck_process_name() {
        return check_process_name;
    }

    public void setCheck_process_name(String check_process_name) {
        this.check_process_name = check_process_name;
    }

    public String getNext_check_name() {
        return next_check_name;
    }

    public void setNext_check_name(String next_check_name) {
        this.next_check_name = next_check_name;
    }
}
