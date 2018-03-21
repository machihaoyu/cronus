package com.fjs.cronus.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.dto.cronus.PhoneAreaDTO;
import com.fjs.cronus.dto.cronus.PhoneResultDTO;
import org.apache.http.client.methods.HttpPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class HttpsClientUtil
{
    private static  Logger log = LoggerFactory.getLogger(HttpsClientUtil.class);
    /**
     * 发送https请求方法
     * @param
     * @return
     */
    public static String sendHttps(String sendUrl)
    {
        String sCurrentLine;
        String sTotalString;
        sCurrentLine = "";
        sTotalString = "";
        //OutputStreamWriter osw = null;
        HttpsURLConnection con = null;

        String hsUrl = sendUrl;
        try {
            URL url = new URL(hsUrl);
            con = (HttpsURLConnection) url.openConnection();
            X509TrustManager xtm = new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    // TODO Auto-generated method stub
                    return null;
                }

                @Override
                public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                        throws CertificateException {
                    // TODO Auto-generated method stub

                }

                @Override
                public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                        throws CertificateException {
                    // TODO Auto-generated method stub

                }
            };

            TrustManager[] tm = { xtm };

            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, tm, null);

            con.setSSLSocketFactory(ctx.getSocketFactory());
            con.setRequestMethod("GET");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            //con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            con.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
            /*osw = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
            osw.write(outWrite);
            osw.flush();
            osw.close();*/


            BufferedReader l_reader = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            while ((sCurrentLine = l_reader.readLine()) != null) {
                sTotalString += sCurrentLine + "\r\n";
            }
            l_reader.close();

            //System.out.println(sTotalString.trim());
            //return sTotalString;

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally
        {
            if (con != null) {
                con.disconnect();
            }
        }
        String resultStr = null;
        try {
            resultStr = sTotalString;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return resultStr;
    }
    public static void main(String args[]){
        String url = "https://api.fangjinsuo.com/phonearea/get/" + "13162706810";
        String string = sendHttps(url);
        PhoneAreaDTO phoneAreaDTO = new PhoneAreaDTO();
        JSONObject jsonObject = JSONObject.parseObject(string);
        PhoneResultDTO  resultDTO = jsonObject.toJavaObject(PhoneResultDTO.class);

        JSONObject json = (JSONObject) resultDTO.getRetData();
        phoneAreaDTO = json.toJavaObject(PhoneAreaDTO.class);
        System.out.println(string);
        System.out.println(phoneAreaDTO.getCity() + phoneAreaDTO.getIsp());


      /*  String result =  HttpsClientUtil.sendHttps("http://beta-base.fang-crm.com/Api/Index/newGetBrand?key=356o192c191db04c54513b0lc28d46ee63954iab");
        System.out.println(result);*/
    }
}
