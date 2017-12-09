package com.fjs.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.dto.cronus.EmplouInfo;
import com.fjs.cronus.dto.cronus.QuestionsDTO;
import org.w3c.dom.ls.LSInput;

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


        /**
         *    @ApiModelProperty(value = "企业名称",notes = "企业名称")
        private String companyName;

         @ApiModelProperty(value = "成立年限",notes = "成立年限")
         private String years;

         @ApiModelProperty(value = "年流水",notes = "年流水")
         private String turnover;

         @ApiModelProperty(value = "注册资本",notes = "注册资本")
         private String registerMoney;

         @ApiModelProperty(value = "认缴资本",notes = "认缴资本")
         private String subscribedMoney;

         @ApiModelProperty(value = "角色1法人 2股东 3高管",notes = "角色1法人 2股东 3高管")
         private Integer roles;

         @ApiModelProperty(value = "角色2股东占股多少",notes = "角色2股东占股多少")
         private String shares;
         @ApiModelProperty(value = "角色3高管 职位",notes = "角色3高管 职位")
         private String position;

         @ApiModelProperty(value = "经营状态：1存续，2在业，3吊销，4注销，5迁出，6迁入，7停业，8清算",notes = "经营状态：1存续，2在业，3吊销，4注销，5迁出，6迁入，7停业，8清算")
         private Integer status;

         */
      /*  Calendar cal = Calendar.getInstance();//使用默认时区和语言环境获得一个日历。
        cal.add(Calendar.DAY_OF_YEAR, -100);//取当前日期的后一天.
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(cal.getTime());*/
        /*List<QuestionsDTO> questionsDTOS = new ArrayList<>();
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
        //*/
        JSONObject jsonObject = new JSONObject();

       // a:1:{i:0;a:3:{s:7:"content";s:12:"按规定发";s:14:"create_user_id";s:1:"1";s:11:"create_time";i:1512095726;}}
        jsonObject.put("content","按规定发");
        jsonObject.put("create_user_id","1");
        jsonObject.put("create_time","1512095726");


        // a:1:{i:0;a:3:{s:7:"content";s:12:"按规定发";s:14:"create_user_id";s:1:"1";s:11:"create_time";i:1512095726;}}
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("content","sdaadsfasdfasf");
        jsonObject1.put("create_user_id","1");
        jsonObject1.put("create_time","1512095726");
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(jsonObject);
        jsonArray.add(jsonObject1);

        System.out.println(jsonArray.toString());

    }
}
