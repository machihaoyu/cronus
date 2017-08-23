package com.fjs.cronus.dto.php;



import java.io.Serializable;

public class CustomerInterviewCarInfoDTO implements Serializable{

    private static final long serialVersionUID = 6841315540181335020L;

    private Integer customer_interview_car_info_id;//`customer_interview_car_info_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    private Integer customer_interview_base_info_id;//`customer_interview_base_info_id` int(11) DEFAULT NULL COMMENT '面谈基础信息ID',
    private String car_type;//`car_type` varchar(50) DEFAULT NULL COMMENT '车辆类型',
    private String licence_plate_location;//`licence_plate_location` varchar(255) DEFAULT NULL COMMENT '车牌地区',
    private String buy_date;//`buy_date` int(10) DEFAULT NULL COMMENT '购买时间',
    private String mortgage_paid_num;//`mortgage_paid_num` int(10) DEFAULT NULL COMMENT '按揭月供已还月数',
    private String mortgage_month_amount;//`mortgage_month_amount` int(10) DEFAULT NULL COMMENT '按揭月供金额',
    private String price_now;//`price_now` decimal(10,2) DEFAULT NULL COMMENT '现车价',
    private String is_full_insurance;//`is_full_insurance` varchar(50) DEFAULT NULL COMMENT '是否两险齐全',
    private String create_time;//`create_time` int(10) DEFAULT NULL COMMENT '创建时间',
    private String create_user_id;//`creat_user_id` int(11) DEFAULT NULL COMMENT '创建人ID',
    private String update_time;//`update_time` int(10) DEFAULT NULL COMMENT '更新时间',

    public Integer getCustomer_interview_car_info_id() {
        return customer_interview_car_info_id;
    }

    public void setCustomer_interview_car_info_id(Integer customer_interview_car_info_id) {
        this.customer_interview_car_info_id = customer_interview_car_info_id;
    }

    public Integer getCustomer_interview_base_info_id() {
        return customer_interview_base_info_id;
    }

    public void setCustomer_interview_base_info_id(Integer customer_interview_base_info_id) {
        this.customer_interview_base_info_id = customer_interview_base_info_id;
    }

    public String getCar_type() {
        return car_type;
    }

    public void setCar_type(String car_type) {
        this.car_type = car_type;
    }

    public String getLicence_plate_location() {
        return licence_plate_location;
    }

    public void setLicence_plate_location(String licence_plate_location) {
        this.licence_plate_location = licence_plate_location;
    }

    public String getBuy_date() {
        return buy_date;
    }

    public void setBuy_date(String buy_date) {
        this.buy_date = buy_date;
    }

    public String getMortgage_paid_num() {
        return mortgage_paid_num;
    }

    public void setMortgage_paid_num(String mortgage_paid_num) {
        this.mortgage_paid_num = mortgage_paid_num;
    }

    public String getMortgage_month_amount() {
        return mortgage_month_amount;
    }

    public void setMortgage_month_amount(String mortgage_month_amount) {
        this.mortgage_month_amount = mortgage_month_amount;
    }

    public String getPrice_now() {
        return price_now;
    }

    public void setPrice_now(String price_now) {
        this.price_now = price_now;
    }

    public String getIs_full_insurance() {
        return is_full_insurance;
    }

    public void setIs_full_insurance(String is_full_insurance) {
        this.is_full_insurance = is_full_insurance;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getCreate_user_id() {
        return create_user_id;
    }

    public void setCreate_user_id(String create_user_id) {
        this.create_user_id = create_user_id;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }
}
