package com.fjs.cronus.dto;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by chenjie on 2017/4/23.
 */
public class ContractDTO implements Serializable{

    private String contract_number;// varchar(25) DEFAULT '' COMMENT '合同标号字段',
    private Integer contract_id;// int(10) unsigned NOT NULL AUTO_INCREMENT,
    private Integer agreement_id;// int(10) NOT NULL DEFAULT '0' COMMENT '居间协议id',
    private String borrower;// varchar(200) DEFAULT NULL COMMENT '借款人',
    private String identity;// varchar(200) DEFAULT NULL COMMENT '借款人身份证',
    private String per_address;// varchar(255) DEFAULT NULL COMMENT '户籍地址',
    private String phone;// varchar(100) DEFAULT NULL COMMENT '联系电话',
    private String address;// varchar(255) DEFAULT NULL COMMENT '联系地址',
    private Integer contract_type;// tinyint(1) NOT NULL DEFAULT '1' COMMENT '合同类型 1:渠道 2:自营',
    private String product_type;// varchar(100) DEFAULT NULL COMMENT '产品类型(信用,赎楼,抵押)',
    private String product_name;// varchar(100) DEFAULT NULL COMMENT '产品名称,用,隔开',
    private String channel_name;// varchar(100) DEFAULT NULL COMMENT '出借人信息：渠道名称',
    private BigDecimal borrow_money;// decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '借款金额',
    private Integer duration;// int(10) NOT NULL DEFAULT '0' COMMENT '借款期限',
    private BigDecimal month_rate;// decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '月利率',
    private BigDecimal year_rate;// decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '年利率',
    private BigDecimal manger_money;// decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '管理费',
    private BigDecimal channel_money;// decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '返费支出总值',
    private BigDecimal packing;// decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '材料费总值',
    private BigDecimal return_fee;// decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '返费收入总值',
    private BigDecimal service_money;// decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '服务费总值',
    private Integer pay_type;// tinyint(1) NOT NULL DEFAULT '1' COMMENT '还款方式(1-等额本息,2-先息后本,3-等本等息,4其他)',
    private String house_address;// varchar(255) DEFAULT NULL COMMENT '抵押:房产地址',
    private BigDecimal house_age;// decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '抵押:户龄',
    private BigDecimal house_area;// decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '抵押:房产面积',
    private BigDecimal house_value;// decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '抵押:房产估值',
    private Integer create_user_id;// int(10) NOT NULL DEFAULT '0' COMMENT '创建人id',
    private Integer old_create_user_id;// int(10) NOT NULL DEFAULT '0' COMMENT '原始创建人',
    private Long create_time;// int(10) NOT NULL DEFAULT '0' COMMENT '创建时间',
    private Long updata_time;// int(10) NOT NULL DEFAULT '0' COMMENT '更新时间',
    private Long expire_time;// int(10) NOT NULL DEFAULT '0' COMMENT '到期时间',
    private Integer is_delete;// tinyint(1) NOT NULL DEFAULT '0' COMMENT '0:未删除  1:已删除',
    private Integer status;// tinyint(1) NOT NULL DEFAULT '0' COMMENT '合同状态(-1-无效 0-进行中,1-结案中,2-已结束)',
    private BigDecimal give_money;// decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '放款金额',
    private Long give_time;// int(10) NOT NULL DEFAULT '0' COMMENT '放款时间',
    private BigDecimal give_money_two;// decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '放款金额二',
    private Long give_time_two;// int(10) NOT NULL DEFAULT '0' COMMENT '放款时间二',
    private Integer check_process;// tinyint(1) NOT NULL DEFAULT '0' COMMENT '审核流程(0-'''',1-结案申请,2-团队长确认,3-分公司总经理确认,4-财务确认,)',
    private Integer check_status;// tinyint(1) NOT NULL DEFAULT '0' COMMENT '0:提交  1:驳回',
    private Integer check_num;// int(10) NOT NULL DEFAULT '1' COMMENT '审核轮数:渠道一轮,自营可多轮',
    private List<Object> partake;// text COMMENT '多个参与人',
    private String duration_unit;// varchar(255) NOT NULL DEFAULT '月' COMMENT '成本期限单位',
    private BigDecimal s_year_rate;// decimal(10,0) NOT NULL DEFAULT '0' COMMENT '收益年利率',
    private BigDecimal s_duration;// decimal(10,0) NOT NULL DEFAULT '0' COMMENT '收益期限',
    private String s_duration_unit;// varchar(255) NOT NULL DEFAULT '月' COMMENT '收益期限单位',
    private Integer is_special;// int(1) NOT NULL DEFAULT '0' COMMENT '0-不是利差合同 1-是利差字段',
    private String user_name;//业务员;
    private String process_status;//审核流程字段;
    private String pay_percent_status;//回款支出完成度
    private String percent_status;//回款收入完成度
    private String product_type_name;
    private String ext_value;//返回的模板内容;
    private Integer template_id;//模板id;
    private String partake_serialize;//分成;
    private String template_serialize;//确认书
    private String give_time_str;//放款时间字符串;
    private String expire_time_str;//到期时间字符串

