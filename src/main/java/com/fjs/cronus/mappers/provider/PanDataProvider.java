package com.fjs.cronus.mappers.provider;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by Administrator on 2018/6/12.
 */
@Component
public class PanDataProvider {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public String getPublicSelect(Map<String, Object> para) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("SELECT * from customer_info c where c.own_user_id = -3 ");
        if (para.get("telephone")!=null && StringUtils.isNoneEmpty(para.get("telephone").toString())) {
            stringBuffer.append(" and c.telephonenumber = '"+ para.get("telephone")+"'");
        }
        if (para.get("customerName")!=null && StringUtils.isNoneEmpty(para.get("customerName").toString().trim())) {
            stringBuffer.append(" and c.customer_name like '%"+ para.get("customerName").toString().trim()+"%'");
        }
        stringBuffer.append(" order by ");
        if (para.get("order")!=null && StringUtils.isNoneEmpty(para.get("order").toString()))
            stringBuffer.append(para.get("order"));
        else
            stringBuffer.append("c.create_time ");
        stringBuffer.append(" desc ");
        stringBuffer.append(" limit ");
        if (para.get("start")!=null && StringUtils.isNoneEmpty(para.get("start").toString())) {
            stringBuffer.append(para.get("start"));
        } else {
            stringBuffer.append("0");
        }
        if (para.get("size")!=null && StringUtils.isNoneEmpty(para.get("size").toString())) {
            stringBuffer.append("," + para.get("size"));
        } else {
            stringBuffer.append(",10");
        }

        return stringBuffer.toString();
    }

    public String getPublicSelectCount(Map<String, Object> para) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("SELECT count(1) FROM customer_info c WHERE c.own_user_id = -3 ");
        if (para.get("telephone")!=null && StringUtils.isNoneEmpty(para.get("telephone").toString())) {
            stringBuffer.append(" and c.telephonenumber = '"+ para.get("telephone")+"'");
        }
        if (para.get("customerName")!=null && StringUtils.isNoneEmpty(para.get("customerName").toString().trim())) {
            stringBuffer.append(" and c.customer_name like '%"+ para.get("customerName").toString().trim()+"%'");
        }
        return stringBuffer.toString();
    }
}
