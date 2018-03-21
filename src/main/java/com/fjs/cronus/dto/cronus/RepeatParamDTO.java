package com.fjs.cronus.dto.cronus;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by msi on 2017/12/12.
 */
public class RepeatParamDTO  implements Serializable{

    private Integer reat_num;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date repeat_callback_time;

    public Integer getReat_num() {
        return reat_num;
    }

    public void setReat_num(Integer reat_num) {
        this.reat_num = reat_num;
    }

    public Date getRepeat_callback_time() {
        return repeat_callback_time;
    }

    public void setRepeat_callback_time(Date repeat_callback_time) {
        this.repeat_callback_time = repeat_callback_time;
    }
}
