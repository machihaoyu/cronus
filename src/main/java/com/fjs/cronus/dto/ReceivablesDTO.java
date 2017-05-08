package com.fjs.cronus.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by chenjie on 2017/4/26.
 */
public class ReceivablesDTO implements Serializable{
    private static final long serialVersionUID = -221919053211201440L;

    private Integer receivables_id;// int(10) NOT NULL AUTO_INCREMENT COMMENT '记录id号',
    private Integer agreement_id;// int(10) unsigned NOT NULL DEFAULT '0' COMMENT '居间协议id号',
    private Integer contract_id;// int(10) unsigned NOT NULL DEFAULT '0' COMMENT '合同id号',
    private BigDecimal commission;// decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '服务费(元)',
    private BigDecimal rate;// decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '利息(元)(现在不要了)',
    private BigDecimal principal;// decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '本金(元)(现在不要了)',
    private BigDecimal return_fee;// decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '返费(元) 返费收入',
    private BigDecimal deposit;// decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '定金(元) 预付款',
    private BigDecimal packing;// decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '包装费(元) 材料费',
    private BigDecimal channel_money;// decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '渠道费用(元) 返费支出',
    private BigDecimal other_fee;// decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '其他金额(元)',
    private Integer pay_type;// int(10) unsigned NOT NULL DEFAULT '6' COMMENT '支付方式(1-银行转账,2-现金,3-pos机,4-微信,5-支付宝,6其他)',
    private BigDecimal  pay_money;// decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '支付金额(元)',
    private Integer pay_time;// int(10) NOT NULL DEFAULT '0' COMMENT '支付日期',
    private String payee;// varchar(127) DEFAULT NULL COMMENT '收款人',
    private String payee_account;// varchar(127) DEFAULT NULL COMMENT '收款账户',
    private Integer create_user_id;// int(10) NOT NULL DEFAULT '0' COMMENT '信息创建人id',
    private Integer create_time;// int(10) NOT NULL DEFAULT '0' COMMENT '信息创建时间',
    private String mark;// varchar(255) DEFAULT NULL COMMENT '创建人对信息的备注',
    private Integer check_num;// int(10) NOT NULL DEFAULT '1' COMMENT '审核轮数:渠道一轮,自营可多轮',
    private Integer achievement_id;// int(10) NOT NULL DEFAULT '-1' COMMENT '-1:财务未审核 0:财务已审核 n:业绩被确认',
    private Integer operate_user_id;// int(10) NOT NULL DEFAULT '0' COMMENT '操作人',
    private String operate_user_name;// varchar(100) NOT NULL DEFAULT '' COMMENT '操作人姓名',
    private String pay_time_str;//yyyy-MM-dd支付日期;
    private String agreementNum;//协议编号;

    public String getAgreementNum() {
        return agreementNum;
    }

    public void setAgreementNum(String agreementNum) {
        this.agreementNum = agreementNum;
    }

    public String getPay_time_str() {
        return pay_time_str;
    }

    public void setPay_time_str(String pay_time_str) {
        this.pay_time_str = pay_time_str;
    }

    public Integer getReceivables_id() {
        return receivables_id;
    }

    public void setReceivables_id(Integer receivables_id) {
        this.receivables_id = receivables_id;
    }

    public Integer getAgreement_id() {
        return agreement_id;
    }

    public void setAgreement_id(Integer agreement_id) {
        this.agreement_id = agreement_id;
    }

    public Integer getContract_id() {
        return contract_id;
    }

    public void setContract_id(Integer contract_id) {
        this.contract_id = contract_id;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getPrincipal() {
        return principal;
    }

    public void setPrincipal(BigDecimal principal) {
        this.principal = principal;
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

    public BigDecimal getPacking() {
        return packing;
    }

    public void setPacking(BigDecimal packing) {
        this.packing = packing;
    }

    public BigDecimal getChannel_money() {
        return channel_money;
    }

    public void setChannel_money(BigDecimal channel_money) {
        this.channel_money = channel_money;
    }

    public BigDecimal getOther_fee() {
        return other_fee;
    }

    public void setOther_fee(BigDecimal other_fee) {
        this.other_fee = other_fee;
    }

    public Integer getPay_type() {
        return pay_type;
    }

    public void setPay_type(Integer pay_type) {
        this.pay_type = pay_type;
    }

    public BigDecimal getPay_money() {
        return pay_money;
    }

    public void setPay_money(BigDecimal pay_money) {
        this.pay_money = pay_money;
    }

    public Integer getPay_time() {
        return pay_time;
    }

    public void setPay_time(Integer pay_time) {
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

    public Integer getCreate_user_id() {
        return create_user_id;
    }

    public void setCreate_user_id(Integer create_user_id) {
        this.create_user_id = create_user_id;
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

    public Integer getCheck_num() {
        return check_num;
    }

    public void setCheck_num(Integer check_num) {
        this.check_num = check_num;
    }

    public Integer getAchievement_id() {
        return achievement_id;
    }

    public void setAchievement_id(Integer achievement_id) {
        this.achievement_id = achievement_id;
    }

    public Integer getOperate_user_id() {
        return operate_user_id;
    }

    public void setOperate_user_id(Integer operate_user_id) {
        this.operate_user_id = operate_user_id;
    }

    public String getOperate_user_name() {
        return operate_user_name;
    }

    public void setOperate_user_name(String operate_user_name) {
        this.operate_user_name = operate_user_name;
    }
}
