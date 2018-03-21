package com.fjs.cronus.dto;

import java.util.List;

/**
 * Created by Administrator on 2017/6/12 0012.
 */
public class QueryResult<T> {
    private String total;
    private List<T> rows;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
