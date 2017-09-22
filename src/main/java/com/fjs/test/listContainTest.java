package com.fjs.test;

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
        String str = "借款人配偶(反)";
        str.replace("(反)","(正)");
        System.out.println(str);
    }
}
