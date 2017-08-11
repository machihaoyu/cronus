package com.fjs.cronus.dto.php;




import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class CustomerInterviewInfoDTO implements Serializable{

    private static final long serialVersionUID = 6841315540181335020L;
    private Integer customer_interview_base_info_id;//`customer_interview_base_info_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',

    @ApiModelProperty(value = "客户ID" ,required = true)
    private Integer customer_id;// `customer_id` int(11) DEFAULT NULL COMMENT '客户ID',
    private Integer owner_user_id;//`owner_user_id` int(11) DEFAULT NULL COMMENT '当前负责人ID',
    private String owner_user_name;// `owner_user_name` varchar(255) DEFAULT NULL COMMENT '当前负责人姓名',
    private String name;//`name` varchar(50) DEFAULT NULL COMMENT '姓名',
    private String sex;//`sex` varchar(10) DEFAULT NULL COMMENT '性别',
    private String birth_date;//`birth_date` int(10) DEFAULT NULL COMMENT '出生日期',
    private String age;//`age` int(10) DEFAULT NULL COMMENT '年龄',
    private String telephonenumber;//`telephonenumber` varchar(50) DEFAULT NULL COMMENT '手机号码',
    private String marital_status;//`marital_status` varchar(50) DEFAULT NULL COMMENT '婚姻状况',
    private String household_register;//`household_register` varchar(50) DEFAULT NULL COMMENT '户籍地址',
    private String education;//`education` varchar(50) DEFAULT NULL COMMENT '学历',
    private String fee_channel_name;//`fee_channel_name` varchar(50) DEFAULT NULL COMMENT '资金渠道名',
    private String product_name;//`product_name` varchar(50) DEFAULT NULL COMMENT '产品类型名',
    private Integer loan_amount;//loan_amount` varchar(50) DEFAULT NULL COMMENT '借款金额',
    private Integer loan_time;//loan_time` int(11) DEFAULT NULL COMMENT '借款期限（月）',
    private Integer loan_use_time;//`loan_use_time` varchar(50) DEFAULT NULL COMMENT '借款使用时间',
    private String loan_purpose;//`loan_purpose` varchar(50) DEFAULT NULL COMMENT '借款用途',
    private String payment_type;//`payment_type` varchar(50) DEFAULT NULL COMMENT '还款方式',
    private String credit_record;//`credit_record` varchar(50) DEFAULT NULL COMMENT '征信记录',
    private Integer zhima_credit;//`zhima_credit` int(10) DEFAULT NULL COMMENT '芝麻信用积分',
    private Integer credit_query_num_two_month;//`credit_query_num_two_month` int(10) DEFAULT NULL COMMENT '征信近两个月查询次数',
    private Integer credit_query_num_six_month;//`credit_query_num_six_month` int(10) DEFAULT NULL COMMENT '征信近半年查询次数',
    private Integer continuity_overdue_num_two_year;//`continuity_overdue_num_two_year` int(10) DEFAULT NULL COMMENT '两年内连续逾期月数',
    private Integer total_overdue_num_two_year;//`total_overdue_num_two_year` int(10) DEFAULT NULL COMMENT '两年内累计逾期月数',
    private Integer debt_amount;//`debt_amount` int(10) DEFAULT NULL COMMENT '负债金额',
    private String is_overdue;//`is_overdue` varchar(50) DEFAULT NULL COMMENT '当前是否有逾期',
    private String overdue_amount;//`overdue_amount` int(10) DEFAULT NULL COMMENT '当前逾期金额',
    private String industry;//`industry` varchar(50) DEFAULT NULL COMMENT '行业',
    private Integer income_amount;//`income_amount` int(10) DEFAULT NULL COMMENT '打卡工资',
    private Integer social_security_date;//`social_security_date` int(10) DEFAULT NULL COMMENT '社保缴纳时间',
    private Integer social_security_payment;//`social_security_payment` int(10) DEFAULT NULL COMMENT '社保缴纳基数',
    private Integer housing_fund_date;//`housing_fund_date` int(10) DEFAULT NULL COMMENT '公积金缴纳时间',
    private Integer housing_fund_payment;//`housing_fund_payment` int(10) DEFAULT NULL COMMENT '公积金缴纳基数',
    private Integer work_date;//`work_date` int(10) DEFAULT NULL COMMENT '工作年限',
    private Integer company_register_date;//`company_register_date` int(10) DEFAULT NULL COMMENT '公司注册年限',
    private Float share_rate;//`share_rate` decimal(10,2) DEFAULT NULL COMMENT '占股比例',
    private Integer public_flow_year_amount;//`public_flow_year_amount` int(10) DEFAULT NULL COMMENT '年对公流水',
    private Integer private_flow_year_amount;//`private_flow_year_amount` int(10) DEFAULT NULL COMMENT '年对私流水',
    private String is_litigation;//`is_litigation` varchar(50) DEFAULT NULL COMMENT '有无涉诉',
    private Integer retire_date;//`retire_date` int(10) DEFAULT NULL COMMENT '退休日期',
    private Integer retirement_pay_min_amount;//`retirement_pay_min_amount` int(10) DEFAULT NULL COMMENT '退休最少工资',
    private String is_relative_known;//`is_relative_known` varchar(50) DEFAULT NULL COMMENT '直系亲属是否知道',
    private String remark;// 备注信息

    private Integer customer_interview_house_info_id;//`customer_interview_insurance_info_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    private String house_status;//`house_status` varchar(50) DEFAULT NULL COMMENT '产权状态',
    private String house_property_type;//`house_property_type` varchar(50) DEFAULT NULL COMMENT '产权类型',
    private Float area;//`area` decimal(10,2) DEFAULT NULL COMMENT '面积',
    private Integer build_date;//`build_date` int(10) DEFAULT NULL COMMENT '建成年限',
    private Integer house_property_rights_num;//`house_property_rights_num` int(11) DEFAULT NULL COMMENT '产权人数',
    private String is_child_in_property_rigths;//`is_child_in_property_rigths` varchar(50) DEFAULT NULL COMMENT '产权中是否有小孩',
    private String is_old_in_property_rigths;//`is_old_in_property_rigths` varchar(50) DEFAULT NULL COMMENT '产权中是否有老人',
    private String is_property_rights_clear;//`is_property_rights_clear` varchar(50) DEFAULT NULL COMMENT '房产权属是否清晰',
    private String is_other_house;//`is_other_house` varchar(50) DEFAULT NULL COMMENT '有无备用房',
    private String is_bank_flow;//`is_bank_flow` varchar(50) DEFAULT NULL COMMENT '是否有银行流水',
    private Integer bank_flow_month_amount;//`bank_flow_month_amount` int(10) DEFAULT NULL COMMENT '月流水金额',
    private Integer house_mortgage_month_amount;//`mortgage_month_amount` int(50) DEFAULT NULL COMMENT '按揭月供金额',
    private Integer house_mortgage_paid_num;//`mortgage_paid_num` int(10) DEFAULT NULL COMMENT '按揭已还月份',

    private Integer customer_interview_car_info_id;//`customer_interview_car_info_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    private String car_type;//`car_type` varchar(50) DEFAULT NULL COMMENT '车辆类型',
    private String licence_plate_location;//`licence_plate_location` varchar(255) DEFAULT NULL COMMENT '车牌地区',
    private Integer buy_date;//`buy_date` int(10) DEFAULT NULL COMMENT '购买时间（月）',
    private Integer car_mortgage_paid_num;//`mortgage_paid_num` int(10) DEFAULT NULL COMMENT '按揭月供已还月数',
    private Integer car_mortgage_month_amount;//`mortgage_month_amount` int(10) DEFAULT NULL COMMENT '按揭月供金额',
    private Integer price_now;//`price_now` decimal(10,2) DEFAULT NULL COMMENT '现车价',
    private String is_full_insurance;//`is_full_insurance` varchar(50) DEFAULT NULL COMMENT '是否两险齐全',

    private Integer customer_interview_insurance_info_id;//`customer_interview_insurance_info_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    private String insurance_type;//`insurance_type` varchar(50) DEFAULT NULL COMMENT '保险类型',
    private String pay_type;//`pay_type` varchar(50) DEFAULT NULL COMMENT '缴费方式',
    private Integer year_pay_amount;//`year_pay_amount` int(10) DEFAULT NULL COMMENT '年缴费金额',
    private Integer month_pay_amount;//`month_pay_amount` int(10) DEFAULT NULL COMMENT '月缴费金额',
    private Integer effect_date;//`effect_date` int(10) DEFAULT NULL COMMENT '生效时间',
    private String is_suspend;//`is_suspend` varchar(50) DEFAULT NULL COMMENT '有无断缴',
    private String create_time;//`create_time` int(10) DEFAULT NULL COMMENT '创建时间',
    private Integer create_user_id;//`create_user_id` int(11) DEFAULT NULL COMMENT '创建人ID',
    private String update_time;//`update_time` int(10) DEFAULT NULL COMMENT '更新时间',

    public Integer getCustomer_interview_base_info_id() {
        return customer_interview_base_info_id;
    }

    public void setCustomer_interview_base_info_id(Integer customer_interview_base_info_id) {
        this.customer_interview_base_info_id = customer_interview_base_info_id;
    }

    public Integer getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(Integer customer_id) {
        this.customer_id = customer_id;
    }

    public Integer getOwner_user_id() {
        return owner_user_id;
    }

    public void setOwner_user_id(Integer owner_user_id) {
        this.owner_user_id = owner_user_id;
    }

    public String getOwner_user_name() {
        return owner_user_name;
    }

    public void setOwner_user_name(String owner_user_name) {
        this.owner_user_name = owner_user_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getTelephonenumber() {
        return telephonenumber;
    }

    public void setTelephonenumber(String telephonenumber) {
        this.telephonenumber = telephonenumber;
    }

    public String getMarital_status() {
        return marital_status;
    }

    public void setMarital_status(String marital_status) {
        this.marital_status = marital_status;
    }

    public String getHousehold_register() {
        return household_register;
    }

    public void setHousehold_register(String household_register) {
        this.household_register = household_register;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getFee_channel_name() {
        return fee_channel_name;
    }

    public void setFee_channel_name(String fee_channel_name) {
        this.fee_channel_name = fee_channel_name;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public Integer getLoan_amount() {
        return loan_amount;
    }

    public void setLoan_amount(Integer loan_amount) {
        this.loan_amount = loan_amount;
    }

    public Integer getLoan_time() {
        return loan_time;
    }

    public void setLoan_time(Integer loan_time) {
        this.loan_time = loan_time;
    }

    public Integer getLoan_use_time() {
        return loan_use_time;
    }

    public void setLoan_use_time(Integer loan_use_time) {
        this.loan_use_time = loan_use_time;
    }

    public String getLoan_purpose() {
        return loan_purpose;
    }

    public void setLoan_purpose(String loan_purpose) {
        this.loan_purpose = loan_purpose;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getCredit_record() {
        return credit_record;
    }

    public void setCredit_record(String credit_record) {
        this.credit_record = credit_record;
    }

    public Integer getZhima_credit() {
        return zhima_credit;
    }

    public void setZhima_credit(Integer zhima_credit) {
        this.zhima_credit = zhima_credit;
    }

    public Integer getCredit_query_num_two_month() {
        return credit_query_num_two_month;
    }

    public void setCredit_query_num_two_month(Integer credit_query_num_two_month) {
        this.credit_query_num_two_month = credit_query_num_two_month;
    }

    public Integer getCredit_query_num_six_month() {
        return credit_query_num_six_month;
    }

    public void setCredit_query_num_six_month(Integer credit_query_num_six_month) {
        this.credit_query_num_six_month = credit_query_num_six_month;
    }

    public Integer getContinuity_overdue_num_two_year() {
        return continuity_overdue_num_two_year;
    }

    public void setContinuity_overdue_num_two_year(Integer continuity_overdue_num_two_year) {
        this.continuity_overdue_num_two_year = continuity_overdue_num_two_year;
    }

    public Integer getTotal_overdue_num_two_year() {
        return total_overdue_num_two_year;
    }

    public void setTotal_overdue_num_two_year(Integer total_overdue_num_two_year) {
        this.total_overdue_num_two_year = total_overdue_num_two_year;
    }

    public Integer getDebt_amount() {
        return debt_amount;
    }

    public void setDebt_amount(Integer debt_amount) {
        this.debt_amount = debt_amount;
    }

    public String getIs_overdue() {
        return is_overdue;
    }

    public void setIs_overdue(String is_overdue) {
        this.is_overdue = is_overdue;
    }

    public String getOverdue_amount() {
        return overdue_amount;
    }

    public void setOverdue_amount(String overdue_amount) {
        this.overdue_amount = overdue_amount;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public Integer getIncome_amount() {
        return income_amount;
    }

    public void setIncome_amount(Integer income_amount) {
        this.income_amount = income_amount;
    }

    public Integer getSocial_security_date() {
        return social_security_date;
    }

    public void setSocial_security_date(Integer social_security_date) {
        this.social_security_date = social_security_date;
    }

    public Integer getSocial_security_payment() {
        return social_security_payment;
    }

    public void setSocial_security_payment(Integer social_security_payment) {
        this.social_security_payment = social_security_payment;
    }

    public Integer getHousing_fund_date() {
        return housing_fund_date;
    }

    public void setHousing_fund_date(Integer housing_fund_date) {
        this.housing_fund_date = housing_fund_date;
    }

    public Integer getHousing_fund_payment() {
        return housing_fund_payment;
    }

    public void setHousing_fund_payment(Integer housing_fund_payment) {
        this.housing_fund_payment = housing_fund_payment;
    }

    public Integer getWork_date() {
        return work_date;
    }

    public void setWork_date(Integer work_date) {
        this.work_date = work_date;
    }

    public Integer getCompany_register_date() {
        return company_register_date;
    }

    public void setCompany_register_date(Integer company_register_date) {
        this.company_register_date = company_register_date;
    }

    public Float getShare_rate() {
        return share_rate;
    }

    public void setShare_rate(Float share_rate) {
        this.share_rate = share_rate;
    }

    public Integer getPublic_flow_year_amount() {
        return public_flow_year_amount;
    }

    public void setPublic_flow_year_amount(Integer public_flow_year_amount) {
        this.public_flow_year_amount = public_flow_year_amount;
    }

    public Integer getPrivate_flow_year_amount() {
        return private_flow_year_amount;
    }

    public void setPrivate_flow_year_amount(Integer private_flow_year_amount) {
        this.private_flow_year_amount = private_flow_year_amount;
    }

    public String getIs_litigation() {
        return is_litigation;
    }

    public void setIs_litigation(String is_litigation) {
        this.is_litigation = is_litigation;
    }

    public Integer getRetire_date() {
        return retire_date;
    }

    public void setRetire_date(Integer retire_date) {
        this.retire_date = retire_date;
    }

    public Integer getRetirement_pay_min_amount() {
        return retirement_pay_min_amount;
    }

    public void setRetirement_pay_min_amount(Integer retirement_pay_min_amount) {
        this.retirement_pay_min_amount = retirement_pay_min_amount;
    }

    public String getIs_relative_known() {
        return is_relative_known;
    }

    public void setIs_relative_known(String is_relative_known) {
        this.is_relative_known = is_relative_known;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getCustomer_interview_house_info_id() {
        return customer_interview_house_info_id;
    }

    public void setCustomer_interview_house_info_id(Integer customer_interview_house_info_id) {
        this.customer_interview_house_info_id = customer_interview_house_info_id;
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

    public Float getArea() {
        return area;
    }

    public void setArea(Float area) {
        this.area = area;
    }

    public Integer getBuild_date() {
        return build_date;
    }

    public void setBuild_date(Integer build_date) {
        this.build_date = build_date;
    }

    public Integer getHouse_property_rights_num() {
        return house_property_rights_num;
    }

    public void setHouse_property_rights_num(Integer house_property_rights_num) {
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

    public Integer getBank_flow_month_amount() {
        return bank_flow_month_amount;
    }

    public void setBank_flow_month_amount(Integer bank_flow_month_amount) {
        this.bank_flow_month_amount = bank_flow_month_amount;
    }

    public Integer getHouse_mortgage_month_amount() {
        return house_mortgage_month_amount;
    }

    public void setHouse_mortgage_month_amount(Integer house_mortgage_month_amount) {
        this.house_mortgage_month_amount = house_mortgage_month_amount;
    }

    public Integer getHouse_mortgage_paid_num() {
        return house_mortgage_paid_num;
    }

    public void setHouse_mortgage_paid_num(Integer house_mortgage_paid_num) {
        this.house_mortgage_paid_num = house_mortgage_paid_num;
    }

    public Integer getCustomer_interview_car_info_id() {
        return customer_interview_car_info_id;
    }

    public void setCustomer_interview_car_info_id(Integer customer_interview_car_info_id) {
        this.customer_interview_car_info_id = customer_interview_car_info_id;
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

    public Integer getBuy_date() {
        return buy_date;
    }

    public void setBuy_date(Integer buy_date) {
        this.buy_date = buy_date;
    }

    public Integer getCar_mortgage_paid_num() {
        return car_mortgage_paid_num;
    }

    public void setCar_mortgage_paid_num(Integer car_mortgage_paid_num) {
        this.car_mortgage_paid_num = car_mortgage_paid_num;
    }

    public Integer getCar_mortgage_month_amount() {
        return car_mortgage_month_amount;
    }

    public void setCar_mortgage_month_amount(Integer car_mortgage_month_amount) {
        this.car_mortgage_month_amount = car_mortgage_month_amount;
    }

    public Integer getPrice_now() {
        return price_now;
    }

    public void setPrice_now(Integer price_now) {
        this.price_now = price_now;
    }

    public String getIs_full_insurance() {
        return is_full_insurance;
    }

    public void setIs_full_insurance(String is_full_insurance) {
        this.is_full_insurance = is_full_insurance;
    }

    public Integer getCustomer_interview_insurance_info_id() {
        return customer_interview_insurance_info_id;
    }

    public void setCustomer_interview_insurance_info_id(Integer customer_interview_insurance_info_id) {
        this.customer_interview_insurance_info_id = customer_interview_insurance_info_id;
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

    public Integer getYear_pay_amount() {
        return year_pay_amount;
    }

    public void setYear_pay_amount(Integer year_pay_amount) {
        this.year_pay_amount = year_pay_amount;
    }

    public Integer getMonth_pay_amount() {
        return month_pay_amount;
    }

    public void setMonth_pay_amount(Integer month_pay_amount) {
        this.month_pay_amount = month_pay_amount;
    }

    public Integer getEffect_date() {
        return effect_date;
    }

    public void setEffect_date(Integer effect_date) {
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
