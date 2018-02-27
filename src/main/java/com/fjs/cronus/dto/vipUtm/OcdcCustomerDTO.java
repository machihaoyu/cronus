package com.fjs.cronus.dto.vipUtm;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by msi on 2018/2/26.
 */
public class OcdcCustomerDTO implements Serializable {

    @ApiModelProperty(value = "客户id",notes = "客户id")
    private String id;

    @ApiModelProperty(value = "手机号",notes = "手机号")
    private String telephonenumber;

    @ApiModelProperty(value = "姓名",notes = "姓名")
    private String name;

    @ApiModelProperty(value = "城市",notes = "城市")
    private String city;

    @ApiModelProperty(value = "意向金额",notes = "意向金额")
    private String loan_amount;

    @ApiModelProperty(value = "是否重复 0非重复，1 重复",notes = "是否重复 0非重复，1 重复")
    private String is_repeat;

    @ApiModelProperty(value = "是否推送 0未推送，1 已推送",notes = "是否推送 0未推送，1 已推送")
    private String push_status;

    @ApiModelProperty(value = "推送时间",notes = "推送时间")
    private String push_time;

    @ApiModelProperty(value = "来自",notes = "来自")
    private String from_source;

    @ApiModelProperty(value = "来源",notes = "来源")
    private String customer_source;

    @ApiModelProperty(value = "渠道",notes = "渠道")
    private String utm_source;

    @ApiModelProperty(value = "申请时间",notes = "申请时间")
    private String create_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTelephonenumber() {
        return telephonenumber;
    }

    public void setTelephonenumber(String telephonenumber) {
        this.telephonenumber = telephonenumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLoan_amount() {
        return loan_amount;
    }

    public void setLoan_amount(String loan_amount) {
        this.loan_amount = loan_amount;
    }

    public String getIs_repeat() {
        return is_repeat;
    }

    public void setIs_repeat(String is_repeat) {
        this.is_repeat = is_repeat;
    }

    public String getPush_status() {
        return push_status;
    }

    public void setPush_status(String push_status) {
        this.push_status = push_status;
    }

    public String getPush_time() {
        return push_time;
    }

    public void setPush_time(String push_time) {
        this.push_time = push_time;
    }

    public String getFrom_source() {
        return from_source;
    }

    public void setFrom_source(String from_source) {
        this.from_source = from_source;
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

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
