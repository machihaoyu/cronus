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
        List<EmplouInfo> list = new ArrayList<>();
        EmplouInfo emplouInfo = new EmplouInfo();
        emplouInfo.setCompanyName("上海房金所");
        emplouInfo.setYears("10");
        emplouInfo.setTurnover("1000");
        emplouInfo.setRegisterMoney("1000");
        emplouInfo.setSubscribedMoney("1000");
        emplouInfo.setRoles(1);
        emplouInfo.setStatus(1);
        EmplouInfo emplouInfo1 = new EmplouInfo();
        emplouInfo1.setCompanyName("上海房金所");
        emplouInfo1.setYears("10");
        emplouInfo1.setTurnover("1000");
        emplouInfo1.setRegisterMoney("1000");
        emplouInfo1.setSubscribedMoney("1000");
        emplouInfo1.setRoles(2);
        emplouInfo1.setShares("50");
        emplouInfo1.setStatus(1);

        JSONArray jsonArray = new JSONArray();
        list.add(emplouInfo1);
        list.add(emplouInfo);
       /* jsonArray.add(questionsDTO);
        jsonArray.add(questionsDTO2);
        jsonArray.add(questionsDTO3);
        System.out.println(questionsDTOS.toString());*/
        String string = jsonArray.toJSONString(list);
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
