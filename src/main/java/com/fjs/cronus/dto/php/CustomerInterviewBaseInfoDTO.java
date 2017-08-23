package com.fjs.cronus.dto.php;




import java.io.Serializable;

public class CustomerInterviewBaseInfoDTO implements Serializable{

    private static final long serialVersionUID = 6841315540181335020L;
    private Integer customer_interview_base_info_id;//`customer_interview_base_info_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
    private Integer customer_id;// `customer_id` int(11) DEFAULT NULL COMMENT '客户ID',
    private Integer owner_user_id;//`owner_user_id` int(11) DEFAULT NULL COMMENT '当前负责人ID',
    private String owner_user_name;// `owner_user_name` varchar(255) DEFAULT NULL COMMENT '当前负责人姓名',
    private String name;//`name` varchar(50) DEFAULT NULL COMMENT '姓名',
    private String sex;//`sex` varchar(10) DEFAULT NULL COMMENT '性别',
    private String birth_date;//`birth_date` int(10) DEFAULT NULL COMMENT '出生日期',
    private String telephonenumber;//`telephonenumber` varchar(50) DEFAULT NULL COMMENT '手机号码',
    private String marital_status;//`marital_status` varchar(50) DEFAULT NULL COMMENT '婚姻状况',
    private String household_register;//`household_register` varchar(50) DEFAULT NULL COMMENT '户籍地址',
    private String education;//`education` varchar(50) DEFAULT NULL COMMENT '学历',
    private String fee_channel_name;//`fee_channel_name` varchar(50) DEFAULT NULL COMMENT '资金渠道名',
    private String product_name;//`product_name` varchar(50) DEFAULT NULL COMMENT '产品类型名',
    private String loan_amount;//loan_amount` varchar(50) DEFAULT NULL COMMENT '借款金额',
    private String loan_time;//loan_time` int(11) DEFAULT NULL COMMENT '借款期限（月）',
    private String loan_use_time;//`loan_use_time` varchar(50) DEFAULT NULL COMMENT '借款使用时间',
    private String loan_purpose;//`loan_purpose` varchar(50) DEFAULT NULL COMMENT '借款用途',
    private String payment_type;//`payment_type` varchar(50) DEFAULT NULL COMMENT '还款方式',
    private String credit_record;//`credit_record` varchar(50) DEFAULT NULL COMMENT '征信记录',
    private Integer zhima_credit;//`zhima_credit` int(10) DEFAULT NULL COMMENT '芝麻信用积分',
    private Integer credit_query_num_two_month;//`credit_query_num_two_month` int(10) DEFAULT NULL COMMENT '征信近两个月查询次数',
    private Integer credit_query_num_six_month;//`credit_query_num_six_month` int(10) DEFAULT NULL COMMENT '征信近半年查询次数',
    private Integer continuity_overdue_num_two_year;//`continuity_overdue_num_two_year` int(10) DEFAULT NULL COMMENT '两年内连续逾期月数',
    private Integer total_overdue_num_two_year;//`total_overdue_num_two_year` int(10) DEFAULT NULL COMMENT '两年内累计逾期月数',
    private String debt_amount;//`debt_amount` int(10) DEFAULT NULL COMMENT '负债金额',
    private String is_overdue;//`is_overdue` varchar(50) DEFAULT NULL COMMENT '当前是否有逾期',
    private String overdue_amount;//`overdue_amount` int(10) DEFAULT NULL COMMENT '当前逾期金额',
    private String industry;//`industry` varchar(50) DEFAULT NULL COMMENT '行业',
    private String income_amount;//`income_amount` int(10) DEFAULT NULL COMMENT '打卡工资',
    private String social_security_date;//`social_security_date` int(10) DEFAULT NULL COMMENT '社保缴纳时间',
    private String social_security_payment;//`social_security_payment` int(10) DEFAULT NULL COMMENT '社保缴纳基数',
    private String housing_fund_date;//`housing_fund_date` int(10) DEFAULT NULL COMMENT '公积金缴纳时间',
    private String housing_fund_payment;//`housing_fund_payment` int(10) DEFAULT NULL COMMENT '公积金缴纳基数',
    private String work_date;//`work_date` int(10) DEFAULT NULL COMMENT '工作日期',
    private String company_register_date;//`company_register_date` int(10) DEFAULT NULL COMMENT '公司注册时间',
    private String share_rate;//`share_rate` decimal(10,2) DEFAULT NULL COMMENT '占股比例',
    private String public_flow_year_amount;//`public_flow_year_amount` int(10) DEFAULT NULL COMMENT '年对公流水',
    private String private_flow_year_amount;//`private_flow_year_amount` int(10) DEFAULT NULL COMMENT '年对私流水',
    private String is_litigation;//`is_litigation` varchar(50) DEFAULT NULL COMMENT '有无涉诉',
    private String retire_date;//`retire_date` int(10) DEFAULT NULL COMMENT '退休日期',
    private String retirement_pay_min_amount;//`retirement_pay_min_amount` int(10) DEFAULT NULL COMMENT '退休最少工资',
    private String is_relative_known;//`is_relative_known` varchar(50) DEFAULT NULL COMMENT '直系亲属是否知道',
    private String remark;
    private String create_time;//`create_time` int(10) DEFAULT NULL COMMENT '创建时间',
    private String create_user_id;//`create_user_id` int(11) DEFAULT NULL COMMENT '创建用户ID',
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

