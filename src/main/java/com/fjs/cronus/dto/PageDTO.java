package com.fjs.cronus.dto;

import java.io.Serializable;

public class PageDTO implements Serializable {

    private static final long serialVersionUID = -4868370219742317703L;

    private Integer listRows;
    private Integer totalRows;
    private Integer totalPages;
    private Integer rollPage;

    public Integer getListRows() {
        return listRows;
    }

    public void setListRows(Integer listRows) {
        this.listRows = listRows;
    }

    public Integer getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(Integer totalRows) {
        this.totalRows = totalRows;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getRollPage() {
        return rollPage;
    }

    public void setRollPage(Integer rollPage) {
        this.rollPage = rollPage;
    }
}
