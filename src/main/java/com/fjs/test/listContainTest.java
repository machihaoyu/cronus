package com.fjs.test;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.dto.ocr.IdCardDTO;
import com.fjs.cronus.util.FastJsonUtils;
import org.joda.time.DateTime;

import java.util.Arrays;

/**
 * Created by msi on 2017/9/20.
 */
public class listContainTest {
    public static void  main(String args[]) {

       /* String[] FILETYPE = {"pdf", "doc", "docx", "xls", "xlsx", "txt", "rar", "zip", "jpg", "png", "jpeg", "gif"};
        if (!Arrays.<String> asList(FILETYPE).contains("123")){

            System.out.println(111);
            return;
        };
    /*    System.out.println(2222);*//*
        String oldName = "12324.jpg";
        String newname= oldName.substring(oldName.lastIndexOf("."));
        System.out.println(newname);*/
/*        JSONObject jsonObject = new JSONObject();
        jsonObject.put("customer_telephone","122151idCardDTO121");
        jsonObject.put("crm_attach_id",1);
        jsonObject.put("create_user_id",1);
        jsonObject.put("create_user_name","dsfasfdasf");
        jsonObject.put("update_user_id",1);
        jsonObject.put("update_user_name","dsfdasfdasf");
        jsonObject.put("category",1);
        IdCardDTO idCardDTO = FastJsonUtils.getSingleBean(jsonObject.toString(), IdCardDTO.class);
        System.out.println(idCardDTO.getCategory());*/
        // TODO 生成对应的ocr信息
      /*  String str = "借款人配偶(反)";
        str.replace("(反)","(正)");
        System.out.println(str);*/
       /* String fileName = "121211.jpg";
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        System.out.println(suffix);*/
        String imagePath = new DateTime().toString("yyyy/MM/dd");
        System.out.println(imagePath);
    }
}