    public String getLoan_amount() {
        return loan_amount;
    }

    public void setLoan_amount(String loan_amount) {
        this.loan_amount = loan_amount;
    }

    public String getLoan_time() {
        return loan_time;
    }

    public void setLoan_time(String loan_time) {
        this.loan_time = loan_time;
    }

    public String getLoan_use_time() {
        return loan_use_time;
    }

    public void setLoan_use_time(String loan_use_time) {
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

    public String getDebt_amount() {
        return debt_amount;
    }

    public void setDebt_amount(String debt_amount) {
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

    public String getIncome_amount() {
        return income_amount;
    }

    public void setIncome_amount(String income_amount) {
        this.income_amount = income_amount;
    }

    public String getSocial_security_date() {
        return social_security_date;
    }

    public void setSocial_security_date(String social_security_date) {
        this.social_security_date = social_security_date;
    }

    public String getSocial_security_payment() {
        return social_security_payment;
    }

    public void setSocial_security_payment(String social_security_payment) {
        this.social_security_payment = social_security_payment;
    }

    public String getHousing_fund_date() {
        return housing_fund_date;
    }

    public void setHousing_fund_date(String housing_fund_date) {
        this.housing_fund_date = housing_fund_date;
    }

    public String getHousing_fund_payment() {
        return housing_fund_payment;
    }

    public void setHousing_fund_payment(String housing_fund_payment) {
        this.housing_fund_payment = housing_fund_payment;
    }

    public String getWork_date() {
        return work_date;
    }

    public void setWork_date(String work_date) {
        this.work_date = work_date;
    }

    public String getCompany_register_date() {
        return company_register_date;
    }

    public void setCompany_register_date(String company_register_date) {
        this.company_register_date = company_register_date;
    }

    public String getShare_rate() {
        return share_rate;
    }

    public void setShare_rate(String share_rate) {
        this.share_rate = share_rate;
    }

    public String getPublic_flow_year_amount() {
        return public_flow_year_amount;
    }

    public void setPublic_flow_year_amount(String public_flow_year_amount) {
        this.public_flow_year_amount = public_flow_year_amount;
    }

    public String getPrivate_flow_year_amount() {
        return private_flow_year_amount;
    }

    public void setPrivate_flow_year_amount(String private_flow_year_amount) {
        this.private_flow_year_amount = private_flow_year_amount;
    }

    public String getIs_litigation() {
        return is_litigation;
    }

    public void setIs_litigation(String is_litigation) {
        this.is_litigation = is_litigation;
    }

    public String getRetire_date() {
        return retire_date;
    }

    public void setRetire_date(String retire_date) {
        this.retire_date = retire_date;
    }

    public String getRetirement_pay_min_amount() {
        return retirement_pay_min_amount;
    }

    public void setRetirement_pay_min_amount(String retirement_pay_min_amount) {
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
