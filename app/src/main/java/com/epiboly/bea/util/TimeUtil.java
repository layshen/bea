package com.epiboly.bea.util;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author vemao
 * @time 2023/2/6
 * @describe
 */
public class TimeUtil {
    public static Date formatDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date date = formatter.parse(strDate, pos);
        return date;
    }

    public static String formatString(Date date, int type) {
        DateFormat df = null;
        if (type == 1) {
            df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } else {
            df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        }
        return df.format(date);
    }

    //年月日时分秒
    public static String longToYMDHMS(long time) {
        if (time < 10000000000L) {
            time = time * 1000;
        }
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeString = sdf.format(date);//Date-->String
        return timeString;
    }
}
