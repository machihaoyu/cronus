package com.fjs.cronus.mappers.provider;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/6/12.
 */
@Component
public class PanDataProvider {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public String getPublicSelect(Map<String, Object> para) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("SELECT * from customer_info c where 1=1");
        if (para.get("ownUser") != null && StringUtils.isNoneEmpty(para.get("ownUser").toString())) {
            stringBuffer.append(" and c.own_user_id = " + para.get("ownUser") + " ");
        }
        if (para.get("telephone") != null && StringUtils.isNoneEmpty(para.get("telephone").toString())) {
            stringBuffer.append(" and c.telephonenumber = '" + para.get("telephone") + "'");
        }
        if (para.get("customerName") != null && StringUtils.isNoneEmpty(para.get("customerName").toString().trim())) {
            stringBuffer.append(" and c.customer_name like '%" + para.get("customerName").toString().trim() + "%'");
        }

        stringBuffer.append(getUserAuthorityScopeSql(para));

        stringBuffer.append(" order by ");
        if (para.get("order") != null && StringUtils.isNoneEmpty(para.get("order").toString()))
            stringBuffer.append(para.get("order"));
        else
            stringBuffer.append("c.create_time ");
        stringBuffer.append(" desc ");
        stringBuffer.append(" limit ");
        if (para.get("start") != null && StringUtils.isNoneEmpty(para.get("start").toString())) {
            stringBuffer.append(para.get("start"));
        } else {
            stringBuffer.append("0");
        }
        if (para.get("size") != null && StringUtils.isNoneEmpty(para.get("size").toString())) {
            stringBuffer.append("," + para.get("size"));
        } else {
            stringBuffer.append(",10");
        }

        logger.info("PublicSelectSql:"+stringBuffer.toString());
        return stringBuffer.toString();
    }

    public String getPublicSelectCount(Map<String, Object> para) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("SELECT count(1) FROM customer_info c WHERE 1=1 ");
        if (para.get("ownUser") != null && StringUtils.isNoneEmpty(para.get("ownUser").toString())) {
            stringBuffer.append(" and c.own_user_id = " + para.get("ownUser") + "");
        }
        if (para.get("telephone") != null && StringUtils.isNoneEmpty(para.get("telephone").toString())) {
            stringBuffer.append(" and c.telephonenumber = '" + para.get("telephone") + "'");
        }
        if (para.get("customerName") != null && StringUtils.isNoneEmpty(para.get("customerName").toString().trim())) {
            stringBuffer.append(" and c.customer_name like '%" + para.get("customerName").toString().trim() + "%'");
        }
        stringBuffer.append(getUserAuthorityScopeSql(para));
        return stringBuffer.toString();
    }

    private String getUserAuthorityScopeSql(Map<String, Object> para)
    {
        StringBuffer stringBuffer = new StringBuffer();
        if (para.get("subCompanyIds") != null && para.get("canMangerMainCity") != null) {

            List<Integer> subCompanyIds = (ArrayList<Integer>) para.get("subCompanyIds");
            List<String> canMangerMainCity = (ArrayList<String>) para.get("canMangerMainCity");
            if (canMangerMainCity.size() > 0 && subCompanyIds.size() == 0) {
                stringBuffer.append(" and city in (");
                for (int i=0 ;i<canMangerMainCity.size() ;i++) {
                    stringBuffer.append("'"+canMangerMainCity.get(i)+"'");
                    if (i!=canMangerMainCity.size()-1)
                        stringBuffer.append(",");
                }
                stringBuffer.append(" ) ");
            } else if (canMangerMainCity.size() == 0 && subCompanyIds.size() > 0) {
                stringBuffer.append(" and sub_company_id in (");
                for (int i=0 ;i<subCompanyIds.size() ;i++) {
                    stringBuffer.append(subCompanyIds.get(i));
                    if (i!=subCompanyIds.size()-1)
                        stringBuffer.append(",");
                }
                stringBuffer.append(" ) ");
            } else if (canMangerMainCity.size() > 0 && subCompanyIds.size() > 0) {
                stringBuffer.append(" and ( city in  (");
                for (int i=0 ;i<canMangerMainCity.size() ;i++) {
                    stringBuffer.append("'"+canMangerMainCity.get(i)+"'");
                    if (i!=canMangerMainCity.size()-1)
                        stringBuffer.append(",");
                }
                stringBuffer.append(" ) ");
                stringBuffer.append(" OR sub_company_id in (");
                for (int i=0 ;i<subCompanyIds.size() ;i++) {
                    stringBuffer.append(subCompanyIds.get(i));
                    if (i!=subCompanyIds.size()-1)
                        stringBuffer.append(",");
                }
                stringBuffer.append(" ) ) ");
            } else if (canMangerMainCity.size() == 0 && subCompanyIds.size() == 0) {
                stringBuffer.append(" and city  is NULL and sub_company_id IS  NULL ");
            }
        }
        return stringBuffer.toString();
    }
}
