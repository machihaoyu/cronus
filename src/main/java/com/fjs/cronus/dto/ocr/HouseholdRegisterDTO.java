package com.fjs.cronus.dto.ocr;

import java.io.Serializable;

/**
 * Created by chenjie on 2017/8/17.
 */
public class HouseholdRegisterDTO extends OcrCronusBaseDTO implements Serializable{

    private static final long serialVersionUID = -744359352406200358L;

    private String household_name;
    private String household_sex;
    private String household_native_place;
    private String household_birthday;
    private String household_id_number;
    private String household_people;

    private String household_job;
    private String household_merriage;
    private String household_education;

    public String getHousehold_job() {
        return household_job;
    }

    public void setHousehold_job(String household_job) {
        this.household_job = household_job;
    }

    public String getHousehold_merriage() {
        return household_merriage;
    }

    public void setHousehold_merriage(String household_merriage) {
        this.household_merriage = household_merriage;
    }

    public String getHousehold_education() {
        return household_education;
    }

    public void setHousehold_education(String household_education) {
        this.household_education = household_education;
    }

    public String getHousehold_name() {
        return household_name;
    }

    public void setHousehold_name(String household_name) {
        this.household_name = household_name;
    }

    public String getHousehold_sex() {
        return household_sex;
    }

    public void setHousehold_sex(String household_sex) {
        this.household_sex = household_sex;
    }

    public String getHousehold_native_place() {
        return household_native_place;
    }

    public void setHousehold_native_place(String household_native_place) {
        this.household_native_place = household_native_place;
    }

    public String getHousehold_birthday() {
        return household_birthday;
    }

    public void setHousehold_birthday(String household_birthday) {
        this.household_birthday = household_birthday;
    }

    public String getHousehold_id_number() {
        return household_id_number;
    }

    public void setHousehold_id_number(String household_id_number) {
        this.household_id_number = household_id_number;
    }

    public String getHousehold_people() {
        return household_people;
    }

    public void setHousehold_people(String household_people) {
        this.household_people = household_people;
    }
}
