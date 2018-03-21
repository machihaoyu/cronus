package com.fjs.cronus.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by feng on 2017/9/18.
 */
public class CommonUtil {

    public static String queryStrArrayToStr(String[] array) {
        String str = "";
        if (array.length == 1) {
            str = array[0];
        } else {
            str = array[0];
            for (int i = 1; i < array.length; i++) {
                str += "," + array[i];
            }
        }
        return str;
    }

    public static List<Integer> initStrtoIntegerList(String str) {
        String[] strArray = str.split(",");
        List<Integer> integerList = new ArrayList<>();
        for (int i = 0; i < strArray.length; i++) {
            integerList.add(Integer.valueOf(strArray[i]));
        }
        return integerList;
    }

    /**
     * 拆分集合
     *
     * @param <T>
     * @param resList 要拆分的集合
     * @param count   每个集合的元素个数
     * @return 返回拆分后的各个集合
     */
    public static <T> List<List<T>> splitList(List<T> resList, int count) {

        if (resList == null || count < 1)
            return null;
        List<List<T>> ret = new ArrayList<List<T>>();
        int size = resList.size();
        if (size <= count) { //数据量不足count指定的大小
            ret.add(resList);
        } else {
            int pre = size / count;
            int last = size % count;
            //前面pre个集合，每个大小都是count个元素
            for (int i = 0; i < pre; i++) {
                List<T> itemList = new ArrayList<T>();
                for (int j = 0; j < count; j++) {
                    itemList.add(resList.get(i * count + j));
                }
                ret.add(itemList);
            }
            //last的进行处理
            if (last > 0) {
                List<T> itemList = new ArrayList<T>();
                for (int i = 0; i < last; i++) {
                    itemList.add(resList.get(pre * count + i));
                }
                ret.add(itemList);
            }
        }
        return ret;

    }

    public static String initStrListToStr(List<String> list) {
        StringBuffer str = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            str.append(list.get(i));
            if (i < list.size() - 1) {
                str.append(",");
            }
        }
        return str.toString();
    }

    public static String initIntegerListToStr(List<Integer> list) {
        StringBuffer str = new StringBuffer();
        str.append(list.get(0));
        for (int i = 0; i < list.size(); i++) {
            str.append(",");
            str.append(list.get(i));
        }
        return str.toString();
    }

    public static void main(String[] args) {
        String s1 = "Programming";
        String s2 = new String("Programming");
        String s3 = "Program";
        String s4 = "ming";
        String s5 = "Program" + "ming";
        String s6 = s3 + s4;
        System.out.println(s1 == s2);//false
        System.out.println(s1 == s5);//true
        System.out.println(s1 == s6);//true
        System.out.println(s1 == s6.intern());
        System.out.println(s2 == s2.intern());
    }
}