    public String getGive_time_str() {
        return give_time_str;
    }

    public void setGive_time_str(String give_time_str) {
        this.give_time_str = give_time_str;
    }

    public String getExpire_time_str() {
        return expire_time_str;
    }

    public void setExpire_time_str(String expire_time_str) {
        this.expire_time_str = expire_time_str;
    }

    public String getTemplate_serialize() {
        return template_serialize;
    }

    public void setTemplate_serialize(String template_serialize) {
        this.template_serialize = template_serialize;
    }

    public String getPartake_serialize() {
        return partake_serialize;
    }

    public void setPartake_serialize(String partake_serialize) {
        this.partake_serialize = partake_serialize;
    }

    public Integer getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(Integer template_id) {
        this.template_id = template_id;
    }

    public String getExt_value() {
        return ext_value;
    }

    public void setExt_value(String ext_value) {
        this.ext_value = ext_value;
    }

    public String getProduct_type_name() {
        return product_type_name;
    }

    public void setProduct_type_name(String product_type_name) {
        this.product_type_name = product_type_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getProcess_status() {
        return process_status;
    }

    public void setProcess_status(String process_status) {
        this.process_status = process_status;
    }

    public String getPay_percent_status() {
        return pay_percent_status;
    }

    public void setPay_percent_status(String pay_percent_status) {
        this.pay_percent_status = pay_percent_status;
    }

    public String getPercent_status() {
        return percent_status;
    }

    public void setPercent_status(String percent_status) {
        this.percent_status = percent_status;
    }

    public String getContract_number() {
        return contract_number;
    }

    public void setContract_number(String contract_number) {
        this.contract_number = contract_number;
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

    public String getBorrower() {
        return borrower;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getPer_address() {
        return per_address;
    }

    public void setPer_address(String per_address) {
        this.per_address = per_address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getContract_type() {
        return contract_type;
    }

    public void setContract_type(Integer contract_type) {
        this.contract_type = contract_type;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    public BigDecimal getBorrow_money() {
        return borrow_money;
    }

    public void setBorrow_money(BigDecimal borrow_money) {
        this.borrow_money = borrow_money;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public BigDecimal getMonth_rate() {
        return month_rate;
    }

    public void setMonth_rate(BigDecimal month_rate) {
        this.month_rate = month_rate;
    }

    public BigDecimal getYear_rate() {
        return year_rate;
    }

    public void setYear_rate(BigDecimal year_rate) {
        this.year_rate = year_rate;
    }

    public BigDecimal getManger_money() {
        return manger_money;
    }

    public void setManger_money(BigDecimal manger_money) {
        this.manger_money = manger_money;
    }

    public BigDecimal getChannel_money() {
        return channel_money;
    }

    public void setChannel_money(BigDecimal channel_money) {
        this.channel_money = channel_money;
    }

    public BigDecimal getPacking() {
        return packing;
    }

    public void setPacking(BigDecimal packing) {
        this.packing = packing;
    }

    public BigDecimal getReturn_fee() {
        return return_fee;
    }

    public void setReturn_fee(BigDecimal return_fee) {
        this.return_fee = return_fee;
    }

    public BigDecimal getService_money() {
        return service_money;
    }

    public void setService_money(BigDecimal service_money) {
        this.service_money = service_money;
    }

    public Integer getPay_type() {
        return pay_type;
    }

    public void setPay_type(Integer pay_type) {
        this.pay_type = pay_type;
    }

    public String getHouse_address() {
        return house_address;
    }

    public void setHouse_address(String house_address) {
        this.house_address = house_address;
    }

    public BigDecimal getHouse_age() {
        return house_age;
    }

    public void setHouse_age(BigDecimal house_age) {
        this.house_age = house_age;
    }

    public BigDecimal getHouse_area() {
        return house_area;
    }

    public void setHouse_area(BigDecimal house_area) {
        this.house_area = house_area;
    }

    public BigDecimal getHouse_value() {
        return house_value;
    }

    public void setHouse_value(BigDecimal house_value) {
        this.house_value = house_value;
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

    public Long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Long create_time) {
        this.create_time = create_time;
    }

    public Long getUpdata_time() {
        return updata_time;
    }

    public void setUpdata_time(Long updata_time) {
        this.updata_time = updata_time;
    }

    public Long getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(Long expire_time) {
        this.expire_time = expire_time;
    }

    public Integer getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(Integer is_delete) {
        this.is_delete = is_delete;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BigDecimal getGive_money() {
        return give_money;
    }

    public void setGive_money(BigDecimal give_money) {
        this.give_money = give_money;
    }

    public Long getGive_time() {
        return give_time;
    }

    public void setGive_time(Long give_time) {
        this.give_time = give_time;
    }

    public BigDecimal getGive_money_two() {
        return give_money_two;
    }

    public void setGive_money_two(BigDecimal give_money_two) {
        this.give_money_two = give_money_two;
    }

    public Long getGive_time_two() {
        return give_time_two;
    }

    public void setGive_time_two(Long give_time_two) {
        this.give_time_two = give_time_two;
    }

    public Integer getCheck_process() {
        return check_process;
    }

    public void setCheck_process(Integer check_process) {
        this.check_process = check_process;
    }

    public Integer getCheck_status() {
        return check_status;
    }

    public void setCheck_status(Integer check_status) {
        this.check_status = check_status;
    }

    public Integer getCheck_num() {
        return check_num;
    }

    public void setCheck_num(Integer check_num) {
        this.check_num = check_num;
    }

    public List<Object> getPartake() {
        return partake;
    }

    public void setPartake(List<Object> partake) {
        this.partake = partake;
    }

    public String getDuration_unit() {
        return duration_unit;
    }

    public void setDuration_unit(String duration_unit) {
        this.duration_unit = duration_unit;
    }

    public BigDecimal getS_year_rate() {
        return s_year_rate;
    }

    public void setS_year_rate(BigDecimal s_year_rate) {
        this.s_year_rate = s_year_rate;
    }

    public BigDecimal getS_duration() {
        return s_duration;
    }

    public void setS_duration(BigDecimal s_duration) {
        this.s_duration = s_duration;
    }

    public String getS_duration_unit() {
        return s_duration_unit;
    }

    public void setS_duration_unit(String s_duration_unit) {
        this.s_duration_unit = s_duration_unit;
    }

    public Integer getIs_special() {
        return is_special;
    }

    public void setIs_special(Integer is_special) {
        this.is_special = is_special;
    }

}
