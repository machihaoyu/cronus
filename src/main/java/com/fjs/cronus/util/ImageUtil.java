package com.fjs.cronus.util;

import sun.misc.BASE64Encoder;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

/**
 *
 * 缩略图片的大小尺寸等
 */
public class ImageUtil {
    /** * 图片文件读取 * * @param srcImgPath * @return */
    @SuppressWarnings("uncheck")
    private static BufferedImage InputImage(InputStream inputStream,int new_w, int new_h) {
        BufferedImage srcImage = null;
        try {
            srcImage = javax.imageio.ImageIO.read(inputStream);

        } catch (IOException e) {
            System.out.println("读取图片文件出错！" + e.getMessage());
            e.printStackTrace();
        }
        return srcImage;
    }

    /**
     * * 将图片按照指定的图片尺寸压缩 * * @param srcImgPath :源图片路径 * @param outImgPath *
     * :输出的压缩图片的路径 * @param new_w * :压缩后的图片宽 * @param new_h * :压缩后的图片高
     */
    @SuppressWarnings("uncheck")
    public static String  compressImage(InputStream srcImgPath,
                                     int new_w, int new_h) throws Exception{
        BufferedImage src = InputImage(srcImgPath, new_w,  new_h);
        if (null != src) {
            int old_w = src.getWidth();
            // 得到源图宽
            int old_h = src.getHeight();
            if(old_w < new_w && old_h < new_h){
                return "0";
            }
        }
        String  base64Code = disposeImage(src, new_w, new_h);
        return  base64Code;
    }

/*    *//**
     * * 指定长或者宽的最大值来压缩图片 * * @param srcImgPath * :源图片路径 * @param outImgPath *
     * :输出的压缩图片的路径 * @param maxLength * :长或者宽的最大值
     *//*
    public static void compressImage(String srcImgPath, String outImgPath,
                                     int maxLength) throws Exception {
        // 得到图片
        BufferedImage src = InputImage(srcImgPath);
        if (null != src) {
            int old_w = src.getWidth();
            // 得到源图宽
            int old_h = src.getHeight();
            // 得到源图长
            int new_w = 0;
            // 新图的宽
            int new_h = 0;
            // 新图的长
            // 根据图片尺寸压缩比得到新图的尺寸
            if (old_w > old_h) {
                // 图片要缩放的比例
                new_w = maxLength;
                new_h = (int) Math.round(old_h * ((float) maxLength / old_w));
            } else {
                new_w = (int) Math.round(old_w * ((float) maxLength / old_h));
                new_h = maxLength;
            }
            disposeImage(src, outImgPath, new_w, new_h);
        }
    }*/

    /** * 处理图片 * * @param src * @param outImgPath * @param new_w * @param new_h */
    @SuppressWarnings("uncheck")
    private synchronized static String disposeImage(BufferedImage src,
                                                    int new_w, int new_h) throws Exception
    {
        // 得到图片
        int old_w = src.getWidth();
        // 得到源图宽
        int old_h = src.getHeight();
        // 得到源图长
        BufferedImage newImg = null;
        // 判断输入图片的类型
        switch (src.getType()) {
            case 13:
                // png,gifnewImg = new BufferedImage(new_w, new_h,
                // BufferedImage.TYPE_4BYTE_ABGR);
                break;
            default:
                newImg = new BufferedImage(new_w, new_h, BufferedImage.TYPE_INT_RGB);
                break;
        }
        Graphics2D g = newImg.createGraphics();
        // 从原图上取颜色绘制新图
        g.drawImage(src, 0, 0, old_w, old_h, null);
        g.dispose();
        // 根据图片尺寸压缩比得到新图的尺寸
        newImg.getGraphics().drawImage(
                src.getScaledInstance(new_w, new_h, Image.SCALE_SMOOTH), 0, 0,
                null);
        // 调用方法输出图片文件
       // OutImage(outImgPath, newImg);
        //对缩略图进行64编码
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(newImg, "jpg", outputStream);
        BASE64Encoder encoder = new BASE64Encoder();
        String base64Code = encoder.encode(outputStream.toByteArray());
        return  base64Code;
    }
}