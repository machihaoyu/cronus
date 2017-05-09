package com.fjs.cronus.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by chenjie on 2017/5/8.
 */
public class ProductDTO implements Serializable{

    private static final long serialVersionUID = 5832778481148221408L;

    private Integer product_id;// int(10) unsigned NOT NULL AUTO_INCREMENT,
    private Integer sub_company_id;// int(10) NOT NULL DEFAULT '0' COMMENT '分公司id',
    private String agency_name;// varchar(100) DEFAULT NULL COMMENT '机构名',
    private Integer agency_type;// tinyint(1) DEFAULT NULL COMMENT '资金渠道类别1银行2小贷3其他',
    private Integer ispublic;// tinyint(1) DEFAULT NULL COMMENT '1对公2对私',
    private Integer ismakeagreement;// tinyint(1) DEFAULT NULL COMMENT '是否签订协议1已签订2未签订',
    private String product_name;// varchar(255) NOT NULL COMMENT '产品名',
    private BigDecimal rate_min;// decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '利率',
    private BigDecimal rate_max;// decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '利率',
    private BigDecimal mortgage_rate;// decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '抵押率',
    private Integer has_commission;// tinyint(1) NOT NULL DEFAULT '0' COMMENT '有无返佣1有0无',
    private String loan_time;// varchar(128) NOT NULL DEFAULT '' COMMENT '放款时间1 3天2七天3收件4',
    private String risk;// varchar(128) NOT NULL DEFAULT '' COMMENT '风险控制 1高2中3低',
    private Integer has_question;// tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否有问答',
    private Integer product_nature;// tinyint(1) DEFAULT NULL COMMENT '产品性质1信用2抵押',
    private String agency_owner;// varchar(200) DEFAULT NULL COMMENT '对接人',
    private String agency_phone;// varchar(100) DEFAULT NULL COMMENT '对接人电话',
    private String agency_address;// varchar(256) DEFAULT NULL COMMENT '对方联系人地址',
    private String develop_owner;// varchar(200) DEFAULT NULL COMMENT '开发人',
    private String develop_phone;// varchar(100) DEFAULT NULL COMMENT '开发人电话',
    private Integer recommend_isfjs;// tinyint(100) DEFAULT NULL COMMENT '是否为公司员工',
    private String recommend_owner;// varchar(100) DEFAULT NULL COMMENT '介绍人',
    private String recommend_phone;// varchar(100) DEFAULT NULL COMMENT '介绍人联系电话',
    private String transact_owner;// varchar(100) DEFAULT NULL COMMENT '谈判人',
    private String transact_phone;// varchar(100) DEFAULT NULL COMMENT '谈判人联系方式',
    private String owner;// varchar(100) DEFAULT NULL COMMENT '维护人',
    private String phone;// varchar(100) DEFAULT NULL COMMENT '维护人电话',
    private String type1;// enum('信贷','抵押') DEFAULT NULL COMMENT '产品类型1',
    private String type2;// enum('消费贷','经营贷') DEFAULT NULL COMMENT '产品类型2',
    private String type3;// enum('个人','企业') DEFAULT NULL COMMENT '产品类型3',
    private String description;// text COMMENT '产品描述',
    private Integer create_role_id;// int(10) NOT NULL DEFAULT '0' COMMENT '创建人',
    private Long create_time;// int(10) NOT NULL DEFAULT '0' COMMENT '创建时间',
    private Long update_time;// int(10) NOT NULL DEFAULT '0' COMMENT '更新时间',
    private Long cooperate_time;// int(11) DEFAULT '0' COMMENT '合作时间',
    private Integer status;// int(11) DEFAULT '1' COMMENT '状态:1-正常   0-终止',
    private String sub_company;

    public String getSub_company() {
        return sub_company;
    }

