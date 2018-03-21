package com.fjs.cronus.Common;

/**
 * Created by msi on 2017/10/17.
 */
public enum ProductTyoeEnum {

    producttype_credit(1, "信用"),//意向客户
    producttype_mortgage(2, "抵押"),
    producttype_ransomfloor(3, "赎楼");

    private int value;
    private String name;

    private ProductTyoeEnum(int value, String name) {
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

    public static ProductTyoeEnum getByValue(int value) {
        for (ProductTyoeEnum ocrInfoEnum : values()) {
            if (ocrInfoEnum.getValue() == value) {
                return ocrInfoEnum;
            }
        }
        return null;
    }
    public static ProductTyoeEnum getByIndex(String value) {
        for (ProductTyoeEnum ocrInfoEnum : values()) {
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

        System.out.println(ProductTyoeEnum.producttype_credit.getValue());

    }

}
