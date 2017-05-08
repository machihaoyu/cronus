package com.fjs.cronus.dto;

import java.io.Serializable;

public class CustomerSaleDTO implements Serializable{

    private static final long serialVersionUID = 6841315540181335020L;

    private int customer_id; //int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '唯一值',
    private String telephonenumber; // varchar(100) NOT NULL COMMENT '手机号码'
    private String customer_name; // varchar(100) NOT NULL COMMENT '客户姓名'
    private int owner_user_id; // int(10) unsigned NOT NULL DEFAULT '0' COMMENT '当前负责人',
    private String owner_user_name; // varchar(50) DEFAULT NULL COMMENT '拥有人',
    private int creater_user_id; // int(10) NOT NULL DEFAULT '0' COMMENT '创建人',
    private String customer_level;// varchar(100) DEFAULT '意向客户' COMMENT '客户等级',
    private long loan_amount;// bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '意向需求资金1',
    private String spare_phone;// varchar(100) DEFAULT NULL COMMENT '备用联系方式',
    private String age;// varchar(100) DEFAULT NULL COMMENT '年龄',
    private String marriage;// enum('已婚','未婚','离异') DEFAULT NULL COMMENT '婚姻',
    private String id_card;// varchar(20) DEFAULT NULL COMMENT '身份证号码',
    private String province_huji;// varchar(100) DEFAULT NULL COMMENT '户籍',
    private String sex;// enum('男','女','') NOT NULL DEFAULT '男' COMMENT '性别',
    private String customer_address;// varchar(250) DEFAULT NULL COMMENT '客户地址',
    private String province; //客户地址-省
    private String city; //客户地址-市
    private String area; //客户地址-区域
    private String street; //客户地址-地址
    private String provinces;  //房产地址-省
    private String citys; //房产地址-市
    private String areas; //房产地址-区域
    private String streets; //房产地址-地址
    private String per_description;// text COMMENT '信息备注',
    private String house_status;// enum('','无','有') NOT NULL DEFAULT '' COMMENT '有无房产',
    private String house_amount;// varchar(100) DEFAULT NULL COMMENT '几套房',
    private String house_type;// varchar(100) DEFAULT NULL COMMENT '房产类型',
    private String house_value;// varchar(50) DEFAULT NULL COMMENT '房产估值',
    private String house_area;// varchar(50) DEFAULT NULL COMMENT '房产面积',
    private String house_age;// varchar(100) DEFAULT NULL COMMENT '房龄',
    private String house_loan;// enum('是','否') NOT NULL DEFAULT '否' COMMENT '是否按揭',
    private String house_alone;// enum('是','否') NOT NULL DEFAULT '否' COMMENT '是否备用房',
    private String house_location;// varchar(250) DEFAULT NULL COMMENT '房产地址',
    private int retain;// tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否保留  0不保留1保留2已签合同',
    private long create_time;// int(10) NOT NULL DEFAULT '0' COMMENT '创建时间',
    private long update_time;// int(10) NOT NULL DEFAULT '0' COMMENT '更新时间',
    private long receive_time;// int(10) NOT NULL DEFAULT '0' COMMENT '领取时间',
    private int is_lock;// tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否锁定',
    private long phone_view_time;// int(10) unsigned NOT NULL DEFAULT '0' COMMENT '被查看的最后时间',
    private int phone_view_uid;// int(10) unsigned NOT NULL DEFAULT '0' COMMENT '最后查看的人',
    private long phone_view_count;// int(3) unsigned NOT NULL DEFAULT '0' COMMENT '被查看的次数',
    private int autostatus;// tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否自动分配：1自动分配，0没有自动分配',
    private String customer_source;// varchar(255) DEFAULT NULL COMMENT '客户来源',
    private String utm_source;// varchar(255) DEFAULT NULL COMMENT '渠道',
    private String customer_classify;// varchar(100) NOT NULL DEFAULT '正常' COMMENT '体现客户的状态',
    private String customer_type;// varchar(100) NOT NULL DEFAULT '意向客户' COMMENT '客户类型(意向客户/协议客户/成交客户)',
    private long communitime;// int(10) NOT NULL DEFAULT '0' COMMENT '沟通时间',
    private String callback_status;// char(10) DEFAULT '正常' COMMENT '回访状态',
    private long callback_time;// int(11) NOT NULL DEFAULT '0' COMMENT '回访时间',
    private long first_communitime_time;// int(10) NOT NULL DEFAULT '0' COMMENT '首次沟通时间',
    private long first_fenpei_time;// int(10) NOT NULL DEFAULT '0' COMMENT '首次分配时间',
    private String cooperation_status;// varchar(100) DEFAULT NULL COMMENT '合作状态(暂未接通 无意向 有意向待跟踪 资质差无法操作 其他)',
    private String ext;// text,
    private String has_agreement; //
    private String useful;//":0

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getTelephonenumber() {
        return telephonenumber;
    }

