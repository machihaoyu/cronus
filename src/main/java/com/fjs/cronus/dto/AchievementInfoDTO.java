package com.fjs.cronus.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 审核(客户-合同-业绩，审核是当时的业绩合同、协议、回款)
 * 根据审核业绩查询合同信息、回款信息
 * Created by crm on 2017/5/3.
 */
public class AchievementInfoDTO implements Serializable {

    private static final long serialVersionUID = 3711303693613174421L;

    private Integer achievement_id; //13,
    private Integer parent_id; //0,
    private Integer contract_id; //22,
    private Integer agreement_id; //108,
    private Integer user_id; //1,
    private Double commission_total; //32.00,
    private Double channel_money; //0.00,
    private Double principal; //0.00,
    private Double rate; //0.00,
    private Double packing; //0.00,
    private Double return_fee; //0.00,
    private Double deposit; //32.00,
    private Double commission_net; //32.00,
    private AgreementDTO agreement_info;
    private ContractDTO contract_info;
    private List<ReceivablesDTO> receivables_infos;
    private Integer status; //1,
    private Integer create_time; //1476165963,
    private Integer check_time; //1476176446,
    private Integer confirm_time; //0

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

    public Double getCommission_total() {
        return commission_total;
    }

    public void setCommission_total(Double commission_total) {
        this.commission_total = commission_total;
    }

    public Double getChannel_money() {
        return channel_money;
    }

    public void setChannel_money(Double channel_money) {
        this.channel_money = channel_money;
    }

    public Double getPrincipal() {
        return principal;
    }

    public void setPrincipal(Double principal) {
        this.principal = principal;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Double getPacking() {
        return packing;
    }

    public void setPacking(Double packing) {
        this.packing = packing;
    }

    public Double getReturn_fee() {
        return return_fee;
    }

    public void setReturn_fee(Double return_fee) {
        this.return_fee = return_fee;
    }

    public Double getDeposit() {
        return deposit;
    }

    public void setDeposit(Double deposit) {
        this.deposit = deposit;
    }

    public Double getCommission_net() {
        return commission_net;
    }

    public void setCommission_net(Double commission_net) {
        this.commission_net = commission_net;
    }

    public AgreementDTO getAgreement_info() {
        return agreement_info;
    }

    public void setAgreement_info(AgreementDTO agreement_info) {
        this.agreement_info = agreement_info;
    }

    public ContractDTO getContract_info() {
        return contract_info;
    }

    public void setContract_info(ContractDTO contract_info) {
        this.contract_info = contract_info;
    }

    public List<ReceivablesDTO> getReceivables_infos() {
        return receivables_infos;
    }

    public void setReceivables_infos(List<ReceivablesDTO> receivables_infos) {
        this.receivables_infos = receivables_infos;
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
}
