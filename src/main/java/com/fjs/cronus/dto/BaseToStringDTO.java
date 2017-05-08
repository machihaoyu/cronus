package com.fjs.cronus.dto;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Created by crm on 2017/4/29.
 */
public class BaseToStringDTO {

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
