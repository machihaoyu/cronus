package com.fjs.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by msi on 2017/10/11.
 */
public class DateTest {

    public static void main(String args[]){

        Calendar cal = Calendar.getInstance();//使用默认时区和语言环境获得一个日历。
        cal.add(Calendar.DAY_OF_YEAR, -100);//取当前日期的后一天.
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(cal.getTime());
    }
}
