package com.fjs.cronus.util;

import org.springframework.stereotype.Service;

import java.net.URL;

/**
 * Created by msi on 2017/10/12.
 */
@Service
public class JavaUrlConnectionUtil {


    public static  String getReturnData(String urlString) {
        String res = "";
        try {
            URL url = new URL(urlString);
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url
                    .openConnection();
            conn.connect();
            java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream(),"UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                res += line;
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static void main(String args[]){
        String url = "https://api.fangjinsuo.com/phonearea/get/" + "13162706810";
         String str = getReturnData(url);
         System.out.println(str);

    }
}
