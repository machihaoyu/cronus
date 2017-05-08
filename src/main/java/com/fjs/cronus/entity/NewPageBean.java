package com.fjs.cronus.entity;

import com.fjs.cronus.dto.PageDTO;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chenjie on 2017/4/23.
 */
public class NewPageBean<T> implements Serializable{

    private List<T> data;

    private String page;

    private PageDTO oPage;

    private Integer perpage;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public PageDTO getoPage() {
        return oPage;
    }

    public void setoPage(PageDTO oPage) {
        this.oPage = oPage;
    }

    public Integer getPerpage() {
        return perpage;
    }

    public void setPerpage(Integer perpage) {
        this.perpage = perpage;
    }
}
