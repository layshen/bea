package com.epiboly.bea.util;

import java.util.Formatter;

/**
 * @author vemao
 * @time 2023/2/12
 * @describe
 */
public class MathUtil {
    public static String format4(double value) {
        /*
         * %.2f % 表示 小数点前任意位数 2 表示两位小数 格式后的结果为 f 表示浮点型
         */
        return new Formatter().format("%.2f", value).toString();
    }

    public static int parseInt(String s) {
        try {
            return Integer.parseInt(s);
        }catch (Exception e){
            return 0;
        }
    }
}
