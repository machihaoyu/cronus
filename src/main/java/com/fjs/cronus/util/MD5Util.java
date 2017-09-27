package com.fjs.cronus.util;


import java.io.IOException;

import java.io.InputStream;
import java.security.MessageDigest;



public class MD5Util {

    static char hexdigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };



    /**

     * @funcion 对文件全文生成MD5摘要

     * @return MD5摘要码

     */
    @SuppressWarnings("uncheck")
    public static String getMD5( InputStream fis ) {



        try {

            MessageDigest md = MessageDigest.getInstance("MD5");

           // fis = new FileInputStream(file);

            byte[] buffer = new byte[2048];

            int length = -1;

            while ((length = fis.read(buffer)) != -1) {

                md.update(buffer, 0, length);

            }

            byte[] b = md.digest();

            return byteToHexString(b);

        } catch (Exception e) {

            e.printStackTrace();

            return null;

        } finally {

            try {

                fis.close();

            } catch (IOException e) {

                e.printStackTrace();

            }

        }

    }



    /**

     * @function 把byte[]数组转换成十六进制字符串表示形式

     * @param tmp  要转换的byte[]

     * @return 十六进制字符串表示形式

     */
    @SuppressWarnings("uncheck")
    private static String byteToHexString(byte[] tmp) {

        String s;

        // 用字节表示就是 16 个字节

        // 每个字节用 16 进制表示的话，使用两个字符，所以表示成 16 进制需要 32 个字符

        // 比如一个字节为01011011，用十六进制字符来表示就是“5b”

        char str[] = new char[16 * 2];

        int k = 0; // 表示转换结果中对应的字符位置

        for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节转换成 16 进制字符的转换

            byte byte0 = tmp[i]; // 取第 i 个字节

            str[k++] = hexdigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换, >>> 为逻辑右移，将符号位一起右移

            str[k++] = hexdigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换

        }



        s = new String(str); // 换后的结果转换为字符串

        return s;

    }

    @SuppressWarnings("uncheck")
    public static String getMd5Code(String base64Code) {
        try {
            String md5 = getMD5(FileBase64ConvertUitl.decoderBase64File(base64Code));
            return md5;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String arg[]) throws Exception {

        String base64Code = FileBase64ConvertUitl.encodeBase64File("d:123.jpg");
        String base64Code1 = FileBase64ConvertUitl.encodeBase64File("d:456.jpg");
        String base64Code2 = FileBase64ConvertUitl.encodeBase64File("d:789.jpg");
        String a = getMD5(FileBase64ConvertUitl.decoderBase64File(base64Code));

        String b = getMD5(FileBase64ConvertUitl.decoderBase64File(base64Code1));

        String c = getMD5(FileBase64ConvertUitl.decoderBase64File(base64Code2));



        System.out.println("a.txt的摘要值为：" + a);

        System.out.println("b.txt的摘要值为：" + b);

        System.out.println("c.txt的摘要值为：" + c);



        if(a.equals(b)) {

            System.out.println("a.txt中的内容与b.txt中的内容一致");

        } else {

            System.out.println("a.txt中的内容与b.txt中的内容不一致");

        }



        if(a.equals(c)) {

            System.out.println("a.txt中的内容与c.txt中的内容一致");

        } else {

            System.out.println("a.txt中的内容与c.txt中的内容不一致");

        }

    }

}