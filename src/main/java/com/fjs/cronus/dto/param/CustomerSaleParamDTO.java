package com.fjs.cronus.dto.param;

/**
 * 客户列表参数
 * Created by crm on 2017/5/5.
 */
public class CustomerSaleParamDTO {

    private String key;
    private Integer user_id;
    private String type;
    private Integer p;
    private String user_ids;
    private Integer data_type;
    private Integer look_phone;
    private String cooperation_status; //合作状态--:
    private String retain; //是否保留--:
    private String customer_name;// 客户名--:
    private Integer customer_level; //客户等级--:
    private String customer_classify; //客户类型--:
    private String customer_type;//客户状态--:
    private String telephonenumber;//电话号--telephonenumber:
    private String customer_source; //客户来源--customer_source:
    private String agreement_number; //居间协议号--:
    private String orderby; //排序的字段--:
    private String pot; //排序的顺倒--:
    private String app_search; //app的手机号和客户名组合查询--:
    private Integer perpage; //每页的个数--:
    //communication_order-是否沟通是否确认(0-未沟未确,1-已沟未确,2-已购已确
    private String communication_order; //是否沟通是否确认--:
    private String owner_user_name;//负责人,搜索选项;
    private String utm_source;//渠道,搜索选项;

    public String getOwner_user_name() {
        return owner_user_name;
    }

    public void setOwner_user_name(String owner_user_name) {
        this.owner_user_name = owner_user_name;
    }

    public String getUtm_source() {
        return utm_source;
    }

    public void setUtm_source(String utm_source) {
        this.utm_source = utm_source;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getP() {
        return p;
    }

    public void setP(Integer p) {
        this.p = p;
    }

    public String getUser_ids() {
        return user_ids;
    }

    public void setUser_ids(String user_ids) {
        this.user_ids = user_ids;
    }

    public Integer getData_type() {
        return data_type;
    }

    public void setData_type(Integer data_type) {
        this.data_type = data_type;
    }

    public Integer getLook_phone() {
        return look_phone;
    }

    public void setLook_phone(Integer look_phone) {
        this.look_phone = look_phone;
    }

    public String getCooperation_status() {
        return cooperation_status;
    }

    public void setCooperation_status(String cooperation_status) {
        this.cooperation_status = cooperation_status;
    }

    public String getRetain() {
        return retain;
    }

    public void setRetain(String retain) {
        this.retain = retain;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public Integer getCustomer_level() {
        return customer_level;
    }

    public void setCustomer_level(Integer customer_level) {
        this.customer_level = customer_level;
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

    public String getTelephonenumber() {
        return telephonenumber;
    }

    public void setTelephonenumber(String telephonenumber) {
        this.telephonenumber = telephonenumber;
    }

    public String getCustomer_source() {
        return customer_source;
    }

    public void setCustomer_source(String customer_source) {
        this.customer_source = customer_source;
    }

    public String getAgreement_number() {
        return agreement_number;
    }

    public void setAgreement_number(String agreement_number) {
        this.agreement_number = agreement_number;
    }

    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public String getPot() {
        return pot;
    }

    public void setPot(String pot) {
        this.pot = pot;
    }

    public String getApp_search() {
        return app_search;
    }

    public void setApp_search(String app_search) {
        this.app_search = app_search;
    }

    public Integer getPerpage() {
        return perpage;
    }

    public void setPerpage(Integer perpage) {
        this.perpage = perpage;
    }

    public String getCommunication_order() {
        return communication_order;
    }

    public void setCommunication_order(String communication_order) {
        this.communication_order = communication_order;
    }
}
