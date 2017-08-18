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
