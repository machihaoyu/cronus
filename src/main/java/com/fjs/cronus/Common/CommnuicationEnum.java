package com.fjs.cronus.Common;

/**
 * Created by msi on 2018/3/6.
 */
public enum CommnuicationEnum {

    no_intention("无意向", "无意向"),
    poor_qualifications("资质差无法操作", "资质差无法操作"),
    not_yet_connected("暂未接通", "暂未接通"),
    intention_to_tracked("有意向待跟踪", "有意向待跟踪");

    private String value;
    private String name;

    private CommnuicationEnum(String value, String name) {
        this.setValue(value);
        this.setName(name);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static CommnuicationEnum getByValue(String value) {
        for (CommnuicationEnum commnuicationEnum : values()) {
            if (commnuicationEnum.getValue() == value) {
                return commnuicationEnum;
            }
        }
        return null;
    }
}
