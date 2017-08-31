package com.fjs.cronus.dto.php;



import java.io.Serializable;

public class CustomerInterviewHouseInfoDTO implements Serializable{

    private static final long serialVersionUID = 6841315540181335020L;

    private Integer customer_interview_house_info_id;//`customer_interview_insurance_info_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    private Integer customer_interview_base_info_id;//`customer_interview_base_info_id` int(11) DEFAULT NULL COMMENT '面谈基础信息ID',
    private String house_status;//`house_status` varchar(50) DEFAULT NULL COMMENT '产权状态',
    private String house_property_type;//`house_property_type` varchar(50) DEFAULT NULL COMMENT '产权类型',
    private String area;//`area` decimal(10,2) DEFAULT NULL COMMENT '面积',
    private String build_date;//`build_date` int(10) DEFAULT NULL COMMENT '建成时间',
    private String house_property_rights_num;//`house_property_rights_num` int(11) DEFAULT NULL COMMENT '产权人数',
    private String is_child_in_property_rigths;//`is_child_in_property_rigths` varchar(50) DEFAULT NULL COMMENT '产权中是否有小孩',
    private String is_old_in_property_rigths;//`is_old_in_property_rigths` varchar(50) DEFAULT NULL COMMENT '产权中是否有老人',
    private String is_property_rights_clear;//`is_property_rights_clear` varchar(50) DEFAULT NULL COMMENT '房产权属是否清晰',
    private String is_other_house;//`is_other_house` varchar(50) DEFAULT NULL COMMENT '有无备用房',
    private String is_bank_flow;//`is_bank_flow` varchar(50) DEFAULT NULL COMMENT '是否有银行流水',
    private String bank_flow_month_amount;//`bank_flow_month_amount` int(10) DEFAULT NULL COMMENT '月流水金额',
    private String mortgage_month_amount;//`mortgage_month_amount` int(50) DEFAULT NULL COMMENT '按揭月供金额',
    private String mortgage_paid_num;//`mortgage_paid_num` int(10) DEFAULT NULL COMMENT '按揭已还月份',
    private String create_time;//`create_time` int(10) DEFAULT NULL COMMENT '创建时间',
    private Integer create_user_id;//`create_user_id` int(11) DEFAULT NULL COMMENT '创建人ID',
    private String update_time;//`update_time` int(10) DEFAULT NULL COMMENT '更新时间',

    public Integer getCustomer_interview_house_info_id() {
        return customer_interview_house_info_id;
    }

    public void setCustomer_interview_house_info_id(Integer customer_interview_house_info_id) {
        this.customer_interview_house_info_id = customer_interview_house_info_id;
    }

    public Integer getCustomer_interview_base_info_id() {
        return customer_interview_base_info_id;
    }

    public void setCustomer_interview_base_info_id(Integer customer_interview_base_info_id) {
        this.customer_interview_base_info_id = customer_interview_base_info_id;
    }

    public String getHouse_status() {
        return house_status;
    }

    public void setHouse_status(String house_status) {
        this.house_status = house_status;
    }

    public String getHouse_property_type() {
        return house_property_type;
    }

    public void setHouse_property_type(String house_property_type) {
        this.house_property_type = house_property_type;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getBuild_date() {
        return build_date;
    }

    public void setBuild_date(String build_date) {
        this.build_date = build_date;
    }

    public String getHouse_property_rights_num() {
        return house_property_rights_num;
    }

    public void setHouse_property_rights_num(String house_property_rights_num) {
        this.house_property_rights_num = house_property_rights_num;
    }

    public String getIs_child_in_property_rigths() {
        return is_child_in_property_rigths;
    }

    public void setIs_child_in_property_rigths(String is_child_in_property_rigths) {
        this.is_child_in_property_rigths = is_child_in_property_rigths;
    }

    public String getIs_old_in_property_rigths() {
        return is_old_in_property_rigths;
    }

    public void setIs_old_in_property_rigths(String is_old_in_property_rigths) {
        this.is_old_in_property_rigths = is_old_in_property_rigths;
    }

    public String getIs_property_rights_clear() {
        return is_property_rights_clear;
    }

    public void setIs_property_rights_clear(String is_property_rights_clear) {
        this.is_property_rights_clear = is_property_rights_clear;
    }

    public String getIs_other_house() {
        return is_other_house;
    }

    public void setIs_other_house(String is_other_house) {
        this.is_other_house = is_other_house;
    }

    public String getIs_bank_flow() {
        return is_bank_flow;
    }

    public void setIs_bank_flow(String is_bank_flow) {
        this.is_bank_flow = is_bank_flow;
    }

    public String getBank_flow_month_amount() {
        return bank_flow_month_amount;
    }

    public void setBank_flow_month_amount(String bank_flow_month_amount) {
        this.bank_flow_month_amount = bank_flow_month_amount;
    }

    public String getMortgage_month_amount() {
        return mortgage_month_amount;
    }

    public void setMortgage_month_amount(String mortgage_month_amount) {
        this.mortgage_month_amount = mortgage_month_amount;
    }

    public String getMortgage_paid_num() {
        return mortgage_paid_num;
    }

    public void setMortgage_paid_num(String mortgage_paid_num) {
        this.mortgage_paid_num = mortgage_paid_num;
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
