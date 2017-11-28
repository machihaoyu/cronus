package com.fjs.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.dto.cronus.QuestionsDTO;

import javax.swing.event.ListDataEvent;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

       /* jsonArray.add(questionsDTO);
        jsonArray.add(questionsDTO2);
        jsonArray.add(questionsDTO3);
        System.out.println(questionsDTOS.toString());*/
        String string = jsonArray.toJSONString(questionsDTOS);
        System.out.println(string);
   /*     List<Integer> list = new ArrayList();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(5);
        list.add(4);
        list.add(7);
        list.add(6);
        list.add(8);
        list.add(9);
        Integer a = 0;
        for (Integer i: list) {
            if (i-1 > a){
                a= i-1;
            }
        }
       System.out.println();*/
   /*     JSONObject jsonObject = new JSONObject();
        jsonObject.put("a",1);
        jsonObject.put("a",2);
        System.out.println(jsonObject.toJSONString());*/
      /*  String[] ALLUSERROLE ={"","业务员","团队长","分公司经理","分公司财务" };
        List<String> roleList = Arrays.<String> asList(ALLUSERROLE);
        for (int i = 0; i < roleList.size(); i++){
             System.out.println(roleList.get(i));
        }

        System.out.println(roleList.size());*/
        BigDecimal a = new BigDecimal(1.456);
        BigDecimal b = new BigDecimal(1.789);
        int result = a.compareTo(b);
        System.out.println(result);
    }
}
