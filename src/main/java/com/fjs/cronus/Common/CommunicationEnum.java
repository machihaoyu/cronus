package com.fjs.cronus.Common;

/**
 * Created by msi on 2018/3/6.
 */
public enum CommunicationEnum {

    no_intention("无意向", "无意向"),
    poor_qualifications("资质差无法操作", "资质差无法操作"),
    not_yet_connected("暂未接通", "暂未接通"),
    intention_to_tracked("有意向待跟踪", "有意向待跟踪");

    private String value;
    private String name;

    private CommunicationEnum(String value, String name) {
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

    public static CommunicationEnum getByValue(String value) {
        for (CommunicationEnum communicationEnum : values()) {
            if (communicationEnum.getValue().equals(value)) {
                return communicationEnum;
            }
        }
        return null;
    }

    public static void main(String[] args){

        if (CommunicationEnum.getByValue("无意向sdaf") != null) {
            switch (CommunicationEnum.getByValue("无意向safa")) {
                case no_intention:
                    //TODO 发送短信
                    System.out.println("1");
                    break;
                case poor_qualifications:
                    System.out.println("2");
                    break;
                case not_yet_connected:
                    System.out.println("3");
                    break;
                case intention_to_tracked:
                    System.out.println("4");
                    break;
            }
        }else {
            System.out.println("bufuhepipei");
        }

    }
}
