package com.fjs.cronus.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class AgreementDTO implements Serializable {

    private static final long serialVersionUID = 1623659527822670142L;

    private Integer agreement_id;// int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
    private Integer customer_id;// int(11) NOT NULL DEFAULT '0' COMMENT '客户id',
    private Integer user_id;
    private String number;// varchar(20) NOT NULL DEFAULT '' COMMENT '居间协议编号',
    private BigDecimal plan_money;// decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '拟借款金额',
    private BigDecimal commission;// decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '佣金',
    private String status;// tinyint(3) NOT NULL DEFAULT '0' COMMENT '状态(-1:无效,0-处理中,1-成功,2-失败)',
    private Integer create_user_id;// int(11) unsigned NOT NULL DEFAULT '0' COMMENT '创建人id',
    private Integer old_create_user_id;// int(11) NOT NULL DEFAULT '0' COMMENT '原始创建人',
    private Integer create_time;// int(10) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
    private String mark;// varchar(255) DEFAULT NULL COMMENT '业务员对协议的额外备注信息',
    private String borrower;// varchar(127) NOT NULL DEFAULT '' COMMENT '借款人',
    private String product_type;// enum('抵押','信用','理财','','赎楼') NOT NULL DEFAULT '',
    private String identity;// varchar(100) NOT NULL DEFAULT '' COMMENT '借款人身份证',
    private BigDecimal rate;// decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '费率',
    private String telephone;// varchar(255) NOT NULL DEFAULT '' COMMENT '电话',
    private Date update_time;//timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    private BigDecimal deposit;//decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '预付款',
    private Integer pay_type;//int(10) NOT NULL DEFAULT '6' COMMENT '支付方式(1-银行转账,2-现金,3-pos机,4-微信,5-支付宝,6其他)',
    private Long pay_time;//int(10) NOT NULL DEFAULT '0' COMMENT '支付日期',
    private String payee;// varchar(127) DEFAULT NULL COMMENT '收款人',
    private String payee_account;// varchar(127) DEFAULT NULL COMMENT '收款账户',
    private Integer is_special;// int(1) NOT NULL DEFAULT '0' COMMENT '是不是利差合同(0-不是,1是)',
    private String template_serialize; //纸质协议
    private String time;
    private String create_user_name;//协议所属者名字;

    public String getCreate_user_name() {
        return create_user_name;
    }

    public void setCreate_user_name(String create_user_name) {
        this.create_user_name = create_user_name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemplate_serialize() {
        return template_serialize;
    }

    public void setTemplate_serialize(String template_serialize) {
        this.template_serialize = template_serialize;
    }
    public Integer getAgreement_id() {
        return agreement_id;
    }

    public void setAgreement_id(Integer agreement_id) {
        this.agreement_id = agreement_id;
    }

    public Integer getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(Integer customer_id) {
        this.customer_id = customer_id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public BigDecimal getPlan_money() {
        return plan_money;
    }

    public void setPlan_money(BigDecimal plan_money) {
        this.plan_money = plan_money;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCreate_user_id() {
        return create_user_id;
    }

    public void setCreate_user_id(Integer create_user_id) {
        this.create_user_id = create_user_id;
    }

    public Integer getOld_create_user_id() {
        return old_create_user_id;
    }

    public void setOld_create_user_id(Integer old_create_user_id) {
        this.old_create_user_id = old_create_user_id;
    }

    public Integer getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Integer create_time) {
        this.create_time = create_time;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getBorrower() {
        return borrower;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public BigDecimal getDeposit() {
        return deposit;
    }

    public void setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
    }

    public Integer getPay_type() {
        return pay_type;
    }

    public void setPay_type(Integer pay_type) {
        this.pay_type = pay_type;
    }

    public Long getPay_time() {
        return pay_time;
    }

    public void setPay_time(Long pay_time) {
        this.pay_time = pay_time;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public String getPayee_account() {
        return payee_account;
    }

    public void setPayee_account(String payee_account) {
        this.payee_account = payee_account;
    }

    public Integer getIs_special() {
        return is_special;
    }

    public void setIs_special(Integer is_special) {
        this.is_special = is_special;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }
}
