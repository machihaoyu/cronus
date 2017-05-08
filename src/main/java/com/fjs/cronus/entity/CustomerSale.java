package com.fjs.cronus.entity;


import com.fjs.cronus.dto.CustomerSaleDTO;
import com.fjs.cronus.dto.PageDTO;

import java.util.List;

public class CustomerSale {
    private List<CustomerSaleDTO> data;

    private String page;

    private PageDTO oPage;

    private Integer perpage;

    public List<CustomerSaleDTO> getData() {
        return data;
    }

    public void setData(List<CustomerSaleDTO> data) {
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
