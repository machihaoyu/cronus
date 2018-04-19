package com.fjs.cronus.dto.customer;

public class CustomerComDTO {


    private String operation;//操作类型
    private Integer count;//几条数据

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