    public void setTelephonenumber(String telephonenumber) {
        this.telephonenumber = telephonenumber;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public int getOwner_user_id() {
        return owner_user_id;
    }

    public void setOwner_user_id(int owner_user_id) {
        this.owner_user_id = owner_user_id;
    }

    public String getOwner_user_name() {
        return owner_user_name;
    }

    public void setOwner_user_name(String owner_user_name) {
        this.owner_user_name = owner_user_name;
    }

    public int getCreater_user_id() {
        return creater_user_id;
    }

    public void setCreater_user_id(int creater_user_id) {
        this.creater_user_id = creater_user_id;
    }

    public String getCustomer_level() {
        return customer_level;
    }

    public void setCustomer_level(String customer_level) {
        this.customer_level = customer_level;
    }

    public long getLoan_amount() {
        return loan_amount;
    }

    public void setLoan_amount(long loan_amount) {
        this.loan_amount = loan_amount;
    }

    public String getSpare_phone() {
        return spare_phone;
    }

    public void setSpare_phone(String spare_phone) {
        this.spare_phone = spare_phone;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getMarriage() {
        return marriage;
    }

    public void setMarriage(String marriage) {
        this.marriage = marriage;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    public String getProvince_huji() {
        return province_huji;
    }

    public void setProvince_huji(String province_huji) {
        this.province_huji = province_huji;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCustomer_address() {
        return customer_address;
    }

    public void setCustomer_address(String customer_address) {
        this.customer_address = customer_address;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getProvinces() {
        return provinces;
    }

    public void setProvinces(String provinces) {
        this.provinces = provinces;
    }

    public String getCitys() {
        return citys;
    }

    public void setCitys(String citys) {
        this.citys = citys;
    }

    public String getAreas() {
        return areas;
    }

    public void setAreas(String areas) {
        this.areas = areas;
    }

    public String getStreets() {
        return streets;
    }

    public void setStreets(String streets) {
        this.streets = streets;
    }

    public String getPer_description() {
        return per_description;
    }

    public void setPer_description(String per_description) {
        this.per_description = per_description;
    }

    public String getHouse_status() {
        return house_status;
    }

    public void setHouse_status(String house_status) {
        this.house_status = house_status;
    }

    public String getHouse_amount() {
        return house_amount;
    }

    public void setHouse_amount(String house_amount) {
        this.house_amount = house_amount;
    }

    public String getHouse_type() {
        return house_type;
    }

    public void setHouse_type(String house_type) {
        this.house_type = house_type;
    }

    public String getHouse_value() {
        return house_value;
    }

    public void setHouse_value(String house_value) {
        this.house_value = house_value;
    }

    public String getHouse_area() {
        return house_area;
    }

    public void setHouse_area(String house_area) {
        this.house_area = house_area;
    }

    public String getHouse_age() {
        return house_age;
    }

    public void setHouse_age(String house_age) {
        this.house_age = house_age;
    }

    public String getHouse_loan() {
        return house_loan;
    }

    public void setHouse_loan(String house_loan) {
        this.house_loan = house_loan;
    }

    public String getHouse_alone() {
        return house_alone;
    }

    public void setHouse_alone(String house_alone) {
        this.house_alone = house_alone;
    }

    public String getHouse_location() {
        return house_location;
    }

    public void setHouse_location(String house_location) {
        this.house_location = house_location;
    }

    public int getRetain() {
        return retain;
    }

    public void setRetain(int retain) {
        this.retain = retain;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(long update_time) {
        this.update_time = update_time;
    }

    public long getReceive_time() {
        return receive_time;
    }

    public void setReceive_time(long receive_time) {
        this.receive_time = receive_time;
    }

    public int getIs_lock() {
        return is_lock;
    }

    public void setIs_lock(int is_lock) {
        this.is_lock = is_lock;
    }

    public long getPhone_view_time() {
        return phone_view_time;
    }

    public void setPhone_view_time(long phone_view_time) {
        this.phone_view_time = phone_view_time;
    }

    public int getPhone_view_uid() {
        return phone_view_uid;
    }

    public void setPhone_view_uid(int phone_view_uid) {
        this.phone_view_uid = phone_view_uid;
    }

    public long getPhone_view_count() {
        return phone_view_count;
    }

    public void setPhone_view_count(long phone_view_count) {
        this.phone_view_count = phone_view_count;
    }

    public int getAutostatus() {
        return autostatus;
    }

    public void setAutostatus(int autostatus) {
        this.autostatus = autostatus;
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

    public String getCustomer_classify() {
        return customer_classify;
    }

    public void setCustomer_classify(String customer_classify) {
        this.customer_classify = customer_classify;
    }

    public String getCustomer_type() {
        return customer_type;
    }

    public void setCustomer_type(String customer_type) {
        this.customer_type = customer_type;
    }

    public long getCommunitime() {
        return communitime;
    }

    public void setCommunitime(long communitime) {
        this.communitime = communitime;
    }

    public String getCallback_status() {
        return callback_status;
    }

    public void setCallback_status(String callback_status) {
        this.callback_status = callback_status;
    }

    public long getCallback_time() {
        return callback_time;
    }

    public void setCallback_time(long callback_time) {
        this.callback_time = callback_time;
    }

    public long getFirst_communitime_time() {
        return first_communitime_time;
    }

    public void setFirst_communitime_time(long first_communitime_time) {
        this.first_communitime_time = first_communitime_time;
    }

    public long getFirst_fenpei_time() {
        return first_fenpei_time;
    }

    public void setFirst_fenpei_time(long first_fenpei_time) {
        this.first_fenpei_time = first_fenpei_time;
    }

    public String getCooperation_status() {
        return cooperation_status;
    }

    public void setCooperation_status(String cooperation_status) {
        this.cooperation_status = cooperation_status;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getHas_agreement() {
        return has_agreement;
    }

    public void setHas_agreement(String has_agreement) {
        this.has_agreement = has_agreement;
    }

    public String getUseful() {
        return useful;
    }

    public void setUseful(String useful) {
        this.useful = useful;
    }
}
