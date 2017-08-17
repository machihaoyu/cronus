package com.fjs.cronus.dto.ocr;

import java.io.Serializable;

/**
 * Created by chenjie on 2017/8/17.
 */
public class IdCardDTO extends OcrCronusBaseDTO implements Serializable {

    private static final long serialVersionUID = -4922293620210293465L;

    private String card_name;// varchar(50) DEFAULT NULL COMMENT '身份证-姓名',
    private String card_sex;// varchar(10) DEFAULT NULL COMMENT '身份证-性别',
    private String card_nation;// varchar(50) DEFAULT NULL COMMENT '身份证-民族',
    private String card_birth;// varchar(30) DEFAULT NULL COMMENT '身份证-出生日期',
    private String card_address;// varchar(100) DEFAULT NULL COMMENT '身份证-住址',
    private String card_num;// varchar(20) DEFAULT NULL COMMENT '身份证-身份证号',
    private String card_sign_org;// varchar(50) DEFAULT NULL COMMENT '身份证-签发机关',
    private String card_valid_start;// varchar(50) DEFAULT NULL COMMENT '身份证-有效期-开始',
    private String card_valid_end;// varchar(50) DEFAULT NULL COMMENT '身份证-有效期-截止',

    public String getCard_name() {
        return card_name;
    }

    public void setCard_name(String card_name) {
        this.card_name = card_name;
    }

    public String getCard_sex() {
        return card_sex;
    }

    public void setCard_sex(String card_sex) {
        this.card_sex = card_sex;
    }

    public String getCard_nation() {
        return card_nation;
    }

    public void setCard_nation(String card_nation) {
        this.card_nation = card_nation;
    }

    public String getCard_birth() {
        return card_birth;
    }

    public void setCard_birth(String card_birth) {
        this.card_birth = card_birth;
    }

    public String getCard_address() {
        return card_address;
    }

    public void setCard_address(String card_address) {
        this.card_address = card_address;
    }

    public String getCard_num() {
        return card_num;
    }

    public void setCard_num(String card_num) {
        this.card_num = card_num;
    }

    public String getCard_sign_org() {
        return card_sign_org;
    }

    public void setCard_sign_org(String card_sign_org) {
        this.card_sign_org = card_sign_org;
    }

    public String getCard_valid_start() {
        return card_valid_start;
    }

    public void setCard_valid_start(String card_valid_start) {
        this.card_valid_start = card_valid_start;
    }

    public String getCard_valid_end() {
        return card_valid_end;
    }

    public void setCard_valid_end(String card_valid_end) {
        this.card_valid_end = card_valid_end;
    }
}
