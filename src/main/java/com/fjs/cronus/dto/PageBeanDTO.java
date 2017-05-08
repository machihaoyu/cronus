package com.fjs.cronus.dto;

import java.util.List;

/**
 * Created by crm on 2017/5/2.
 */
public class PageBeanDTO<T> {

    private List<T> data;

    private PageDTO oPage;

    private Integer perpage;

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

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
