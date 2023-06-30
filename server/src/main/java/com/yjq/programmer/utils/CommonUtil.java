package com.yjq.programmer.utils;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-04-26 19:09
 */

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 通用工具类
 */
public class CommonUtil {

    /**
     * 验证字符串是否为空
     * @return
     */
    public static boolean isEmpty(String str) {
        if(str == null || "".equals(str)) {
            return true; //为空
        }else {
            return false; //不为空
        }
    }

    /**
     * 判断后缀是否是图片文件的后缀
     * @param suffix
     * @return
     */
    public static boolean isPhoto(String suffix){
        if("jpg".toUpperCase().equals(suffix.toUpperCase())){
            return true;
        }else if("png".toUpperCase().equals(suffix.toUpperCase())){
            return true;
        }else if("gif".toUpperCase().equals(suffix.toUpperCase())){
            return true;
        }else if("jpeg".toUpperCase().equals(suffix.toUpperCase())){
            return true;
        }else{
            return false;
        }
    }
    /**
     * 返回指定格式的日期字符串
     * @param date
     * @param formatter
     * @return
     */
    public static String getFormatterDate(Date date, String formatter){
        SimpleDateFormat sdf = new SimpleDateFormat(formatter);
        return sdf.format(date);
    }

    /**
     * 将字符串日期转换成时间
     * @param str
     * @param formatter
     * @return
     * @throws ParseException
     */
    public static Date getFormatterStr(String str, String formatter) {
        DateFormat format = new SimpleDateFormat(formatter);
        try {
            return format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 判断两个日期的间隔天数
     * @param startTime
     * @param endTime
     * @return
     */
    public static int differentDaysByMillisecond(Date startTime, Date endTime)
    {
        int days = (int) ((endTime.getTime() - startTime.getTime()) / (1000*3600*24));
        return days;
    }


}
