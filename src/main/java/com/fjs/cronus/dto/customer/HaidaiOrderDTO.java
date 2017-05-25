package com.fjs.cronus.dto.customer;

import java.io.Serializable;

/**
 * Created by chenjie on 2017/5/25.
 */
public class HaidaiOrderDTO implements Serializable{

    private static final long serialVersionUID = 2762447370903404321L;

    private Integer id;//海贷魔方客户id;
    private Integer sale_id;//sale系统客户id;
    private String name;//用户名;
    private String telephone;//现在电话;
    private String old_telephone;//第一次进入时电话;
    private Integer loan_amount;//意向金额
    private String  city;//客户城市;
    private String customer_source;//客户来源;
    private String utm_source;//客户渠道;
    private Integer status;//客户状态;
    private Long create_time;//创建时间;
    private Long update_time;//更新时间;
    private String loan_id;//海贷魔方订单号;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSale_id() {
        return sale_id;
    }

    public void setSale_id(Integer sale_id) {
        this.sale_id = sale_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getOld_telephone() {
        return old_telephone;
    }

    public void setOld_telephone(String old_telephone) {
        this.old_telephone = old_telephone;
    }

    public Integer getLoan_amount() {
        return loan_amount;
    }

    public void setLoan_amount(Integer loan_amount) {
        this.loan_amount = loan_amount;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCustomer_source() {
        return customer_source;
    }

    public void setCustomer_source(String customer_source) {
        this.customer_source = customer_source;
    }

    public String getUtm_source() {
        return utm_source;
    }

    public void setUtm_source(String utm_source) {
        this.utm_source = utm_source;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Long create_time) {
        this.create_time = create_time;
    }

    public Long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Long update_time) {
        this.update_time = update_time;
    }

    public String getLoan_id() {
        return loan_id;
    }

    public void setLoan_id(String loan_id) {
        this.loan_id = loan_id;
    }
}
