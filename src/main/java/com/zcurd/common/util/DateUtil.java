package com.zcurd.common.util;

/**
 * <p>Title: 时间和日期的工具类</p>
 * <p>Description: DateUtil类包含了标准的时间和日期格式，以及这些格式在字符串及日期之间转换的方法</p>
 * <p>Copyright: Copyright (c) 2017 dz,Inc. All Rights Reserved</p>
 * <p>Company: dz,Inc.</p>
 *
 * @author dz
 * @version 1.0
 */

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {
    //~ Static fields/initializers =============================================

    private static final String datePattern = "MM/dd/yyyy";

    private static final String datePattern2 = "yyyy-MM-dd HH:mm:ss";

    private static final String datePattern3 = "yyyy-MM-dd";

    private static final String datePattern4 = "yyyy-MM";

    private static final String timePattern = datePattern + " HH:MM a";

    //~ Methods ================================================================

    /**
     * Return default datePattern (MM/dd/yyyy)
     *
     * @return a string representing the date pattern on the UI
     */
    public static String getDatePattern() {
        return datePattern;
    }

    /**
     * This method attempts to convert an Oracle-formatted date
     * in the form dd-MMM-yyyy to mm/dd/yyyy.
     *
     * @param aDate date from database as a string
     * @return formatted string for the ui
     */
    public static final String getDate(Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";

        if (aDate != null) {
            df = new SimpleDateFormat(datePattern);
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }

    public static final String date2Str(Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";

        if (aDate != null) {
            df = new SimpleDateFormat(datePattern);
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }

    public static final String date2Str(String pattern, Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";

        if (aDate != null) {
            df = new SimpleDateFormat(pattern);
            returnValue = df.format(aDate);
        }
        return (returnValue);
    }

    /**
     * This method generates a string representation of a date/time
     * in the format you specify on input
     *
     * @param aMask   the date pattern the string is in
     * @param strDate a string representation of a date
     * @return a converted Date object
     * @throws ParseException
     * @see SimpleDateFormat
     */
    public static final Date convertStringToDate(String aMask, String strDate)
            throws ParseException {
        SimpleDateFormat df = null;
        Date date = null;
        df = new SimpleDateFormat(aMask);

        try {
            date = df.parse(strDate);
        } catch (ParseException pe) {
            return null;
        }

        return (date);
    }

    public static final Date str2Date(String aMask, String strDate)
            throws ParseException {
        SimpleDateFormat df = null;
        Date date = null;
        df = new SimpleDateFormat(aMask);

        try {
            date = df.parse(strDate);
        } catch (ParseException pe) {
            return null;
        }

        return (date);
    }

    /**
     * This method returns the current date time in the format:
     * MM/dd/yyyy HH:MM a
     *
     * @param theTime the current time
     * @return the current date/time
     */
    public static String getTimeNow(Date theTime) {
        return getDateTime(timePattern, theTime);
    }

    /**
     * This method returns the current date in the format: MM/dd/yyyy
     *
     * @return the current date
     * @throws ParseException
     */
    public static Calendar getToday() throws ParseException {
        Date today = new Date();
        SimpleDateFormat df = new SimpleDateFormat(datePattern);

        // This seems like quite a hack (date -> string -> date),
        // but it works ;-)
        String todayAsString = df.format(today);
        Calendar cal = new GregorianCalendar();
        cal.setTime(convertStringToDate(todayAsString));

        return cal;
    }

    /**
     * This method generates a string representation of a date's date/time
     * in the format you specify on input
     *
     * @param aMask the date pattern the string is in
     * @param aDate a date object
     * @return a formatted string representation of the date
     * @see SimpleDateFormat
     */
    public static final String getDateTime(String aMask, Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";

        if (aDate == null) {
            System.out.print("aDate is null!");
        } else {
            df = new SimpleDateFormat(aMask);
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }

    /**
     * This method generates a string representation of a date based
     * on the System Property 'dateFormat'
     * in the format you specify on input
     *
     * @param aDate A date to convert
     * @return a string representation of the date
     */
    public static final String convertDateToString(Date aDate) {
        return getDateTime(datePattern, aDate);
    }

    /**
     * This method converts a String to a date using the datePattern
     *
     * @param strDate the date to convert (in format MM/dd/yyyy)
     * @return a date object
     * @throws ParseException
     */
    public static Date convertStringToDate(String strDate)
            throws ParseException {
        Date aDate = null;

        try {

            aDate = convertStringToDate(datePattern, strDate);
        } catch (ParseException pe) {
            //log.error("Could not convert '" + strDate
            //          + "' to a date, throwing exception");
            pe.printStackTrace();
            return null;

        }
        return aDate;
    }

    //日期格式转换成时间戳
    public static long getTimeStamp(String pattern, String strDate) {
        long returnTimeStamp = 0;
        Date aDate = null;
        try {
            aDate = convertStringToDate(pattern, strDate);
        } catch (ParseException pe) {
            aDate = null;
        }
        if (aDate == null) {
            returnTimeStamp = 0;
        } else {
            returnTimeStamp = aDate.getTime();
        }
        return returnTimeStamp;
    }

    //获取当前日期的邮戳
    public static long getNowTimeStamp() {
        long returnTimeStamp = 0;
        Date aDate = null;
        try {
            aDate = convertStringToDate("yyyy-MM-dd HH:mm:ss", getNowDateTime());
        } catch (ParseException pe) {
            aDate = null;
        }
        if (aDate == null) {
            returnTimeStamp = 0;
        } else {
            returnTimeStamp = aDate.getTime();
        }
        return returnTimeStamp;
    }

    /**
     * 得到格式化后的系统当前日期
     *
     * @param strScheme 格式模式字符串
     * @return 格式化后的系统当前时间，如果有异常产生，返回空串""
     * @see java.text.SimpleDateFormat
     */
    public static final String getNowDateTime(String strScheme) {
        String strReturn = null;
        Date now = new Date();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(strScheme);
            strReturn = sdf.format(now);
        } catch (Exception e) {
            strReturn = "";
        }
        return strReturn;
    }

    public static final String getNowDateTime() {
        String strReturn = null;
        Date now = new Date();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            strReturn = sdf.format(now);
        } catch (Exception e) {
            strReturn = "";
        }
        return strReturn;
    }

    public static final String getNowDate() {
        String strReturn = null;
        Date now = new Date();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            strReturn = sdf.format(now);
        } catch (Exception e) {
            strReturn = "";
        }
        return strReturn;
    }

    /**
     * 转化日期格式"MM/dd/YY、MM.dd.YY、MM-dd-YY、MM/dd/YY"，并输出为正常的格式yyyy-MM-dd
     *
     * @param strDate 待转换的日期格式
     * @return 格式化后的日期，如果有异常产生，返回空串""
     * @see java.text.SimpleDateFormat
     */
    public static final String convertNormalDate(String strDate) {
        String strReturn = null;
        //先把传入参数分格符转换成java认识的分格符
        String[] date_arr = strDate.split("\\.|\\/|\\-");
        try {
            if (date_arr.length == 3) {
                if (date_arr[2].length() != 4) {
                    String nowYear = getNowDateTime("yyyy");
                    date_arr[2] = nowYear.substring(0, 2) + date_arr[2];
                }
                strReturn = DateUtil.getDateTime("yyyy-MM-dd",
                        convertStringToDate(combineStringArray(date_arr, "/")));
            }

        } catch (Exception e) {
            return strReturn;
        }
        return strReturn;
    }

    /**
     * 将字符串数组使用指定的分隔符合并成一个字符串。
     *
     * @param array 字符串数组
     * @param delim 分隔符，为null的时候使用""作为分隔符（即没有分隔符）
     * @return 合并后的字符串
     * @since 0.4
     */
    public static final String combineStringArray(String[] array, String delim) {
        int length = array.length - 1;
        if (delim == null) {
            delim = "";
        }
        StringBuffer result = new StringBuffer(length * 8);
        for (int i = 0; i < length; i++) {
            result.append(array[i]);
            result.append(delim);
        }
        result.append(array[length]);
        return result.toString();
    }

    public static final int getWeekNum(String strWeek) {
        int returnValue = 0;
        if (strWeek.equals("Mon")) {
            returnValue = 1;
        } else if (strWeek.equals("Tue")) {
            returnValue = 2;
        } else if (strWeek.equals("Wed")) {
            returnValue = 3;
        } else if (strWeek.equals("Thu")) {
            returnValue = 4;
        } else if (strWeek.equals("Fri")) {
            returnValue = 5;
        } else if (strWeek.equals("Sat")) {
            returnValue = 6;
        } else if (strWeek.equals("Sun")) {
            returnValue = 0;
        } else if (strWeek == null) {
            returnValue = 0;
        }

        return returnValue;
    }

    /**
     * 得到格式化后的指定日期
     *
     * @param strScheme 格式模式字符串
     * @param date      指定的日期值
     * @return 格式化后的指定日期，如果有异常产生，返回空串""
     * @see java.text.SimpleDateFormat
     */
    public static final String getDateTime(Date date, String strScheme) {
        String strReturn = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(strScheme);
            strReturn = sdf.format(date);
        } catch (Exception e) {
            strReturn = "";
        }

        return strReturn;
    }

    /**
     * 获取日期
     *
     * @param timeType 时间类型，譬如：Calendar.DAY_OF_YEAR
     * @param timenum  时间数字，譬如：-1 昨天，0 今天，1 明天
     * @return 日期
     */
    public static final Date getDateFromNow(int timeType, int timenum) {
        Calendar cld = Calendar.getInstance();
        cld.set(timeType, cld.get(timeType) + timenum);
        return cld.getTime();
    }

    /**
     * 获取日期
     *
     * @param timeType      时间类型，譬如：Calendar.DAY_OF_YEAR
     * @param timenum       时间数字，譬如：-1 昨天，0 今天，1 明天
     * @param format_string 时间格式，譬如："yyyy-MM-dd HH:mm:ss"
     * @return 字符串
     */
    public static final String getDateFromNow(int timeType, int timenum, String format_string) {
        if ((format_string == null) || (format_string.equals("")))
            format_string = "yyyy-MM-dd HH:mm:ss";
        Calendar cld = Calendar.getInstance();
        Date date = null;
        DateFormat df = new SimpleDateFormat(format_string);
        cld.set(timeType, cld.get(timeType) + timenum);
        date = cld.getTime();
        return df.format(date);
    }

    /**
     * 获取当前日期的字符串
     *
     * @param format_string 时间格式，譬如："yyyy-MM-dd HH:mm:ss"
     * @return 字符串
     */
    public static final String getDateNow(String format_string) {
        if ((format_string == null) || (format_string.equals("")))
            format_string = "yyyy-MM-dd HH:mm:ss";
        Calendar cld = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat(format_string);
        return df.format(cld.getTime());
    }

    public static String getDatePattern2() {
        return datePattern2;
    }

    /**
     * 获得指定日期的前一天日期
     *
     * @param currDate
     * @return
     */
    public static Date getPreviousDate(Date currDate) {
        Date previousDate = null;
        try {
            Calendar calendar = Calendar.getInstance(); //得到日历
            calendar.setTime(currDate);//把当前时间赋给日历
            calendar.add(Calendar.DAY_OF_MONTH, -1);  //设置为前一天
            previousDate = calendar.getTime();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return previousDate;
    }

    /**
     * 判断指定日期是否是星期日
     *
     * @param currDate
     * @return
     */
    public static boolean isSunday(Date currDate) {
        boolean state = false;
        try {

        } catch (Exception ex) {
            state = false;
            ex.printStackTrace();
        }
        return state;
    }


    /**
     * 获得指定日期所在一周的星期一的日期
     *
     * @param currDate
     * @return
     */
    public static Date mondayDate(Date currDate) {
        Date mondayDate = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式

            Calendar cal = Calendar.getInstance();
            cal.setTime(currDate);

            //判断1为星期日，7为周六
            int day = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
            if (day == 2) {//星期一
                return currDate;
            } else if (day == 1) {//星期日，需要加-7天
                cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 6);
                mondayDate = sdf.parse(sdf.format(cal.getTime()));
            } else {//day-2周一相差天数
                cal.set(Calendar.DATE, cal.get(Calendar.DATE) - (day - 2));
                mondayDate = sdf.parse(sdf.format(cal.getTime()));
            }

        } catch (Exception ex) {

            ex.printStackTrace();
        }
        return mondayDate;
    }


    /**
     * 获得指定日期所在一周的星期日的日期
     *
     * @param currDate
     * @return
     */
    public static Date SundayDate(Date currDate) {
        Date sunday = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
            Calendar cal = Calendar.getInstance();
            cal.setTime(currDate);

            //判断1为星期日，7为周六
            int day = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
            if (day == 1) {//星期一
                return currDate;
            } else if (day == 7) {
                cal.set(Calendar.DATE, cal.get(Calendar.DATE) + 1);
                sunday = sdf.parse(sdf.format(cal.getTime()));
            } else {//7-day到周六相差天数
                cal.set(Calendar.DATE, cal.get(Calendar.DATE) + (7 - day + 1));
                sunday = sdf.parse(sdf.format(cal.getTime()));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sunday;
    }

    /**
     * 获得指定日期所在一周在一年中的周数
     *
     * @param currDate
     * @return
     */
    public static int week(Date currDate) {
        int weekNum = 0;
        try {
            //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
            Calendar cal = Calendar.getInstance();
            cal.setFirstDayOfWeek(Calendar.MONDAY);
            cal.setTime(currDate);
            weekNum = cal.get(Calendar.WEEK_OF_YEAR);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return weekNum;
    }


    /**
     * 得到某年某周的第一天
     *
     * @param year
     * @param week
     * @return
     * @throws ParseException
     */

    public static Date getFirstDayOfWeek(int year, int week) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.WEEK_OF_YEAR, week);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);// 设置周一
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(sdf.format(calendar.getTime()));
    }

    /**
     * 得到某年某周的最后一天
     *
     * @param year
     * @param week
     * @return
     * @throws ParseException
     */

    public static Date getLastDayOfWeek(int year, int week) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.WEEK_OF_YEAR, week);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() + 6); // Sunday
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(sdf.format(calendar.getTime()));
    }

    /**
     * 获得指定日期的前一日
     *
     * @return
     */
    public static String getBeforeDate(Date currDate) {
        //获得系统日期的前一天
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(currDate);//把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_MONTH, -1);  //设置为前一天
        Date beforeDate = calendar.getTime();   //得到前一天的时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String beforeDateStr = sdf.format(beforeDate);
        return beforeDateStr;
    }

    /**
     * 获得指定日期的前二日
     *
     * @return
     */
    public static String getBefore2Date(Date currDate) {
        //获得系统日期的前一天
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(currDate);//把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_MONTH, -2);  //设置为前一天
        Date beforeDate = calendar.getTime();   //得到前一天的时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String beforeDateStr = sdf.format(beforeDate);
        return beforeDateStr;
    }

    /**
     * 获得指定日期偏移offset日期
     *
     * @return
     */
    public static String getBefore2Date(Date currDate, int offset) {
        //获得系统日期的前一天
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(currDate);//把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_MONTH, offset);  //设置为前一天
        Date beforeDate = calendar.getTime();   //得到前一天的时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String beforeDateStr = sdf.format(beforeDate);
        return beforeDateStr;
    }

    /**
     * 获得指定日期的前一日
     *
     * @return
     */
    public static Date getBeforeDateByDate(Date currDate) {
        //获得系统日期的前一天
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(currDate);//把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_MONTH, -1);  //设置为前一天
        Date beforeDate = calendar.getTime();   //得到前一天的时间
        return beforeDate;
    }

    public static Date getPrevDate(Date currDate) {
        //获得系统日期的前一天
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(currDate);//把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_MONTH, -1);  //设置为前一天
        Date beforeDate = calendar.getTime();   //得到前一天的时间
        return beforeDate;
    }

    /**
     * 获得指定日期的后一日
     *
     * @return
     */
    public static String getAfterDate(Date currDate) {
        //获得系统日期的前一天
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(currDate);//把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_MONTH, 1);  //设置为后一天
        Date afterDate = calendar.getTime();   //得到前一天的时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String afterDateStr = sdf.format(afterDate);
        return afterDateStr;
    }

    public static Date getBackDate(Date currDate) {
        //获得系统日期的前一天
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(currDate);//把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_MONTH, 1);  //设置为后一天
        Date afterDate = calendar.getTime();   //得到后一天的时间
        return afterDate;
    }

    /**
     * 获得指定日期的前一月
     *
     * @return
     */
    public static String getBeforeMonth(Date currDate) {
        //获得系统日期的前一天
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currDate);
        calendar.add(Calendar.MONTH, -1);  //设置为前一月
        Date beforeDate = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String beforeDateStr = sdf.format(beforeDate);
        return beforeDateStr;
    }

    /**
     * 取得当前日期对应周的前一周
     *
     * @param date
     * @return
     */
    public static int getBeforeWeekOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setMinimalDaysInFirstWeek(1);
        calendar.setTime(date);
        calendar.add(Calendar.WEEK_OF_YEAR, -1);  //设置为前一周
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 取得当前日期的年份
     *
     * @param date
     * @return
     */
    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setMinimalDaysInFirstWeek(1);
        Date beforeDate = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String beforeDateStr = sdf.format(beforeDate);
        return Integer.valueOf(beforeDateStr).intValue();
    }

    public static String getDatePattern3() {
        return datePattern3;
    }

    /**
     * 获得指定日期的当月第一天
     *
     * @return
     */
    public static Date getBeginDayOfMonth(Date currDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currDate);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * 获得指定日期的当月最后一天
     *
     * @return
     */
    public static Date getEndDayOfMonth(Date currDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currDate);
        calendar.set(Calendar.DATE, 1);
        calendar.roll(Calendar.DATE, -1);
        return calendar.getTime();
    }

    /**
     * 获取指定日期当月的天数
     *
     * @param currDate
     * @return
     */
    public static int getDayNumOfMonth(Date currDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currDate);
        int dayNum = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        return dayNum;
    }

    /**
     * 获得指定日期N天前的日期
     *
     * @param currDate
     * @param day
     * @return
     */
    public static Date getDateBeforeDay(Date currDate, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currDate);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - day);
        return calendar.getTime();
    }

    /**
     * 获得指定日期N天后的日期
     *
     * @param currDate
     * @param day
     * @return
     */
    public static Date getDateAfterDay(Date currDate, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currDate);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + day);
        return calendar.getTime();
    }

    /**
     * 获得指定日期的前一小时
     *
     * @param currDate
     * @return
     */
    public static Date getPrevHour(Date currDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currDate);
        calendar.add(Calendar.HOUR_OF_DAY, -1);//24小时制
        return calendar.getTime();
    }

    /**
     * 获得指定日期的小时
     *
     * @param currDate
     * @return
     */
    public static String getHourOfDataStr(Date currDate) {
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(currDate);
//		return calendar.get(Calendar.HOUR_OF_DAY);
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        return sdf.format(currDate);
    }

    /**
     * 获得前一个小时的日期
     *
     * @return
     */
    public static String getOneHourAgoYmd() {
        Date prevHourDate = getPrevHour(new Date());
        return date2Str(datePattern3, prevHourDate);
    }

    /**
     * 获得前一个小时的日期
     *
     * @return
     */
    public static String getOneHourAgo() {
        Date prevHourDate = getPrevHour(new Date());
        return date2Str("HH", prevHourDate);
    }

    /**
     * 获得前一个小时的日期
     *
     * @return
     */
    public static String get2DaysAgoYmd() {
        Date prevHourDate = getPrevHour(new Date());
        return date2Str(datePattern3, getPreviousDate(prevHourDate));
    }


    /**
     * 返回年月格式
     */
    public static String getDatePattern4() {
        return datePattern4;
    }

    public static void main(String[] args) {
        System.out.println(getNowTimeStamp());
        System.out.println(getNowDateTime());
        System.out.println(getNowDate());
        System.out.println(getBeforeDate(new Date()));
        System.out.println(getOneHourAgoYmd());
        System.out.println(get2DaysAgoYmd());
    }

}
