package com.fjs.cronus.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;

/**
 * 3des加密
 * @author xulu
 */
public class DEC3Util {
    private static String keyString = "WP2gg%2Yoh!VD78BJV@r5xKv";
    private static String ivString = "JV@r5xKv";
    private static Key deskey = null;
    private static IvParameterSpec iv = null;
    private static String code = "utf-8";

    private static final Logger logger = LoggerFactory.getLogger(DEC3Util.class);

    static {
        try{
            DESedeKeySpec keySpec = new DESedeKeySpec(keyString.getBytes(code));// 设置密钥参数
            iv = new IvParameterSpec(ivString.getBytes(code));// 设置向量
            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");// 获得密钥工厂
            deskey = keyfactory.generateSecret(keySpec);// 得到密钥对象
        } catch (Exception e){
            logger.error("3des加密初始化参数失败", e);
        }
    }

    /**
     * ECB加密,不要IV
     */
    public static String des3EncodeECB(String data)
            throws Exception {
        Cipher cipher = Cipher.getInstance("desede" + "/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, deskey);
        byte[] bOut = cipher.doFinal(data.getBytes(code));
        BASE64Encoder base64Encoder = new BASE64Encoder();
        return base64Encoder.encode(bOut);
    }

    /**
     * ECB解密,不要IV
     */
    public static String ees3DecodeECB(String data) throws Exception {
        Cipher cipher = Cipher.getInstance("desede" + "/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, deskey);
        BASE64Decoder base64Decoder = new BASE64Decoder();
        byte[] bOut = cipher.doFinal(base64Decoder.decodeBuffer(data));
        return new String(bOut, code);
    }

    /**
     * CBC加密
     */
    public static String des3EncodeCBC(String data)
            throws Exception {
        Cipher cipher = Cipher.getInstance("desede" + "/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, deskey, iv);
        byte[] bOut = cipher.doFinal(data.getBytes(code));
        BASE64Encoder base64Encoder = new BASE64Encoder();
        return base64Encoder.encode(bOut);
    }

    /**
     * CBC解密
     */
    public static String des3DecodeCBC(String data)
            throws Exception {
        Cipher cipher = Cipher.getInstance("desede" + "/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, deskey, iv);
        BASE64Decoder base64Decoder = new BASE64Decoder();
        byte[] bOut = cipher.doFinal(base64Decoder.decodeBuffer(data));
        return new String(bOut, code);
    }

    public static void main(String[] args) throws Exception {
        String data = "18616680539";
        System.out.println("CBC加密解密");
       // String str5 = DEC3Util.des3EncodeCBC(data);
        String str6 = DEC3Util.des3DecodeCBC("FdT6QhpkAU3JzRDNlJ1uiQ==");
       // System.out.println("加密结果：" + str5);
        System.out.println("解密结果：" + str6);
    }
}