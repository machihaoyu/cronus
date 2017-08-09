package com.fjs.cronus.dto.php;



import java.io.Serializable;

public class CustomerInterviewInsuranceInfoDTO implements Serializable{

    private static final long serialVersionUID = 6841315540181335020L;

    private Integer customer_interview_insurance_info_id;//`customer_interview_insurance_info_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    private Integer customer_interview_base_info_id;//`customer_interview_base_info_id` int(11) DEFAULT NULL COMMENT '面谈基础信息ID',
    private String insurance_type;//`insurance_type` varchar(50) DEFAULT NULL COMMENT '保险类型',
    private String pay_type;//`pay_type` varchar(50) DEFAULT NULL COMMENT '缴费方式',
    private String year_pay_amount;//`year_pay_amount` int(10) DEFAULT NULL COMMENT '年缴费金额',
    private String month_pay_amount;//`month_pay_amount` int(10) DEFAULT NULL COMMENT '月缴费金额',
    private String effect_date;//`effect_date` int(10) DEFAULT NULL COMMENT '生效时间',
    private String is_suspend;//`is_suspend` varchar(50) DEFAULT NULL COMMENT '有无断缴',
    private String create_time;//`create_time` int(10) DEFAULT NULL COMMENT '创建时间',
    private Integer create_user_id;//`create_user_id` int(11) DEFAULT NULL COMMENT '创建人ID',
    private String update_time;//`update_time` int(10) DEFAULT NULL COMMENT '更新时间',

    public Integer getCustomer_interview_insurance_info_id() {
        return customer_interview_insurance_info_id;
    }

    public void setCustomer_interview_insurance_info_id(Integer customer_interview_insurance_info_id) {
        this.customer_interview_insurance_info_id = customer_interview_insurance_info_id;
    }

    public Integer getCustomer_interview_base_info_id() {
        return customer_interview_base_info_id;
    }

    public void setCustomer_interview_base_info_id(Integer customer_interview_base_info_id) {
        this.customer_interview_base_info_id = customer_interview_base_info_id;
    }

    public String getInsurance_type() {
        return insurance_type;
    }

    public void setInsurance_type(String insurance_type) {
        this.insurance_type = insurance_type;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getYear_pay_amount() {
        return year_pay_amount;
    }

    public void setYear_pay_amount(String year_pay_amount) {
        this.year_pay_amount = year_pay_amount;
    }

    public String getMonth_pay_amount() {
        return month_pay_amount;
    }

    public void setMonth_pay_amount(String month_pay_amount) {
        this.month_pay_amount = month_pay_amount;
    }

    public String getEffect_date() {
        return effect_date;
    }

    public void setEffect_date(String effect_date) {
        this.effect_date = effect_date;
    }

    public String getIs_suspend() {
        return is_suspend;
    }

    public void setIs_suspend(String is_suspend) {
        this.is_suspend = is_suspend;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public Integer getCreate_user_id() {
        return create_user_id;
    }

    public void setCreate_user_id(Integer create_user_id) {
        this.create_user_id = create_user_id;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }
}
