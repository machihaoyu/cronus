package com.fjs.cronus.dto;

import java.io.Serializable;

/**
 * Created by xulu on 2017/6/5 0005.
 * 合同电子签章dto
 */
public class ElecContractDTO implements Serializable {

    private String a_name;
    private String b_name;
    private String a_identity;
    private String a_address;
    private String a_email;
    private String a_phone;
    private String b_contact;
    private String sign_year;
    private String sign_month;
    private String sign_day;
    private String agreement_number;
    private String a_pay_year;
    private String a_pay_month;
    private String a_pay_day;
    private String a_pay_address_shi;
    private String a_pay_address_qu;
    private String borrower_lower;
    private String borrower_upper;
    private String type;
    private String purpose;
    private String service_money_lower;
    private String service_money_upper;
    private String other_lower;
    private String other_upper;
    private String b_account;
    private String other;
    private String contract_number;
    private String is_chapter;
    private String key;
    private String person_sign;
    private String company_sign;

    public ElecContractDTO() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getA_name() {
        return a_name;
    }

    public void setA_name(String a_name) {
        this.a_name = a_name;
    }

    public String getB_name() {
        return b_name;
    }

    public void setB_name(String b_name) {
        this.b_name = b_name;
    }

    public String getA_identity() {
        return a_identity;
    }

    public void setA_identity(String a_identity) {
        this.a_identity = a_identity;
    }

    public String getA_address() {
        return a_address;
    }

    public void setA_address(String a_address) {
        this.a_address = a_address;
    }

    public String getA_email() {
        return a_email;
    }

    public void setA_email(String a_email) {
        this.a_email = a_email;
    }

    public String getA_phone() {
        return a_phone;
    }

    public void setA_phone(String a_phone) {
        this.a_phone = a_phone;
    }

    public String getB_contact() {
        return b_contact;
    }

    public void setB_contact(String b_contact) {
        this.b_contact = b_contact;
    }

    public String getSign_year() {
        return sign_year;
    }

    public void setSign_year(String sign_year) {
        this.sign_year = sign_year;
    }

    public String getSign_month() {
        return sign_month;
    }

    public void setSign_month(String sign_month) {
        this.sign_month = sign_month;
    }

    public String getSign_day() {
        return sign_day;
    }

    public void setSign_day(String sign_day) {
        this.sign_day = sign_day;
    }

    public String getAgreement_number() {
        return agreement_number;
    }

    public void setAgreement_number(String agreement_number) {
        this.agreement_number = agreement_number;
    }

    public String getA_pay_year() {
        return a_pay_year;
    }

    public void setA_pay_year(String a_pay_year) {
        this.a_pay_year = a_pay_year;
    }

    public String getA_pay_month() {
        return a_pay_month;
    }

    public void setA_pay_month(String a_pay_month) {
        this.a_pay_month = a_pay_month;
    }

    public String getA_pay_day() {
        return a_pay_day;
    }

    public void setA_pay_day(String a_pay_day) {
        this.a_pay_day = a_pay_day;
    }

    public String getA_pay_address_shi() {
        return a_pay_address_shi;
    }

    public void setA_pay_address_shi(String a_pay_address_shi) {
        this.a_pay_address_shi = a_pay_address_shi;
    }

    public String getA_pay_address_qu() {
        return a_pay_address_qu;
    }

    public void setA_pay_address_qu(String a_pay_address_qu) {
        this.a_pay_address_qu = a_pay_address_qu;
    }

    public String getBorrower_lower() {
        return borrower_lower;
    }

    public void setBorrower_lower(String borrower_lower) {
        this.borrower_lower = borrower_lower;
    }

    public String getBorrower_upper() {
        return borrower_upper;
    }

    public void setBorrower_upper(String borrower_upper) {
        this.borrower_upper = borrower_upper;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getService_money_lower() {
        return service_money_lower;
    }

    public void setService_money_lower(String service_money_lower) {
        this.service_money_lower = service_money_lower;
    }

    public String getService_money_upper() {
        return service_money_upper;
    }

    public void setService_money_upper(String service_money_upper) {
        this.service_money_upper = service_money_upper;
    }

    public String getOther_lower() {
        return other_lower;
    }

    public void setOther_lower(String other_lower) {
        this.other_lower = other_lower;
    }

    public String getOther_upper() {
        return other_upper;
    }

    public void setOther_upper(String other_upper) {
        this.other_upper = other_upper;
    }

    public String getB_account() {
        return b_account;
    }

    public void setB_account(String b_account) {
        this.b_account = b_account;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getContract_number() {
        return contract_number;
    }

    public void setContract_number(String contract_number) {
        this.contract_number = contract_number;
    }

    public String getIs_chapter() {
        return is_chapter;
    }

    public void setIs_chapter(String is_chapter) {
        this.is_chapter = is_chapter;
    }

    public String getPerson_sign() {
        return person_sign;
    }

    public void setPerson_sign(String person_sign) {
        this.person_sign = person_sign;
    }

    public String getCompany_sign() {
        return company_sign;
    }

    public void setCompany_sign(String company_sign) {
        this.company_sign = company_sign;
    }
}
