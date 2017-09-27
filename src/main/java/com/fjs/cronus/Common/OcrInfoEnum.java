package com.fjs.cronus.Common;

/**
 * Created by msi on 2017/9/27.
 */

/**
 *  ocr_identity(1),//身份证
 ocr_householdregister(2),//房产证
 ocr_driverlicense(3),
 ocr_drivervehicle(4),
 ocr_houseRegister(5);
 */
public enum OcrInfoEnum {

    ocr_identity(1, "identity"),
    ocr_householdregister(2, "householdregister"),
    ocr_driverlicense(3, "driverlicense"),
    ocr_drivervehicle(4, "drivervehicle"),
    ocr_houseRegister(5, "houseRegister");

    private int value;
    private String name;

    private OcrInfoEnum(int value, String name) {
        this.setValue(value);
        this.setName(name);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static OcrInfoEnum getByValue(int value) {
        for (OcrInfoEnum ocrInfoEnum : values()) {
            if (ocrInfoEnum.getValue() == value) {
                return ocrInfoEnum;
            }
        }
        return null;
    }
    public static  void main(String args[]){
        switch (OcrInfoEnum.getByValue(3)) {
            case ocr_identity:
                System.out.println("123212");
                break;
            case ocr_householdregister:
                System.out.println("456789");
                break;
            case ocr_driverlicense:
                System.out.println("zhnaglei");
                break;
            case ocr_drivervehicle:
                System.out.println("wangqian");
                break;
            case ocr_houseRegister:
                System.out.println("loveyou");
                break;
            default:
                System.out.println("asfasdfdasf");
                break;
        }

    }

}
