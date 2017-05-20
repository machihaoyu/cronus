package com.fjs.cronus.dto.customer;

/**
 * 海贷魔方盘客户
 * Created by crm on 2017/5/20.
 */
public class HaidaiCustomerDTO {

    private Integer id;//114,
    private Integer sale_id;//15,
    private String name;//5554,
    private String telephone;//18566669956,
    private String old_telephone;//18817300694,
    private String loan_amount;//20,
    private String city;//上海,
    private String customer_source;//海贷魔方,
    private String utm_source;//海贷魔方,
    private Integer status;//-2,
    private String exend_text;
    private Integer create_time;//1484104556,
    private Integer update_time;//1495191496,
    private String owner_user_name;//团队长测试,
    private String cust_house_city;//,
    private String cust_city;//

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSale_id() {
        return sale_id;
    }

    public void setSale_id(Integer sale_id) {
        this.sale_id = sale_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getOld_telephone() {
        return old_telephone;
    }

    public void setOld_telephone(String old_telephone) {
        this.old_telephone = old_telephone;
    }

    public String getLoan_amount() {
        return loan_amount;
    }

    public void setLoan_amount(String loan_amount) {
        this.loan_amount = loan_amount;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getExend_text() {
        return exend_text;
    }

    public void setExend_text(String exend_text) {
        this.exend_text = exend_text;
    }

    public Integer getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Integer create_time) {
        this.create_time = create_time;
    }

    public Integer getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Integer update_time) {
        this.update_time = update_time;
    }

    public String getOwner_user_name() {
        return owner_user_name;
    }

    public void setOwner_user_name(String owner_user_name) {
        this.owner_user_name = owner_user_name;
    }

    public String getCust_house_city() {
        return cust_house_city;
    }

    public void setCust_house_city(String cust_house_city) {
        this.cust_house_city = cust_house_city;
    }

    public String getCust_city() {
        return cust_city;
    }

    public void setCust_city(String cust_city) {
        this.cust_city = cust_city;
    }
}