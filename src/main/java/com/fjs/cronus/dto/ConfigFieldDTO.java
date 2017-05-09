package com.fjs.cronus.dto;

/**
 * 模板在HTML中显示的元素DTO
 * Created by crm on 2017/5/5.
 */
public class ConfigFieldDTO implements Comparable<ConfigFieldDTO> {

    //html中的元素   aaa <input type="text" value=""  class="1" />
    //上面表单中的  aaa
    private String fieldEleName;
    //上面表单中的  input
    private String eleName;
    //上面表单中的  class="1" 中的1或0 ， 1 必须输项
    private String validate;
    //上面表单中的  text
    private String eleType;
    //上面表单中的  value 值
    private String eleValue;
    //上面表单在页面显示的顺序
    private Integer sort;

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

    public String getValidate() {
        return validate;
    }

    public void setValidate(String validate) {
        this.validate = validate;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Override
    public int compareTo(ConfigFieldDTO o) {
        return this.getSort() - o.getSort();
    }
}