    public void setSub_company(String sub_company) {
        this.sub_company = sub_company;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

    public Integer getSub_company_id() {
        return sub_company_id;
    }

    public void setSub_company_id(Integer sub_company_id) {
        this.sub_company_id = sub_company_id;
    }

    public String getAgency_name() {
        return agency_name;
    }

    public void setAgency_name(String agency_name) {
        this.agency_name = agency_name;
    }

    public Integer getAgency_type() {
        return agency_type;
    }

    public void setAgency_type(Integer agency_type) {
        this.agency_type = agency_type;
    }

    public Integer getIspublic() {
        return ispublic;
    }

    public void setIspublic(Integer ispublic) {
        this.ispublic = ispublic;
    }

    public Integer getIsmakeagreement() {
        return ismakeagreement;
    }

    public void setIsmakeagreement(Integer ismakeagreement) {
        this.ismakeagreement = ismakeagreement;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public BigDecimal getRate_min() {
        return rate_min;
    }

    public void setRate_min(BigDecimal rate_min) {
        this.rate_min = rate_min;
    }

    public BigDecimal getRate_max() {
        return rate_max;
    }

    public void setRate_max(BigDecimal rate_max) {
        this.rate_max = rate_max;
    }

    public BigDecimal getMortgage_rate() {
        return mortgage_rate;
    }

    public void setMortgage_rate(BigDecimal mortgage_rate) {
        this.mortgage_rate = mortgage_rate;
    }

    public Integer getHas_commission() {
        return has_commission;
    }

    public void setHas_commission(Integer has_commission) {
        this.has_commission = has_commission;
    }

    public String getLoan_time() {
        return loan_time;
    }

    public void setLoan_time(String loan_time) {
        this.loan_time = loan_time;
    }

    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }

    public Integer getHas_question() {
        return has_question;
    }

    public void setHas_question(Integer has_question) {
        this.has_question = has_question;
    }

    public Integer getProduct_nature() {
        return product_nature;
    }

    public void setProduct_nature(Integer product_nature) {
        this.product_nature = product_nature;
    }

    public String getAgency_owner() {
        return agency_owner;
    }

    public void setAgency_owner(String agency_owner) {
        this.agency_owner = agency_owner;
    }

    public String getAgency_phone() {
        return agency_phone;
    }

    public void setAgency_phone(String agency_phone) {
        this.agency_phone = agency_phone;
    }

    public String getAgency_address() {
        return agency_address;
    }

    public void setAgency_address(String agency_address) {
        this.agency_address = agency_address;
    }

    public String getDevelop_owner() {
        return develop_owner;
    }

    public void setDevelop_owner(String develop_owner) {
        this.develop_owner = develop_owner;
    }

    public String getDevelop_phone() {
        return develop_phone;
    }

    public void setDevelop_phone(String develop_phone) {
        this.develop_phone = develop_phone;
    }

    public Integer getRecommend_isfjs() {
        return recommend_isfjs;
    }

    public void setRecommend_isfjs(Integer recommend_isfjs) {
        this.recommend_isfjs = recommend_isfjs;
    }

    public String getRecommend_owner() {
        return recommend_owner;
    }

    public void setRecommend_owner(String recommend_owner) {
        this.recommend_owner = recommend_owner;
    }

    public String getRecommend_phone() {
        return recommend_phone;
    }

    public void setRecommend_phone(String recommend_phone) {
        this.recommend_phone = recommend_phone;
    }

    public String getTransact_owner() {
        return transact_owner;
    }

    public void setTransact_owner(String transact_owner) {
        this.transact_owner = transact_owner;
    }

    public String getTransact_phone() {
        return transact_phone;
    }

    public void setTransact_phone(String transact_phone) {
        this.transact_phone = transact_phone;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public String getType3() {
        return type3;
    }

    public void setType3(String type3) {
        this.type3 = type3;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCreate_role_id() {
        return create_role_id;
    }

    public void setCreate_role_id(Integer create_role_id) {
        this.create_role_id = create_role_id;
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

    public Long getCooperate_time() {
        return cooperate_time;
    }

    public void setCooperate_time(Long cooperate_time) {
        this.cooperate_time = cooperate_time;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}