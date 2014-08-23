package cn.magic.rubychina.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * Created by magic on 2014/8/20.
 */
public class DataUtil {
    public static final int DAYINT = 24 * 3600;
    public static final int HOURINT = 3600;
    public static final int MINUTE = 60;

    public static String getShowTimeString(String showTime, DateFormat dateFormat) {
        Date date = new Date();
        Date nowTime = new Date();
        try {
            date = dateFormat.parse(showTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 获得时间差的秒数
        long between = Math.abs((nowTime.getTime() - date.getTime()) / 1000);

        long day = between / DAYINT;

        long hour = between % DAYINT / HOURINT;

        long minute = between % HOURINT / MINUTE;

        if (day > 0) {
            return day + "天前";
        } else if (hour > 0) {
            return hour + "小时前";
        } else if (minute > 0) {
            return minute + "分钟前";
        } else {
            return between + "秒前";
        }
    }
}
