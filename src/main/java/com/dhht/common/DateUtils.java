package com.dhht.common;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    /**
     * 转换为当天的开始时间
     * @param date
     * @return
     */
    public static Date formatToDayStart(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }
    
    /**
     * 转换为当天的结束时间
     * @param date
     * @return
     */
    public static Date formatToDayEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }
    
    /**
     * 获取相对当前日期间隔一定天数的日期
     * @param dayTotal 间隔的天数
     * @return dayTotal为正数时，返回之后天数的日期，否则返回之前天数的日期
     */
    public static Date getRelativeDayDate(int dayTotal){
    	Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, dayTotal);  
        return calendar.getTime();
    }
    
    /**
     * 获取相对当前日期间隔一定月数的日期
     * @param monthTotal 间隔的月数
     * @return monthTotal为正数时，返回之后月数的日期，否则返回之前月数的日期
     */
    public static Date getRelativeMonthDate(int monthTotal){
    	Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, monthTotal);  
        return calendar.getTime();
    }
    
    /**
     * 获取给定时间的间隔一定月数的日期
     * @param date  给定时间
     * @param monthTotal monthTotal为正数时，返回之后月数的日期，否则返回之前月数的日期
     * @return
     */
    public static Date getRelativeMonthOfAppointedDate(Date date,int monthTotal){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, monthTotal);  
        return calendar.getTime();
    }
    
    /**
     * 获取给定时间的间隔一定天数的日期
     * @param date  给定时间
     * @param monthTotal monthTotal为正数时，返回之后天数的日期，否则返回之前天数的日期
     * @return
     */
    public static Date getRelativeDayOfAppointedDate(Date date,int dayTotal){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, dayTotal);  
        return calendar.getTime();
    }
    
}
