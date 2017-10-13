package com.fjs.test;

import com.alibaba.fastjson.JSONArray;
import com.fjs.cronus.dto.cronus.QuestionsDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by msi on 2017/10/11.
 */
public class DateTest {

    public static void main(String args[]){

      /*  Calendar cal = Calendar.getInstance();//使用默认时区和语言环境获得一个日历。
        cal.add(Calendar.DAY_OF_YEAR, -100);//取当前日期的后一天.
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(cal.getTime());*/
        List<QuestionsDTO> questionsDTOS = new ArrayList<>();
        QuestionsDTO questionsDTO = new QuestionsDTO();
        QuestionsDTO questionsDTO2 = new QuestionsDTO();
        QuestionsDTO questionsDTO3 = new QuestionsDTO();
        questionsDTO.setName("asdad");
        questionsDTO.setAnswer("zhangdsaf");
        questionsDTO2.setName("asdasdfsa");
        questionsDTO2.setAnswer("dsfsfafsdf");
        questionsDTO3.setName("dfasdfasdfasdf");
        questionsDTO3.setAnswer("dasfasfasfs");
        questionsDTOS.add(questionsDTO);
        questionsDTOS.add(questionsDTO2);
        questionsDTOS.add(questionsDTO3);
        //
        JSONArray jsonArray = new JSONArray();

        jsonArray.add(questionsDTO);
        jsonArray.add(questionsDTO2);
        jsonArray.add(questionsDTO3);
        System.out.println(questionsDTOS.toString());
        System.out.println(jsonArray.toString());


    }
}
