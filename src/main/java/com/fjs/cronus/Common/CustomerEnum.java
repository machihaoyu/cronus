package com.fjs.cronus.Common;

/**
 * Created by msi on 2017/10/10.
 */
public enum CustomerEnum {
    intentional_customer(1, "意向客户"),//意向客户
    agreement_customer(2, "协议客户"),
    conversion_customer(3, "成交客户");



    private int value;
    private String name;

    private CustomerEnum(int value, String name) {
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

    public static CustomerEnum getByValue(int value) {
        for (CustomerEnum ocrInfoEnum : values()) {
            if (ocrInfoEnum.getValue() == value) {
                return ocrInfoEnum;
            }
        }
        return null;
    }
    public static CustomerEnum getByIndex(String value) {
        for (CustomerEnum ocrInfoEnum : values()) {
            if (ocrInfoEnum.getName() .equals(value)) {
                return ocrInfoEnum;
            }
        }
        return null;
    }
    public static  void main(String args[]){
       /* switch (CustomerEnum.getByValue(1)) {
            case intentional_customer:
                System.out.println(CustomerEnum.);
                break;
            case agreement_customer:
                System.out.println("456789");
                break;
            case conversion_customer:
                System.out.println("zhnaglei");
                break;
            default:
                System.out.println("asfasdfdasf");
                break;
        }
*/
        Integer a = getByIndex("意向客户").getValue();
        System.out.println(a);

    }

}
