package com.fjs.cronus.util;

import java.io.*;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * Created by msi on 2017/9/20.
 */
public class FileBase64ConvertUitl {

    /**
     * 将文件转成base64 字符串
     * @param
     * @return
     * @throws Exception
     */

    public static String encodeBase64File(InputStream in)  {
        String strBase64 = null;
        try {
            byte[] bytes = new byte[in.available()];
            // 将文件中的内容读入到数组中
            in.read(bytes);
            strBase64 = new BASE64Encoder().encode(bytes);//将字节流数组转换为字符串
            in.close();
          }catch (Exception fe) {
           fe.printStackTrace();
        }
        return strBase64;

    }

    /**
     * 将base64字符解码保存文件
     * @param base64Code
     * @param
     * @throws Exception
     */

    public static InputStream decoderBase64File(String base64Code)
             {
        try {
            byte[] buffer = new BASE64Decoder().decodeBuffer(base64Code);
            InputStream in  = new ByteArrayInputStream(buffer);
            return in;
        }catch (Exception e){
            e.printStackTrace();
        }
      /*  FileOutputStream out = new FileOutputStream(targetPath);
        out.write(buffer);
        out.close();*/
      return null;
    }

    /**
     * 将base64字符保存文本文件
     * @param base64Code
     * @param targetPath
     * @throws Exception
     */

    public static void toFile(String base64Code, String targetPath)
            throws Exception {

        byte[] buffer = base64Code.getBytes();
        FileOutputStream out = new FileOutputStream(targetPath);
        out.write(buffer);
        out.close();
    }

    public static void main(String[] args) {
        try {
         /*   //String base64Code = encodeBase64File("D:/image/123.jpg");
            System.out.println(base64Code);
            //decoderBase64File(base64Code, "D:/456.jpg");
            toFile(base64Code, "D:\\123.txt");*/
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

}