package com.fjs.cronus.dto.customer;

import java.util.List;

/**
 * 获取市场推广盘客户信息
 * Created by crm on 2017/5/20.
 */
public class MarketCustomerInfoDTO {

    private MarketCustomerDTO info;
    private List<MarKetCustomerCommunDTO> communitContent;

    public MarketCustomerDTO getInfo() {
        return info;
    }

    public void setInfo(MarketCustomerDTO info) {
        this.info = info;
    }

    public List<MarKetCustomerCommunDTO> getCommunitContent() {
        return communitContent;
    }

    public void setCommunitContent(List<MarKetCustomerCommunDTO> communitContent) {
        this.communitContent = communitContent;
    }
}
