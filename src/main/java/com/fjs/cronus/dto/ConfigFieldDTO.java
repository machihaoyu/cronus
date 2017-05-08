package com.fjs.cronus.dto;

/**
 * Created by crm on 2017/5/5.
 */
public class ConfigFieldDTO {

    private String fieldEleName;
    private String eleName;
    private String eleType;
    private String eleValue;

    public String getEleName() {
        return eleName;
    }

    public void setEleName(String eleName) {
        this.eleName = eleName;
    }

    public String getFieldEleName() {
        return fieldEleName;
    }

    public void setFieldEleName(String fieldEleName) {
        this.fieldEleName = fieldEleName;
    }

    public String getEleType() {
        return eleType;
    }

    public void setEleType(String eleType) {
        this.eleType = eleType;
    }

    public String getEleValue() {
        return eleValue;
    }

    public void setEleValue(String eleValue) {
        this.eleValue = eleValue;
    }
}
